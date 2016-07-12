package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_DATE_FORMAT_MASK;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.masked.Mask;
import com.technology.jep.jepria.client.widget.field.masked.MaskedDateBox;
import com.technology.jep.jepria.client.widget.field.masked.MaskedDateBox.XDefaultFormat;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода даты.<br>
 * Использует маску ввода.<br>
 * Для задания нестандартного (отличного от {@code dd.MM.yyyy}) формата ввода и вывода
 * даты необходимо в классе-наследнике переопределить метод {@code addEditableCard()}
 * следующим образом (задаётся формат {@code dd.MM.yyyy HH:mm:ss}):
 * <pre>
 *   JepDateField dateField = new JepDateField(&quot;Date field&quot;){
 *     {@literal @}Override
 *     protected void addEditableCard() {
 *       format = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT + &quot; &quot; + DEFAULT_TIME_FORMAT);
 *       Mask mask = new Mask(DEFAULT_DATE_FORMAT_MASK + &quot; &quot; + DEFAULT_TIME_FORMAT_MASK);
 *       editableCard = new MaskedDateBox(new DatePicker(), null, new XDefaultFormat(format), mask);
 *       editablePanel.add(editableCard);
 *     }};
 * </pre>
 */
public class JepDateField extends JepMultiStateField<MaskedDateBox, HTML> {
  
  /**
   * Текущее значение поля, для проверки действительности изменения перед уведомлением слушателей
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_VALUE_EVENT CHANGE_VALUE_EVENT}.
  */
  private Date currentDate = null;
  
  /**
   * Формат представления даты.<br>
   * Влияет на отображение даты на карте просмотра и на сравнение дат.
   */
  protected DateTimeFormat format;

  /**
   * Создаёт поле для ввода даты с пустой меткой в стандартном формате (dd.MM.yyyy).
   */
  public JepDateField() {
    this(null);
  }
  
  /**
   * Создаёт поле для ввода даты в стандартном формате (dd.MM.yyyy).
   * @param fieldLabel метка
   */
  public JepDateField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepDateField(String fieldIdAsWebEl, String fieldLabel) {
    super(fieldIdAsWebEl, fieldLabel);
  }

  /**
   * Создание и добавление карты редактирования.<br>
   * Также задаёт формат даты. Если требуется изменить формат ввода и вывода даты
   * (например, с учётом времени), данный метод необходимо перегрузить в классе-наследнике.
   */
  @Override
  protected void addEditableCard() {
    this.format = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);
    Mask mask = new Mask(DEFAULT_DATE_FORMAT_MASK);
    editableCard = new MaskedDateBox(new DatePicker(), null, new XDefaultFormat(format), mask);
    editablePanel.add(editableCard);
  }

  /**
   * Установка значения поля.<br/>
   * @param value значение поля
   */
  @Override
  public void setValue(Object value) {
    Date oldValue = getValue();
    if(!equalsWithFormat(oldValue, (Date)value)) {
      editableCard.setValue((Date) value);
      clearInvalid();
      currentDate = (Date) value;
      setViewValue(value);
    }
  }
  
  /**
   * {@inheritDoc}
   */
  public void setEnabled(boolean enabled) {
    getInputElement().setPropertyBoolean("disabled", !enabled);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    super.clear();
    editableCard.setValue(null);
  }
  
  /**
   * Установка значения карты Просмотра.<br>
   * Значение форматируется в соответствии с заданным форматом ввода даты.
   * @param value значение
   */
  @Override
  protected void setViewValue(Object value) {
    viewCard.setHTML(value != null ? format.format((Date) value) : null);
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Date getValue() {
    return editableCard.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addListener(JepEventType eventType, JepListener listener) {
    switch(eventType) {
      case CHANGE_VALUE_EVENT:
        addChangeValueListener();
        break;
        
      default:;
    }
    
    super.addListener(eventType, listener);
  }
  
  /**
   * Добавление прослушивателей для реализации прослушивания события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_VALUE_EVENT}.
   */
  protected void addChangeValueListener() {
    editableCard.getDatePicker().addValueChangeHandler(
        new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                Date newDate = getValue();
            if (!equalsWithFormat(currentDate, newDate)) {
              notifyListeners(JepEventType.CHANGE_VALUE_EVENT, new JepEvent(JepDateField.this, newDate));
              currentDate = newDate;
            }
            }
        }
      );
    editableCard.addValueChangeHandler(
        new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                Date newDate = getValue();
            if (!equalsWithFormat(currentDate, newDate)) {
              notifyListeners(JepEventType.CHANGE_VALUE_EVENT, new JepEvent(JepDateField.this, newDate));
              currentDate = newDate;
            }
            }
        }
      );
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid() {
    clearInvalid();
    if (!editableCard.isValid()) {
      markInvalid(JepClientUtil.substitute(JepTexts.dateField_invalidText(), getRawValue()));
      return false;
    }
    if (!allowBlank && JepRiaUtil.isEmpty(getValue())) {
      markInvalid(JepTexts.field_blankText());
      return false;
    }
    return true;
  }
  
  /**
   * Сравнивает две даты с учётом формата их представления.<br>
   * Возвращает true, если оба значения null либо оба не null и совпадают,
   * будучи представленными в формате поля.
   * @param date1 первое сравниваемое значение
   * @param date2 второе сравниваемое значение
   * @return результат сравнения
   */
  private boolean equalsWithFormat(Date date1, Date date2) {
    return date1 == date2 || (date1 != null && date2 != null && format.format(date1).equals(format.format(date2)));
  }
}
