package com.technology.jep.jepria.client.widget.field.multistate;

import com.google.gwt.user.client.ui.TextBox;

/**
 * Поле для ввода строки текста.
 */
public class JepTextField extends JepBaseTextField<TextBox> {

	public JepTextField() {
		this("");
	}
	
	public JepTextField(String fieldLabel) {
		super(fieldLabel);
	}
	
	public JepTextField(String fieldId, String fieldLabel) {
		this(fieldLabel);
		this.getElement().setId(fieldId);
		this.getInputElement().setId(fieldId + "_INPUT");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addEditableCard() {
		editableCard = new TextBox();
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
