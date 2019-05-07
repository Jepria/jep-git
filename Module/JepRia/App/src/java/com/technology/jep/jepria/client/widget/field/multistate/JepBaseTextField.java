package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.TYPING_TIMEOUT_DEFAULT_VALUE;
import static com.technology.jep.jepria.client.JepRiaClientConstant.TYPING_TIMEOUT_MIN_TEXT_SIZE;
import static com.technology.jep.jepria.client.util.JepClientUtil.isSpecialKey;
import static com.technology.jep.jepria.client.widget.event.JepEventType.LOST_FOCUS_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.TYPING_TIMEOUT_EVENT;

import java.util.Objects;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;

/**
 * Базовый класс для производных текстового поля.<br/>
 * <br/>
 * Концепция поддержки обработки событий отражена в описании пакета {@link com.technology.jep.jepria.client.widget}.
 * <dl>
 *   <dt>Поддерживаемые типы событий {@link com.technology.jep.jepria.client.widget.event.JepEvent}:</dt>
 *   <dd>{@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_VALUE_EVENT CHANGE_VALUE_EVENT}</dd>
 *   <dd>{@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT TYPING_TIMEOUT_EVENT}</dd>
 *   <dd>{@link com.technology.jep.jepria.client.widget.event.JepEventType#LOST_FOCUS_EVENT LOST_FOCUS_EVENT}</dd>
 * </dl>
 * 
 * Особенности:<br/>
 * Необходимость данной абстракции обусловлена тем, чтобы в прикладном коде избавиться от потребности генерализации поля {@link JepTextField} и его производных
 */
@SuppressWarnings("rawtypes")
public abstract class JepBaseTextField<E extends Widget & HasValue> extends JepMultiStateField<E, HTML> {

  /**
   * Отложенная задача для выполнения при наступлении события {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }
   */
  protected Timer delayedTask = null;
    
  /**
   * Тайм-аут для фиксации паузы в клавиатурном наборе
   */
  protected int typingTimeout = TYPING_TIMEOUT_DEFAULT_VALUE;
  
  /**
   * Количество символов, необходимых для срабатывания события {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }
   */
  protected int typingTimeoutMinTextSize =  TYPING_TIMEOUT_MIN_TEXT_SIZE;
  
  /**
   * Обработчики событий нажатия клавиш клавиатуры
   */
  protected HandlerRegistration keyDownHandler, keyPressHandler, keyUpHandler;
  
  @Deprecated
  public JepBaseTextField() {
    this(null);
  }
  
  @Deprecated
  public JepBaseTextField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepBaseTextField(String fieldIdAsWebEl, String fieldLabel) {
    super(fieldIdAsWebEl, fieldLabel);
  }
  
  /**
   * Установка значения поля.<br/>
   * При установке значения поля, устанавливаются значения для обеих карт сразу: для карты Редактирования и карты Просмотра.<br/>
   * После установки значения карты Редактирования происходит скрытие/стирание сообщения инвалидации.<br/><br/>
   *
   * @param value значение поля
   */
  @Override
  @SuppressWarnings("unchecked")
  public void setValue(Object value) {
    Object oldValue = getValue();
    if(!Objects.equals(oldValue, value)) {
      editableCard.setValue(value);
      clearInvalid();
      setViewValue(value);
    }
  }
  
  /**
   * Установка текста по умолчанию для пустого (незаполненного значением) поля.
   * 
   * @param emptyText пустой текст
   */
  public void setEmptyText(String emptyText){
    getInputElement().setPropertyString("placeholder", emptyText);
  }
  
  /**
   * {@inheritDoc}
   */
  public void setEnabled(boolean enabled) {
    getInputElement().setPropertyBoolean("disabled", !enabled);
  }
  
  /**
   * Очищает значение поля.<br/>
   * При очистке значения поля, очищаются значения для обеих карт сразу: для карты Редактирования и карты Просмотра.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void clear() {
    super.clear();
    editableCard.setValue(null);
  }
  
  /**
   * Установка тайм-аута дял ввода данных, после которого произойдет событие {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }
   * 
   * @param typingTimeout устанавливаемый тайм-аут (в секундах)
   */
  public void setTypingTimeout(int typingTimeout) {
    this.typingTimeout = typingTimeout;
  }
  
  /**
   * Получение значения минимального размера текстового сообщения, после ввода которого произойдет событие {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }
   * 
   * @return соответствующее значение
   */
  public int getTypingTimeoutMinTextSize() {
    return typingTimeoutMinTextSize;
  }
  
