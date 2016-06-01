package com.technology.jep.jepria.client.widget.field.multistate;

import com.google.gwt.user.client.ui.TextBox;

/**
 * Поле для ввода строки текста.
 */
public class JepTextField extends JepBaseTextField<TextBox> {

	public JepTextField() {
		this(null);
	}
	
	public JepTextField(String fieldLabel) {
		this(null, fieldLabel);
	}
	
	public JepTextField(String fieldId, String fieldLabel) {
		super(fieldId, fieldLabel);
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
