package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import java.util.Objects;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.uibinder.client.UiConstructor;
import com.technology.jep.jepria.client.message.JepMessageBoxImpl;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.masked.Mask;
import com.technology.jep.jepria.client.widget.field.masked.MaskedTextBox;
import com.technology.jep.jepria.client.widget.field.multistate.event.InputForbiddenEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.InputForbiddenEvent.InputForbiddenHandler;
import com.technology.jep.jepria.client.widget.field.multistate.event.PasteForbiddenEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.PasteForbiddenEvent.PasteForbiddenHandler;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class JepMaskedTextField extends JepBaseTextField<MaskedTextBox> {
  
  public JepMaskedTextField(Mask mask) {
    this(null, "", mask);
  }

  @UiConstructor
  public JepMaskedTextField(String fieldLabel, String mask) {
    this(null, fieldLabel, new Mask(mask));
  }
  
  public JepMaskedTextField(String fieldIdAsWebEl, String fieldLabel, String mask) {
    this(fieldIdAsWebEl, fieldLabel, new Mask(mask));
  }

  public JepMaskedTextField(String fieldIdAsWebEl, String fieldLabel, Mask mask) {
    super(fieldIdAsWebEl, fieldLabel);
    setMask(mask);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void addEditableCard() {
    editableCard = new MaskedTextBox(new Mask(""));
    editablePanel.add(editableCard);
    
    editableCard.addInputForbiddenHandler(new InputForbiddenHandler() {
      @Override
      public void onInputForbidden(InputForbiddenEvent event) {
        onInputForbiddenEvent(event);
      }
    });
    editableCard.addPasteForbiddenHandler(new PasteForbiddenHandler() {
      @Override
      public void onPasteForbidden(PasteForbiddenEvent event) {
        onPasteForbiddenEvent(event);
      }
    });
  }

  /**
   * Установка маски.<br>
   * Сбрасывается сообщение об ошибке.
   * @param mask маска
   */
  public void setMask(Mask mask) {
    clearInvalid();
    editableCard.setMask(mask);
  }

  /**
   * Установка маски в соответствии со строковым представлением.
   * @param mask строковое представление маски
   */
  public void setMask(String mask) {
    setMask(new Mask(mask));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    editableCard.clear();
    setViewValue(null);
  }

  /** Проверяет, содержит ли поле допустимое значение.<br>
   * Предварительно очищает сообщение об ошибке. Если поле является обязательным, а
   * пользователь ничего не ввёл, устанавливает сообщение об ошибке и возвращает false. 
   * Если введены не все обязательные символы, устанавливает сообщение об ошибке
   * и возвращает false.
   * @return true - если поле содержит допустимое значение, false - в противном случае
   */
  @Override
  public boolean isValid() {
    clearInvalid();
    if (!editableCard.isValid()) {
      markInvalid(JepClientUtil.substitute(JepTexts.maskedTextField_invalidText(), getRawValue()));
      return false;
    }
    if (!allowBlank && JepRiaUtil.isEmpty(getValue())) {
      markInvalid(JepTexts.field_blankText());
      return false;
    }
    return true;
  }

  /**
   * Данный метод не поддерживается.<br> 
   * Максимальная длина задаётся маской.
   * @param maxLength максимальная длина поля 
   */
  @Override
  public void setMaxLength(int maxLength) {
    throw new UnsupportedOperationException();
  }
  
  /**
   * Установка значения поля.<br>
   * Предварительно очищает сообщение об ошибке.
   * Если устанавливаемое значение не соответствует маске, сообщение об ошибке
   * выдаётся пользователю и записывается в лог. При этом в поле устанавливается 
   * пустое значение.
   * @param value значение поля
   */
  @Override
  public void setValue(Object value) {
    Object oldValue = getValue();
    if(!Objects.equals(oldValue, value)) {
      try {
        editableCard.setValue((String)value);
        clearInvalid();
        setViewValue(value);
      }
      catch (IllegalArgumentException exc) {
        editableCard.setValue(null);
        String errorMessage = JepClientUtil.substitute(JepTexts.maskedTextField_errorMessage(), value);
        Log.error(errorMessage);
        JepMessageBoxImpl.instance.showError(errorMessage);
      }
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public String getValue(){
    return editableCard.getValue();
  }

  /**
   * Hook-метод, определяющий поведение виджета в ситуации, когда
   * пользователь попытался ввести не разрешённый в данной позиции символ.<br>
   * Предназначен для переопределения в классах-наследниках.
   * @param event событие
   */
  protected void onInputForbiddenEvent(InputForbiddenEvent event) {
  }

  /**
   * Hook-метод, определяющий поведение виджета в ситуации, когда
   * пользователь попытался ввести не разрешённый в данной позиции символ.<br>
   * Предназначен для переопределения в классах-наследниках.
   * @param event событие
   */
  protected void onPasteForbiddenEvent(PasteForbiddenEvent event) {
  }

}
