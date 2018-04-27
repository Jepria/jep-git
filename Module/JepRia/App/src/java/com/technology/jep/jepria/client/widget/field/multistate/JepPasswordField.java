package com.technology.jep.jepria.client.widget.field.multistate;

import com.google.gwt.user.client.ui.PasswordTextBox;

public class JepPasswordField extends JepBaseTextField<PasswordTextBox> {

  public JepPasswordField() {
    this(null);
  }
  
  public JepPasswordField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepPasswordField(String fieldIdAsWebEl, String fieldLabel) {
    super(fieldIdAsWebEl, fieldLabel);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void addEditableCard() {
    editableCard = new PasswordTextBox();
    editablePanel.add(editableCard);
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public String getValue() {
    return editableCard.getValue();
  }
}
