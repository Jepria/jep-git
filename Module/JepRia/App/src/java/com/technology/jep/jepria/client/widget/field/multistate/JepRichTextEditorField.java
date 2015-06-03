package com.technology.jep.jepria.client.widget.field.multistate;

import com.google.gwt.user.client.ui.HTML;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.widget.field.wysiwyg.RichTextEditorField;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле, позволяющее производить форматирование текста пользователем.
 */
public class JepRichTextEditorField extends JepMultiStateField<RichTextEditorField, HTML> {
	
	public JepRichTextEditorField() {
		this("");
	}
	
	public JepRichTextEditorField(String fieldLabel) {
		super(fieldLabel);
	}
	
	public JepRichTextEditorField(String fieldId, String fieldLabel) {
		this(fieldLabel);
		this.getElement().setId(fieldId);
		this.getInputElement().setId(fieldId + "_INPUT");
	}
	
	/**
	 * Метод добавляющий на панель Редактирование соответствующее поле (компонент) Gxt.<br/>
	 * Перегружается в наследниках для добавления соответсвующих наследникам полей Gxt.
	 */
	@Override
	protected void addEditableCard() {
		editableCard = new RichTextEditorField();
		editablePanel.add(editableCard);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public String getValue(){
		return editableCard.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Object value) {
		String oldValue = getValue();
		if(!JepRiaUtil.equalWithNull(oldValue, value)) {
			editableCard.setValue((String) value);
			clearInvalid();
			setViewValue(value);
		}
	}
	
	/**
	 * Установка значения для карты Просмотра.
	 *
	 * @param value значение для карты Просмотра
	 */	
	@Override
	protected void setViewValue(Object value) {
		((HTML) viewCard).setHTML((String) value);
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void clear(){
		clearView();
		setValue(null);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public String getRawValue(){
		return getValue();
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void setEmptyText(String emptyText){
		throw new UnsupportedOperationException("RichTextEditorField can't set empty text!");
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void setEnabled(boolean enabled) {
	}
	
	@Override
	protected void onChangeWorkstate(WorkstateEnum newWorkstate) {
		super.onChangeWorkstate(newWorkstate);
		editableCard.reset();
	}

}
