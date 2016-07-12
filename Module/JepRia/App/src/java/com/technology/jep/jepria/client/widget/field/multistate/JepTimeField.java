package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_WIDTH;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.SHORT_TIME_FORMAT_MASK;
import static com.technology.jep.jepria.shared.JepRiaConstant.SHORT_TIME_FORMAT;

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
import com.technology.jep.jepria.shared.time.JepTime;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода времени.<br>
 * Использует маску ввода.<br>
 * Для задания нестандартного (отличного от {@code HH:mm}) формата времени
 * необходимо в классе-наследнике переопределить методы {@code addEditableCard()}
 * и {@code getValue()} следующим образом (в примере задаётся формат {@code HH:mm:ss}):
 * <pre>
 *   JepTimeField timeField = new JepTimeField(&quot;timeField&quot;){
 *     {@literal @}Override
 *     protected void addEditableCard() {
 *       this.format = DateTimeFormat.getFormat(DEFAULT_TIME_FORMAT);
 *       Mask mask = new Mask(DEFAULT_TIME_FORMAT_MASK);
 *       editableCard = new MaskedDateBox(new DatePicker(), null, new XDefaultFormat(format), mask){
 *         {@literal @}Override
 *         public void showDatePicker() {
 *           Date current = parseDate(false);
 *           if (current == null) {
 *             current = new Date();
 *           }        
 *         }};
 *       editablePanel.add(editableCard);
 *     }
 *
 *     {@literal @}Override
 *     public JepTime getValue() {
 *       Date dateValue = this.<MaskedDateBox>getEditableCard().getValue();
 *       if (dateValue == null) {
 *         return null;
 *       }
 *       return new JepTime(dateValue);
 *     }};
 * </pre>
 */
public class JepTimeField extends JepMultiStateField<MaskedDateBox, HTML> {
  
  /**
   * Текущее значение поля, для проверки действительности изменения перед уведомлением слушателей
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_VALUE_EVENT CHANGE_VALUE_EVENT}.
  */
  private JepTime currentTime = null;
  
  /**
   * Формат представления времени.
   */
  protected DateTimeFormat format;
  
  public JepTimeField() {
    this("");
  }

  public JepTimeField(String fieldLabel) {
    super(fieldLabel);
    setFieldWidth(FIELD_DEFAULT_WIDTH / 2);
  }
  
  /**
   * Создание и добавление карты редактирования.<br>
   * Также задаёт формат времени. Если требуется изменить формат ввода и вывода времени
   * (например, с секундами), данный метод необходимо перегрузить в классе-наследнике.
   */
  @Override
  protected void addEditableCard() {
    this.format = DateTimeFormat.getFormat(SHORT_TIME_FORMAT);
    Mask mask = new Mask(SHORT_TIME_FORMAT_MASK);
    editableCard = new MaskedDateBox(new DatePicker(), null, new XDefaultFormat(format), mask){
      @Override
      public void showDatePicker() {
        /*
         * Необходимо вызвать метод parseDate(), т.к. он имеет побочный эффект
         * в виде валидации содержимого поля.
         */
        parseDate(false);
      }};
    editablePanel.add(editableCard);
  }

  @Override
  public void setValue(Object value) {
    JepTime oldValue = getValue();
    if(oldValue == null && value != null || oldValue != null && !oldValue.equals(value)) {
      editableCard.setValue(((JepTime) value).addDate(new Date()));
      clearInvalid();
      currentTime = (JepTime) value;
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
    viewCard.setHTML(value != null ? format.format(((JepTime) value).addDate(new Date())) : null);
  }

  /**
   * Возвращает значение поля.<br>
   * По умолчанию возвращает время без секунд. Если требуется время с секундами,
   * метод переопределяется в классах-наследниках.
   * @return значение поля
   */
  @Override
  public JepTime getValue() {
    Date dateValue = editableCard.getValue();
    if (dateValue == null) {
      return null;
    }
    return new JepTime(dateValue.getHours(), dateValue.getMinutes());
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
    }
    
    super.addListener(eventType, listener);
  }
  
  /**
   * Добавление прослушивателей для реализации прослушивания события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_VALUE_EVENT}.
   */
  protected void addChangeValueListener() {
    editableCard.addValueChangeHandler(
        new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                JepTime newTime = getValue();
            if (currentTime == null && newTime != null || currentTime != null && !currentTime.equals(newTime) ) {
              notifyListeners(JepEventType.CHANGE_VALUE_EVENT, new JepEvent(JepTimeField.this, newTime));
              currentTime = newTime;
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
      markInvalid(JepClientUtil.substitute(JepTexts.timeField_invalidText(), getRawValue()));
      return false;
    }
    if (!allowBlank && JepRiaUtil.isEmpty(getValue())) {
      markInvalid(JepTexts.field_blankText());
      return false;
    }
    return true;
  }

}
