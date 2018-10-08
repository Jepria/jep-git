package com.technology.jep.jepria.client.widget.field.masked;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.util.JepClientUtil.getChar;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.multistate.event.InputForbiddenEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.InputForbiddenEvent.HasInputForbiddenHandlers;
import com.technology.jep.jepria.client.widget.field.multistate.event.InputForbiddenEvent.InputForbiddenHandler;
import com.technology.jep.jepria.client.widget.field.multistate.event.PasteForbiddenEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.PasteForbiddenEvent.HasPasteForbiddenHandlers;
import com.technology.jep.jepria.client.widget.field.multistate.event.PasteForbiddenEvent.PasteForbiddenHandler;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Класс-наследник GWT-виджета TextBox, использующий маску.
 */
public class MaskedTextBox extends TextBox
  implements HasInputForbiddenHandlers, HasPasteForbiddenHandlers {

  /**
   * Наименование CSS-класса стилей для поля с маской.
   */
  protected static final String MASKED_TEXT_BOX_STYLE = "jepRia-MaskedTextBox";
  
  /**
   * Содержимое поля в виде массива символов.
   */
  protected char[] charValue;
  /**
   * Флаг, показывающий, что требуется вызвать событие {@link InputForbiddenEvent}.
   */
  private boolean fireInputForbidden = false;
  /**
   * Маска, наложенная на поле.
   */
  protected Mask mask;
  
  public MaskedTextBox(Mask mask) {
    this.mask = mask;
    setCharValue(new char[mask.size()]);
    
    addDomHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        onKeyDownEvent(event);
      }
    }, KeyDownEvent.getType());
    addDomHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        onKeyUpEvent(event);
      }
    }, KeyUpEvent.getType());
    addDomHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        onFocusEvent(event);
      }
    }, FocusEvent.getType());
    addDomHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent blurevent) {
        obBlurEvent(blurevent);
      }
    }, BlurEvent.getType());
    
    addStyleName(MASKED_TEXT_BOX_STYLE);
    
    if (!JepRiaUtil.isAndoridMobile()) {
      sinkEvents(Event.ONPASTE);
      addDomHandler(new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent event) {
          onKeyPressEvent(event);
        }
      }, KeyPressEvent.getType());
      this.addCutHandler(this.getElement());
      this.addPreventUndoRedoHandler(this.getElement());
    }
  }

  public MaskedTextBox(String mask) {
    this(new Mask(mask));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addInputForbiddenHandler(InputForbiddenHandler handler) {
    return addHandler(handler, InputForbiddenEvent.getType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addPasteForbiddenHandler(
      PasteForbiddenHandler handler) {
    return addHandler(handler, PasteForbiddenEvent.getType());
  }
  
  /**
   * Очищает поле.
   */
  public void clear() {
    setCharValue(new char[mask.size()]);
  }

  /**
   * Возвращает наложенную на поле маску.
   * @return маска
   */
  public Mask getMask() {
    return mask;
  }
  
  /**
   * Возвращает значение поля в виде строки.<br>
   * Если значение не соответствует маске (введены не все обязательные символы),
   * возвращает null
   * @return значение поля
   */
  @Override
  public String getValue() {
    if (mask.match(charValue, false)) {
        return mask.getText(charValue, false);
    }
    else {
      return null;
    }
  }
  
  /**
   * Проверяет, является ли значение в поле корректным.<br>
   * Значение считается корректным, если оно пусто и/или введены
   * все обязательные символы.
   * @return true, если значение корректно, и false в противном случае
   */
  public boolean isValid() {
    return isEmpty() || mask.match(charValue, false);
  }

  /**
   * Обработчик события браузера.<br>
   * Переопределён для перехвата события вставки. При наступлении события выполняется проверка,
   * допустима ли вставка, выполняется вставка и корректируется позиция курсора. 
   * Если часть содержимого поля выделена, она предварительно сбрасывается.
   * @param event событие браузера
   */
  @Override
  public void onBrowserEvent(Event event) {
    if (event.getTypeInt() == Event.ONPASTE) {
      onPasteEvent(event);
    }
    else {
      super.onBrowserEvent(event);
    }
  }

  /**
   * Установка маски на поле.<br>
   * Если имеющееся в поле значение противоречит маске, имеющееся значение сбрасывается.
   * @param mask маска
   */
  public void setMask(Mask mask) {
    this.mask = mask;
    if (!mask.match(charValue, true)) {
      setCharValue(mask.getCharArray(""));
    }
    else {
      char[] newCharValue = new char[mask.size()];
      System.arraycopy(charValue, 0, newCharValue, 0, Mask.getEffectiveLength(charValue));
      setCharValue(newCharValue);
    }
  }
  
  /**
   * Установка маски на поле.<br>
   * Если имеющееся в поле значение противоречит маске, имеющееся значение сбрасывается.
   * @param mask строковое представление маски
   */
  public void setMask(String mask) {
    setMask(new Mask(mask));
  }
  
  /**
   * Установка значения поля.<br>
   * Предварительно проверяет, соответствует ли маске устанавливаемое значение.
   * @param value значение
   * @param fireEvents если true, вызывает событие {@link com.google.gwt.event.logical.shared.ValueChangeEvent}
   * @throws IllegalArgumentException если значение не соответствует маске
   */
  @Override
  public void setValue(String value, boolean fireEvents) {
    boolean empty = JepRiaUtil.isEmpty(value);
    if (!empty && !mask.match(value)) {
      throw new IllegalArgumentException(JepClientUtil.substitute(JepTexts.maskedTextField_errorMessage(), value));
    }
    String oldValue = getValue();
    setCharValue(empty ? mask.getCharArray("") : mask.getCharArray(value));
    if (fireEvents) {
      ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
    }
  }

  /**
   * Native-метод, извлекающий содержимое буфера обмена.<br>
   * Источник: <a href="http://comments.gmane.org/gmane.org.google.gwt/46644">http://comments.gmane.org/gmane.org.google.gwt/46644</a> <br>
   * Не работает в Firefox ниже версии 22.
   * @param event событие
   * @return содержимое буфера обмена
   */
  private static native String getClipboardData(Event event)
  /*-{
    var text = "";

    if (event.clipboardData) {
      // Chrome, Firefox 22+
      try {
        text = event.clipboardData.getData("Text");
        return text;
      }
      catch (e) {
        // Не должно произойти.
      }
    }

    if ($wnd.clipboardData) {
      // IE
      try {
        text = $wnd.clipboardData.getData("Text");
        return text;
      }
      catch (e) {
        // Не должно произойти.
      }
    }

    return text;
  }-*/;

  /**
   * Проверка, пусто ли содержимое.
   * @return true, если пусто, и false, если нет
   */
  protected boolean isEmpty() {
    return mask.isValueEmpty(charValue);
  }

  /**
   * Обработчик события {@link com.google.gwt.event.dom.client.KeyDownEvent}.
   * Осуществляет перехват и обработку нажатия на служебные клавиши
   * (left, right, backspace, delete).
   * @param event событие
   */
  protected void onKeyDownEvent(KeyDownEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
      event.preventDefault();
      int cursorPos = getCursorPos();
      int selectionLength = getSelectionLength();
      if (selectionLength > 0) {
        setCharValue(mask.clearChars(charValue, cursorPos, selectionLength));
        setCursorPos(cursorPos);
      }
      else {
        if (cursorPos > 0) {
          char[] newRawValue = mask.removeChar(charValue, cursorPos - 1);
          int newCursorPos = mask.getCursorPositionOnLeft(cursorPos);
          setCharValue(newRawValue);
          setCursorPos(newCursorPos);
        }
      }
    }
    else if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
      event.preventDefault();
      int cursorPos = getCursorPos();
      int selectionLength = getSelectionLength();
      if (selectionLength > 0) {
        setCharValue(mask.clearChars(charValue, cursorPos, selectionLength));
      }
      else {
        if (cursorPos < mask.size()) {
          setCharValue(mask.removeChar(charValue, cursorPos));
        }    
      }
      setCursorPos(cursorPos);
    }
    else if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
      if (!event.isAltKeyDown() && !event.isControlKeyDown() && !event.isShiftKeyDown() && !event.isMetaKeyDown()) {
        event.preventDefault();
        setCursorPos(mask.getCursorPositionOnLeft(getCursorPos()));
      }
    }
    else if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
      if (!event.isAltKeyDown() && !event.isControlKeyDown() && !event.isShiftKeyDown() && !event.isMetaKeyDown()) {
        event.preventDefault();
        setCursorPos(mask.getCursorPositionOnRight(getCursorPos()));
      }
    }
  }

  /**
   * Обработчик события {@link com.google.gwt.event.dom.client.KeyPressEvent}.<br>
   * Осуществляет перехват и обработку пользовательского ввода.
   * @param event событие
   */
  protected void onKeyPressEvent(KeyPressEvent event) {
    if (event.getNativeEvent().getCharCode() == 0) {
      /*
       * Firefox имеет особенность: событие KeyPress генерируется не только при нажатии
       * на алфавитно-цифровые клавиши, поэтому необходимо проверять значение charCode 
       * на равенство нулю.
       */
      return;
    }        
    if (event.isAltKeyDown() || event.isControlKeyDown() || event.isMetaKeyDown()) {
      /*
       * Не реагируем, если нажата одна из клавиш Alt, Ctrl или Meta, 
       * иначе не будут работать сочетания клавиш наподобие Ctrl-C, Ctrl-V.
       */
      return;
    }
    event.preventDefault();
    if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
      /*
       * Необходимо игнорировать нажатие Enter, иначе в IE будет введён
       * символ возврата каретки.
       */
      return;
    }
    
    int position = getCursorPos();
    char currentCharacter = getChar(event.getNativeEvent());
    int selectionLength = getSelectionLength();
    if (selectionLength > 0) {
      char[] tempRawValue = mask.clearChars(charValue, position, selectionLength);
      if (mask.canInsert(tempRawValue, position, currentCharacter)) {
        char[] newRawValue = mask.insertChar(tempRawValue, position, currentCharacter);
        int newCurPos = mask.getCursorPositionOnInsert(tempRawValue, position, currentCharacter);
        setCharValue(newRawValue);
        setCursorPos(newCurPos);
      }
      else {
        fireInputForbidden = false;
      }
    }
    else {
      if (mask.canInsert(charValue, position, currentCharacter)) {
        char[] newRawValue = mask.insertChar(charValue, position, currentCharacter);
        int newCurPos = mask.getCursorPositionOnInsert(charValue, position, currentCharacter);
        setCharValue(newRawValue);
        setCursorPos(newCurPos);
      }
      else {
        fireInputForbidden = true;
      }
    }
  }
  
  /**
   * Обработчик события {@link KeyUpEvent}.<br>
   * Если установлен флаг {@link com.technology.jep.jepria.client.widget.field.masked.MaskedTextBox#fireInputForbidden}, 
   * вызывает событие {@link InputForbiddenEvent} и сбрасывает флаг.
   * @param event событие
   */
  protected void onKeyUpEvent(KeyUpEvent event) {
    if (fireInputForbidden) {
      fireInputForbidden = false;
      fireEvent(new InputForbiddenEvent());
    }
  }

  /**
   * Обработчик события вставки.
   * @param event событие
   */
  private void onPasteEvent(Event event) {
    String clipboardData = getClipboardData(event);
    event.stopPropagation();
    event.preventDefault();
    if (!JepRiaUtil.isEmpty(clipboardData)) {
      int position = getCursorPos();
      int selectionLength = getSelectionLength();
      if (selectionLength > 0) {
        char[] tempCharValue = mask.clearChars(charValue, position, selectionLength);
        if (mask.canPaste(tempCharValue, position, clipboardData)) {
          char[] newRawValue = mask.paste(tempCharValue, position, clipboardData);
          int newCurPos = mask.getCursorPositionOnPaste(tempCharValue, position, clipboardData);
          setCharValue(newRawValue);
          setCursorPos(newCurPos);
        }
        else {
          fireEvent(new PasteForbiddenEvent());
        }
      }
      else {
        if (mask.canPaste(charValue, position, clipboardData)) {
          char[] newCharValue = mask.paste(charValue, position, clipboardData);
          int newCurPos = mask.getCursorPositionOnPaste(charValue, position, clipboardData);
          setCharValue(newCharValue);
          setCursorPos(newCurPos);
        }
        else {
          fireEvent(new PasteForbiddenEvent());
        }
      }
    }
  }

  /**
   * Обработчик события {@link FocusEvent}.<br> 
   * Если поле пустое, то устанавливает курсор на первый специальный символ маски.
   * @param event
   */
  protected void onFocusEvent(FocusEvent event) {
    if(isEmpty()) {
      setCursorPos(mask.getFirstSpecialSymbolPosition());
    }
  }
  
  /**
   * Обработчик события при потере фокуса на поле
   * @param event
   */
  protected void obBlurEvent(BlurEvent event) {
    if(isEmpty()) {
      clear();
    }
  }

  /**
   * Перехват событий изменения значения поля.
   * @param element объект поля.
   */
  private native void addPreventUndoRedoHandler(Element element)
  /*-{
      var temp = this;  // hack to hold on to 'this' reference
      element.oninput = function(e) {
          temp.@com.technology.jep.jepria.client.widget.field.masked.MaskedTextBox::handleUndoRedo(Ljava/lang/String;)(element.value);
      }
  }-*/;
  
  /**
   * Предотвращение сбоя в работе Mask при Undo/Redo.(В частности в IE)
   * @param value новое значение поля.
   */
  private void handleUndoRedo(String value) {
    if (!mask.canPaste(mask.clearChars(charValue, 0, charValue.length - 1), 0, value)) {
      setCharValue(this.charValue);
      setCursorPos(mask.getFirstSpecialSymbolPosition());
    }
  }
  
  /**
   * Перехват и обработка Cut Event.
   * @param element объект поля.
   */
  private native void addCutHandler(Element element)
  /*-{
      var temp = this;  // hack to hold on to 'this' reference
      element.oncut = function(e) {
        e.preventDefault();//Заблокируем стандартное поведение события.
        e.stopPropagation();
        var selectedText = temp.@com.technology.jep.jepria.client.widget.field.masked.MaskedTextBox::getSelectedText()();
        //Вручную добавляем выделенный текст из поля в буфер обмена.
        if (e.clipboardData) { 
          e.clipboardData.setData('text/plain', selectedText);
        }
        if ($wnd.clipboardData) {
          $wnd.clipboardData.setData("Text", selectedText);
        }
        temp.@com.technology.jep.jepria.client.widget.field.masked.MaskedTextBox::handleCut()();
      }
  }-*/;

  /**
   * Вырезание выделенного текста из поля.
   */
  private void handleCut() {
    int position = getCursorPos();
    int selectionLength = getSelectionLength();
    if (selectionLength > 0) {
      char[] tempCharValue = (position + selectionLength) <= charValue.length ? 
        mask.clearChars(charValue, position, selectionLength) :
          mask.clearChars(charValue, position - selectionLength, selectionLength);
      setCharValue(tempCharValue);
      setCursorPos(position);
    }
  }
  
  /**
   * Установка содержимого.
   * @param charValue массив символов
   */
  protected void setCharValue(char[] charValue) {
    mask.applyLiterals(charValue);
    this.charValue = charValue;
    setText(mask.getText(charValue, true));
  }
}
