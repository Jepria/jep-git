package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.widget.event.JepEventType.CHANGE_VALUE_EVENT;

import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.JepOptionField;
import com.technology.jep.jepria.client.widget.field.RadioListField;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class JepRadioField extends JepMultiStateField<RadioListField<JepOption>, HTML> implements JepOptionField {

	private final static String RADIO_FIELD_STYLE = "jepRia-RadioField-Input";
	
	public JepRadioField() {
		this("");
	}
	
	public JepRadioField(String fieldLabel) {
		super(fieldLabel);
	}
	
	public JepRadioField(String fieldId, String fieldLabel) {
		this(fieldLabel);
		this.getElement().setId(fieldId);
		this.getInputElement().setId(fieldId + "_INPUT"); // TODO Сделать, отталкиваяся от главного ID
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addEditableCard() {
		editableCard = new RadioListField<JepOption>();
		editablePanel.add(editableCard);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Object value) {
		JepOption oldValue = getValue();
		if(!JepRiaUtil.equalWithNull(oldValue, value)) {
			editableCard.setValue((JepOption) value);
			clearInvalid();
			setViewValue(value);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setViewValue(Object value) {
		viewCard.setHTML(value != null ? ((JepOption)value).getName() : null);
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
	 * Добавление прослушивателя для реализации прослушивания события
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_SELECTION_EVENT}.
	 */
	protected void addChangeValueListener() {
		editableCard.addValueChangeHandler(new ValueChangeHandler<JepOption>() {
			@Override
			public void onValueChange(ValueChangeEvent event) {
				notifyListeners(CHANGE_VALUE_EVENT, new JepEvent(JepRadioField.this, getValue()));
			}
		});
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
	@SuppressWarnings("unchecked")
	public JepOption getValue() {
		return editableCard.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOptions(List<JepOption> options) {
		editableCard.setOptions(options);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEmptyText(String emptyText){
		throw new UnsupportedOperationException("RadioField can't set empty text!");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRawValue(){
		throw new UnsupportedOperationException("RadioField can't get raw value!");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		// Перед проверкой, очищаем предыдущие ошибки.
		clearInvalid();
		if (!allowBlank && JepRiaUtil.isEmpty(getValue())){
			markInvalid(JepTexts.field_blankText());
			return false;
		}
		return true;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyStyle(){
		super.applyStyle();
		
		getInputElement().addClassName(RADIO_FIELD_STYLE);
		editableCard.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	}
}
