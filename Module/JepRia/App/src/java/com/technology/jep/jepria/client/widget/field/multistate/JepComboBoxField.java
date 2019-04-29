package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_HEIGHT;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;
import static com.technology.jep.jepria.client.widget.event.JepEventType.CHANGE_SELECTION_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.FIRST_TIME_USE_EVENT;
import static com.technology.jep.jepria.shared.field.option.JepOption.EMPTY_OPTION;

import java.util.*;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.technology.jep.jepria.client.JepRiaClientConstant;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.ComboBox;
import com.technology.jep.jepria.client.widget.field.JepOptionField;
import com.technology.jep.jepria.client.widget.field.multistate.event.AfterExpandEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.AfterExpandEvent.AfterExpandHandler;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeExpandEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeExpandEvent.BeforeExpandHandler;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeSelectEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeSelectEvent.BeforeSelectHandler;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода из выпадающего списка.<br/>
 *
 * Основные особенности работы с полем.<br/>
 * Поле в качестве своего значения принимает {@link com.technology.jep.jepria.client.widget.field.multistate.JepBaseTextField#setValue(Object value)} 
 * и возвращает {@link com.technology.jep.jepria.client.widget.field.multistate.JepBaseTextField#getValue()}
 * экземпляр класса {@link com.technology.jep.jepria.shared.field.option.JepOption} .<br/>
 * Таким образом, для работы с полем необходимо в наследниках {@link com.technology.jep.jepria.shared.record.JepRecordDefinition}
 * установить тип поля {@link com.technology.jep.jepria.shared.field.JepTypeEnum#OPTION}
 * и на серверной стороне в бине делать преобразование из/в {@link com.technology.jep.jepria.shared.field.option.JepOption} .<br/>
 * Подобный подход в работе со значением поля связан с оптимизацией производительности работы поля - 
 * предотвращении lookup'ов (переборе списка значений для получения отображаемого значения) на клиенте
 * и переносе операций на сервер.<br/>
 * <br/>
 * Концепция поддержки обработки событий отражена в описании пакета {@link com.technology.jep.jepria.client.widget}.
 * <dl>
 *   <dt>Поддерживаемые типы событий {@link com.technology.jep.jepria.client.widget.event.JepEvent}:</dt>
 *   <dd>{@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_SELECTION_EVENT CHANGE_SELECTION_EVENT}</dd>
 *   <dd>{@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT FIRST_TIME_USE_EVENT}</dd>
 * </dl>
 */
public class JepComboBoxField extends JepBaseTextField<ComboBox<JepOption>> implements JepOptionField {
  
  /**
   * Последняя для вывода в списке опция
   */
  protected JepOption lastOption = null;
  
  /**
   * Запрос, с которым выпадающий список раскрывается впервые
   */
  private String firstTimeUsedQuery = null;
  
  /**
   * Наименование класса стилей поля.
   */
  private static final String COMBOBOX_FIELD_INPUT_STYLE = JepRiaClientConstant.STYLE_PREFIX + "ComboBox-Input";
  
  public JepComboBoxField() {
    this(null);
  }
  
  public JepComboBoxField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepComboBoxField(String fieldIdAsWebEl, String fieldLabel) {
    super(fieldIdAsWebEl, fieldLabel);
    // Выставляем высоту компонента + 1px граница
    setFieldHeight(FIELD_DEFAULT_HEIGHT);
    loadEmptyOptionList();
  }
  
  @Override
  protected void setWebIds() {
    editableCard.setCompositeWebIds(fieldIdAsWebEl);
  }

  /**
   * Метод добавляющий на панель Редактирование соответствующее поле (компонент) Gxt.<br/>
   * Перегружается в наследниках для добавления соответсвующих наследникам полей Gxt.<br/>
   * Особенности:<br/>
   * По умолчанию, в Gxt для комбобокса определено событие срабатывания клавиши, вызывающее метод ComboBox.doQuery(String, boolean).<br/>
   * Во избежание совместной отработки функционала Gxt и определенного в JepRia при наличии слушателя события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT}, переопределяем метод 
   * ComboBox.onKeyUp(FieldEvent) при добавлении ComboBox'а на панель Редактирования.
   */
  @Override
  protected void addEditableCard() {
    editableCard = new ComboBox<JepOption>(fieldIdAsWebEl);
    editablePanel.add(editableCard);
  }

  /**
   * Установка значения поля.<br/>
   * Особеность реализации перегруженного метода:
   * <ul>
   *  <li>Если для поля определен слушатель события FIRST_TIME_USE_EVENT и само событие еще НЕ происходило, то выпадающий список 
   * заполняется одной единственной опцией - опцией, которая устанавливается как значение поля;</li>
   *  <li>В конце вызывается метод предка {@link com.technology.jep.jepria.client.widget.field.multistate.JepBaseTextField#setValue(Object value)} .</li>
   * </ul>
   *
   * @param value значение поля
   */
  @Override
  public void setValue(Object value) {
    JepOption oldValue = getValue();
    if(!Objects.equals(oldValue, value)) {
      editableCard.setValue((JepOption) value);
      clearInvalid();
      setViewValue(value);
    }
  }

  /**
   * Установка значения для карты Просмотра.<br/>
   * При перегрузке данного метода в наследниках необходимо обеспечить, чтобы данный метод был быстрым/НЕ ресурсо-затратным.<br/>
   * 
   * Основная идея Jep-полей: они должны быть легкими. Поэтому, карта Просмотра должна быть именно текстовым (или простым Html) представлением
   * значения карты Редактирования.<br/>
   * В тех случаях, когда чисто текстовое представление нецелесообразно (списки, деревья и т.п.) - в поле используется ТОЛЬКО одна карта - 
   * карта Редактирования (т.е. карта Просмотра - вообще НЕ используется).
   *
   * @param value значение для карты Просмотра
   */
  @Override
  protected void setViewValue(Object value) {
    viewCard.setHTML(value != null ? ((JepOption)value).getName() : null);
  }

  /**
   * Установка опций выпадающего списка (с пустой опцией).
   *
   * @param options опции выпадающего списка
   */
  @Override
  public void setOptions(List<JepOption> options) {
    setOptions(options, true);
  }
  
  /**
   * Установка опций выпадающего списка с возможностью добавления пустой опции.
   * 
   * @param options      список опций
   * @param hasEmptyChoice  флаг для вставки пустой опции
   */
  public void setOptions(List<JepOption> options, boolean hasEmptyChoice) {
    List<JepOption> unfilteredOptions = new ArrayList<JepOption>();
    // Если пустой выбор необходим, то вставляем пустую опцию в начало списка.
    if(hasEmptyChoice) {
      options.add(0, EMPTY_OPTION);
      unfilteredOptions.add(EMPTY_OPTION);
    }
    // Если последняя опция определена, то добавим ее в конец списка.
    if(lastOption != null) {
      options.add(lastOption);
      unfilteredOptions.add(lastOption);
    }
    editableCard.setUnfilteredOptions(unfilteredOptions);
    editableCard.setOptions(options);
    
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
      case CHANGE_SELECTION_EVENT:
        addChangeSelectionListner();
        break;  
      case FIRST_TIME_USE_EVENT:
        addFirstTimeUseListener();
        break;
      default:
        break;
    }
    
    super.addListener(eventType, listener);
  }
  
  /**
   * Добавление прослушивателя для реализации прослушивания события
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_SELECTION_EVENT}.
   */
  protected void addChangeSelectionListner() {
    editableCard.addValueChangeHandler(new ValueChangeHandler<JepOption>() {
      @Override
      public void onValueChange(ValueChangeEvent<JepOption> event) {
        notifyListeners(CHANGE_SELECTION_EVENT, new JepEvent(JepComboBoxField.this, getValue()));
      }
    });
  }
  
  /**
   * Добавление Gxt прослушивателей для реализации прослушивания события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT } .
   */
  protected void addFirstTimeUseListener() {
    editableCard.addBeforeExpandHandler(new BeforeExpandHandler() {
      @Override
      public void onBeforeExpand(BeforeExpandEvent event) {
        if(firstTimeUsedQuery == null && getListeners(FIRST_TIME_USE_EVENT).size() != 0) {
          clearInvalid();
          setLoadingImage(true);
          firstTimeUsedQuery = getRawValue();
          notifyListeners(FIRST_TIME_USE_EVENT, new JepEvent(JepComboBoxField.this, firstTimeUsedQuery));
          // Отменим дальнейшие действия по событию - сначала заполним выпадающий список, а только затем выполним запрос 
          // (см. afterFirstTimeUseEvent()).
          event.setCancelled(true);
        }
      }
    });
  }
  
  /**
   * Добавление прослушивателей для реализации прослушивания события 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.event.BeforeSelectEvent} с целью предотвращения выбора последней опции.
   */
  protected void addLastOptionSelectCancelListener() {
    // Добавим слушателя, если он еще не определен.
    if (!editableCard.hasBeforeSelectHandler()) {
      editableCard.addBeforeSelectHandler(new BeforeSelectHandler<JepOption>() {
        @Override
        public void onBeforeSelect(BeforeSelectEvent<JepOption> event) {
          if(event.getSelectedItem().equals(lastOption)) {
            event.setCancelled(true);
          }
        }
      });
    }
    
    if (!editableCard.hasAfterExpandHandler()) {
      editableCard.addAfterExpandHandler(new AfterExpandHandler() {
        @Override
        public void onAfterExpand(AfterExpandEvent event) {
          editableCard.applyStyleForOption(lastOption, "fontStyle", "italic");
        }
      });
    }
    
    
  }
  
  /**
   * Процедура, выполняемая по успешному завершению обработчика события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT } .
   */
  public void afterFirstTimeUseSuccess() {
    // Выпадающий список заполнен - теперь раскроем список с учетом фильтрации.
    editableCard.setExpanded(true);
    setLoadingImage(false);
  }
  
  /**
   * Процедура, выполняемая по НЕуспешному завершению обработчика события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT } .
   */
  public void afterFirstTimeUseFailure() {
    // Сбросим признак успешного заполнения списка - запрос выполняемый при первом обращении к полю.
    clearFirstTimeUsedQuery();
    setLoadingImage(false);
  }
  
  /**
   * Процедура, выполняемая по успешному завершению обработчика события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT } .
   */
  public void afterTypingTimeoutSuccess() {
    // Выпадающий список заполнен - теперь выполним запрос.
    editableCard.setExpanded(true);
    super.afterTypingTimeoutSuccess();
  }

  /**
   * {@inheritDoc} 
   */
  @Override
  protected void handlePaste(String value) {
    getValueBoxElement().setPropertyString("value", value);
    startTypingTimeout();
  }
  
  /**
   * {@inheritDoc} 
   */
  @Override
  protected void startTypingTimeout() {
    String rawValue = getRawValue();
    if(rawValue != null) {
      // Очистка списка опций если длина набранной строки меньше минимальной для фильтрации
      if(rawValue.length() < this.typingTimeoutMinTextSize) {
        loadEmptyOptionList();
      } else { 
        // Старт тайм-аута
        delayedTask.cancel();
        if (typingTimeout > 0) {
          delayedTask.schedule(typingTimeout);
        } else {
          delayedTask.run();
        }
      }
    }
  }
  
  /**
   * Получение наименования последней опции.
   * 
   * @return наименование последней опции
   */
  public String getLastOptionText() {
    // Берем из опции именно значение, т.к. именно в нем лежит "чистый" текст (без тегов <i/>).
    return JepOption.<String>getValue(lastOption);
  }
    
  /**
   * Установка наименования последней опции.
   * 
   * @param lastOptionText новое наименование последней опции
   */
  public void setLastOptionText(String lastOptionText) {
    // Если текст последней опции определен, то инициализируем ее.
    lastOption = !JepRiaUtil.isEmpty(lastOptionText) ? new JepOption(lastOptionText, null) : null;
    addLastOptionSelectCancelListener();
  }

  /**
   * Получение режима фильтрации.
   * 
   * @return режим фильтрации в виде {@link com.technology.jep.jepria.shared.field.JepLikeEnum} 
   */
  public JepLikeEnum getFilterMode() {
    return editableCard.getFilterMode();
  }
  
  /**
   * Установка режима фильтрации.
   * 
   * @param filterMode режим фильтрации
   */
  public void setFilterMode(JepLikeEnum filterMode) {
    editableCard.setFilterMode(filterMode);
  }
  
  /**
   * {@inheritDoc} 
   */
  @SuppressWarnings("unchecked")
  public JepOption getValue() {
    return editableCard.getValue();
  }
    
  /**
   * {@inheritDoc} 
   */
  public boolean isValid() {
    clearInvalid();
    JepOption value = getValue();
    // flag specifies that no one option is chosen
    boolean isNotChosen = JepRiaUtil.isEmpty(value);
    String rawValue = getRawValue();
    if (isNotChosen ? !JepRiaUtil.isEmpty(rawValue) : !value.getName().equals(rawValue)) {
      markInvalid(JepTexts.errors_tooltip_field_option_incorrectOption());
      return false;
    }
    // flag specifies that no one option is chosen or option value is null
    boolean isEmptyOptionOrNotChosen = isNotChosen || JepRiaUtil.isEmpty(JepOption.<Object>getValue(value));
    if (isEmptyOptionOrNotChosen && !allowBlank) {
      markInvalid(JepTexts.field_blankText());
      return false;
    }
    return true;
  }

  
  /**
   * Установка пустого списка опций JepOption
   */
  public void loadEmptyOptionList() {
    setOptions(new ArrayList<JepOption>());
  }
  
  /**
   * {@inheritDoc} 
   */
  @Override
  public void setMaxLength(int maxLength) {
    throw new UnsupportedOperationException("ComboBox can't limit maxLength!");
  }

  /**
   * {@inheritDoc} 
   */
  @Override
  public void setEnabled(boolean enabled) {
    editableCard.setEnabled(enabled);
  }
  
  /**
   * {@inheritDoc} 
   */
  @Override
  public void setEmptyText(String emptyText) {
    getValueBoxElement().setPropertyString("placeholder", emptyText);
  }
  
  /**
   * {@inheritDoc} 
   */
  @Override
  public String getRawValue() {
    return getValueBoxElement().getPropertyString("value");
  }
  
  /**
   * {@inheritDoc} 
   */
  @Override
  public void markInvalid(String error) {
    getValueBoxElement().getStyle().setBorderColor("#c30");
    super.markInvalid(error);
  }
  
  /**
   * {@inheritDoc} 
   */
  @Override
  public void clearInvalid() {
    getValueBoxElement().getStyle().clearBorderColor();
    super.clearInvalid();
  }
  
  protected Element getValueBoxElement() {
    return editableCard.getValueBoxBase().getElement();
  }
  
  /**
   * {@inheritDoc} 
   */
  @Override
  protected void applyStyle() {
    super.applyStyle();
    getValueBoxElement().addClassName(MAIN_FONT_STYLE);
    getValueBoxElement().addClassName(COMBOBOX_FIELD_INPUT_STYLE);
  }
  
  /**
   * Устанавливает опцию в JepComboBoxField, зная только значение.
   * @param value Значение.
   * @throws NullPointerException если параметр value - null.
   */
  public <T> void setOptionByValue(T value) {
    List<JepOption> options = getEditableCard().getOptions();
    
    JepOption match = null;
    for(JepOption option: options) {
      if(value.equals(option.getValue())) {
        match = option;
        break;
      }
    }
    
    if(match != null) setValue(match);
  }
  
  /**
   * Очищает запрос, с которым выпадающий список раскрывается впервые.
   */
  private void clearFirstTimeUsedQuery() {
    firstTimeUsedQuery = null;
  }
  
  /**
   * Сбрасывает поле. Очищает значение и запрос, с которым выпадающий список раскрывается впервые.
   */
  public void reset() {
    clear();
    clearFirstTimeUsedQuery();
  }
}
