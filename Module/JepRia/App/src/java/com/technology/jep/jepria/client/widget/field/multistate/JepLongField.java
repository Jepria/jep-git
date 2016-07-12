package com.technology.jep.jepria.client.widget.field.multistate;

import com.google.gwt.text.client.LongRenderer;
import com.google.gwt.user.client.ui.LongBox;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода чисел большой разрядности.
 */
public class JepLongField extends JepIntegerField {

  public JepLongField() {
    this(null);
  }
  
  public JepLongField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepLongField(String fieldIdAsWebEl, String fieldLabel) {
    super(fieldIdAsWebEl, fieldLabel);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void addEditableCard() {
    editableCard = new LongBox(){
      @Override
      public void setValue(Long value) {
        super.setText(LongRenderer.instance().render(value).replaceAll(groupingSeparator, ""));
      }
    };
    editablePanel.add(editableCard);
    
    // Добавляем обработчик события "нажатия клавиши" для проверки ввода символов.
    initKeyPressHandler();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(Object value) {
    Object newValue = value;
    if (!JepRiaUtil.isEmpty(newValue)){
      // Проверка важна для наследников, чтобы не изменить текущее значение.
      if (editableCard instanceof LongBox){
        newValue = Long.valueOf(newValue.toString());
      }
    }
    super.setValue(newValue);
  }
}