  /**
   * Установка минимального размера текстового сообщения для срабатывания события {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }
   * 
   * @param minTextSize минимальный размер текстового сообщения (в количестве символов)
   */
  public void setTypingTimeoutMinTextSize(int minTextSize) {
    this.typingTimeoutMinTextSize = minTextSize;
  }
  
  /**
   * Добавление слушателя определенного типа собитий.<br/>
   * Концепция поддержки обработки событий и пример реализации метода отражен в описании пакета {@link com.technology.jep.jepria.client.widget}.
   *
   * @param eventType тип события
   * @param listener слушатель
   */
  @Override
  public void addListener(JepEventType eventType, JepListener listener) {
    switch(eventType) {
      case CHANGE_VALUE_EVENT:
        addChangeValueListener();
        break;
      case TYPING_TIMEOUT_EVENT: 
        addTypingTimeoutListener(); 
        break;
      case LOST_FOCUS_EVENT:
        addLostFocusListener();
        break;
    }
    
    super.addListener(eventType, listener);
  }
  
  /**
   * Добавление Gxt прослушивателей для реализации прослушивания события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_VALUE_EVENT } .<br/>
   * В текущей версии - пустая реализация. Добавлено для возможной перегрузки в наследниках.
   */
  protected void addChangeValueListener() {
  
  }
  
  /**
   * Добавление Gxt прослушивателей для реализации прослушивания события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }.
   */
  protected void addTypingTimeoutListener() {
    if(delayedTask == null) {
      // Обработка тайм-аута
      delayedTask = new Timer() {
        @Override
        public void run() {
          // Очистим карту от ошибок.
          clearInvalid();
          // Отобразим индикатор загрузки.
          setLoadingImage(true);
          notifyListeners(TYPING_TIMEOUT_EVENT, new JepEvent(JepBaseTextField.this));
        }
      };
    }
    // Вешаем обработчики клавиатуры.
    initKeyboardHandlers();
  }
  
