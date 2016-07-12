package com.technology.jep.jepria.client.widget.field.multistate;

import com.google.gwt.text.client.IntegerRenderer;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ValueBox;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода целых чисел.
 */
@SuppressWarnings("unchecked")
public class JepIntegerField extends JepBaseNumberField<ValueBox<? extends Number>> {
  
  public JepIntegerField() {
    this(null);
  }
  
  public JepIntegerField(String fieldLabel) {
    this(null, fieldLabel);
  }

    public JepIntegerField(String fieldIdAsWebEl, String fieldLabel) {
        super(fieldIdAsWebEl, fieldLabel);
    }
    
  /**
   * {@inheritDoc}
   */
  @Override
  protected void addEditableCard() {
    editableCard = new IntegerBox(){
      @Override
      public void setValue(Integer value) {
        super.setText(IntegerRenderer.instance().render(value).replaceAll(groupingSeparator, ""));
      }
    };
    editablePanel.add(editableCard);
    
    // Добавляем обработчик события "нажатия клавиши" для проверки ввода символов.
    initKeyPressHandler();
  }
  
  /**
   * {@inheritDoc}
   * 
   * Особенность:<br/>
   * Для данного поля запрещен ввод разделителя целой и дробной частей.
   */
  protected void initAllowedInputCharacters() {
    // Запрет ввод десятичного разделителя.
    this.allowDecimals = false;
    super.initAllowedInputCharacters();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(Object value) {
    Object newValue = value;
    if (!JepRiaUtil.isEmpty(newValue)){
      // Проверка важна для наследников, чтобы не изменить текущее значение.
      if (editableCard instanceof IntegerBox){
        newValue = Integer.valueOf(newValue.toString());
      }
    }
    super.setValue(newValue);
  }
}