  /**
   * Добавление Gxt прослушивателей для реализации прослушивания события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#LOST_FOCUS_EVENT }.
   */
  protected void addLostFocusListener() {
    editableCard.addDomHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        notifyListeners(LOST_FOCUS_EVENT, new JepEvent(JepBaseTextField.this));
      }
    }, BlurEvent.getType());
  }
    
  /**
   * Установка максимальной длины поля 
   * Особенность:
   * Наличие слушателя события KeyPress, запрещающего возможность ввода символов больше максимально заданного количества.
   * По умолчанию, пользователь может ввести сколь угодно много символов и проверка будет осуществена лишь при валидации поля.
   * 
   * @param maxLength максимальная длина поля
   */
  public void setMaxLength(int maxLength){
    getInputElement().setAttribute("maxLength", maxLength + "");
  }
  
  /**
   * Возвращает максимальную длину поля
   * @return максимальная длина поля
   */
  public Integer getMaxLength(){
    try {
      return Integer.valueOf(getInputElement().getAttribute("maxLength")); 
    } catch (Exception e) {
      return Integer.MAX_VALUE;
    }
  }
  
  /**
   * Обработка события "нажатия клавиши" в поле.
   * 
   * @param event  срабатываемое событие
   * 
   * Особенности:<br/>
   * В случае необходимости добавления функциональности в обработку события, необходимо перекрыть данный метод,
   * а не добавлять новый обработчик события, чтобы обеспечить единую точку входа и обработки данного события.
   */
  protected boolean keyPressEventHandler(DomEvent<?> event){
    if(delayedTask != null) {
      int keyCode = event.getNativeEvent().getKeyCode();
      /*
       * Нажатие TAB не должно прекращать выполнение task'а.
       */
      if (keyCode != KeyCodes.KEY_TAB) {
        delayedTask.cancel();
      }
    }
    return true;
  }

  /**
   * Обработка события "поднятия клавиши" в поле
   * 
   * @param event  срабатываемое событие
   * 
   * Особенности:<br/>
   * В случае необходимости добавления функциональности в обработку события, необходимо перекрыть данный метод,
   * а не добавлять новый обработчик события, чтобы обеспечить единую точку входа и обработки данного события.
   */
  protected void keyUpEventHandler(DomEvent<?> event){
    if(delayedTask != null) {
      int keyCode = event.getNativeEvent().getKeyCode();
      if (keyCode == KeyCodes.KEY_BACKSPACE || keyCode == KeyCodes.KEY_DELETE || !isSpecialKey(event)) {
        startTypingTimeout();
      }
    }
  }
  
  /**
   * Обработка события "опускания клавиши" в поле
   * 
   * @param event  срабатываемое событие
   * Особенности:<br/>
   * В случае необходимости добавления функциональности в обработку события, необходимо перекрыть данный метод,
   * а не добавлять новый обработчик события, чтобы обеспечить единую точку входа и обработки данного события.
   */
  protected boolean keyDownEventHandler(DomEvent<?> event){
    if(delayedTask != null) {
      int keyCode = event.getNativeEvent().getKeyCode();
      /*
       * Нажатие TAB не должно прекращать выполнение task'а.
       */
      if (keyCode != KeyCodes.KEY_TAB) {
        delayedTask.cancel();
      }
    }
    return true;
  }
  
  /**
   * Запуск задачи по тайм-ауту, когда длина вводимой строки превышает необходимое минимальное значение
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepBaseTextField#setTypingTimeoutMinTextSize(int minTextSize)}.
   */
  protected void startTypingTimeout() {
    String rawValue = getRawValue();
    if(rawValue != null && rawValue.length() >= this.typingTimeoutMinTextSize) {
      delayedTask.schedule(typingTimeout);
    }
  }
  
  /**
   * Процедура, выполняемая по успешному завершению обработчика события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT } .
   */
  public void afterTypingTimeoutSuccess() {
    setLoadingImage(false);
  }
  
  /**
   * Процедура, выполняемая по НЕуспешному завершению обработчика события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT } .
   */
  public void afterTypingTimeoutFailure() {
    setLoadingImage(false);
  }

  /**
   * Инициализация обработчиков нажатия кнопок клавиатуры.
   */
  protected void initKeyboardHandlers() {
    initKeyDownHandler();
    initKeyPressHandler();
    initKeyUpHandler();
    initPasteHandler(this.getEditableCard().getElement());
  }
  
  /**
   * Инициализация обработчика "опускания клавиши" {@link com.google.gwt.event.dom.client.KeyDownEvent}.
   */
  protected void initKeyDownHandler(){
    if (keyDownHandler == null) {
      keyDownHandler = editableCard.addDomHandler(new KeyDownHandler() {
        @Override
        public void onKeyDown(KeyDownEvent event) {
          keyDownEventHandler(event);
        }
      }, KeyDownEvent.getType());
    }
  }
  
  /**
   * Инициализация обработчика "нажатия клавиши" {@link com.google.gwt.event.dom.client.KeyPressEvent}.
   */
  protected void initKeyPressHandler(){
    if (keyPressHandler == null) {
      keyPressHandler = editableCard.addDomHandler(new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent event) {
          keyPressEventHandler(event);
        }
      }, KeyPressEvent.getType());
    }
  }
  
  /**
   * Инициализация обработчика "поднятия клавиши" {@link com.google.gwt.event.dom.client.KeyUpEvent}.
   */
  protected void initKeyUpHandler(){
    if (keyUpHandler == null) {
      keyUpHandler = editableCard.addDomHandler(new KeyUpHandler() {
        @Override
        public void onKeyUp(KeyUpEvent event) {
          keyUpEventHandler(event);
        }
      }, KeyUpEvent.getType());
    }
  }

  /**
   * Инициализация обработчика "вставки".
   */
  protected void handlePaste(String value) {
    getInputElement().setPropertyString("value", value);
    startTypingTimeout();
  }
  
  /**
   * Перехват и обработка Paste Event.
   * @param element объект поля.
   */
  private native void initPasteHandler(Element element)
  /*-{
      var temp = this;  // hack to hold on to 'this' reference
      element.onpaste = function(e) {
        var clipBoardData;
        e.preventDefault();//Заблокируем стандартное поведение события.
        e.stopPropagation();
        //Вручную добавляем выделенный текст из поля в буфер обмена.
        if (e.clipboardData) { 
          clipBoardData = e.clipboardData.getData('text/plain');
        }
        if ($wnd.clipboardData) {
          clipBoardData = $wnd.clipboardData.getData("Text");
        }
        temp.@com.technology.jep.jepria.client.widget.field.multistate.JepBaseTextField::handlePaste(Ljava/lang/String;)(clipBoardData);
      }
  }-*/;
}