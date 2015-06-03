package com.technology.jep.jepria.client.widget.field.multistate;

import com.google.gwt.text.client.DoubleRenderer;
import com.google.gwt.user.client.ui.DoubleBox;

/**
 * Поле для ввода десятичных чисел.
 */
@SuppressWarnings("unchecked")
public class JepNumberField extends JepBaseNumberField<DoubleBox> {
	
	public JepNumberField(){
		this("");
	}
	
	public JepNumberField(String fieldLabel){
		super(fieldLabel);
	}
	
	public JepNumberField(String fieldId, String fieldLabel) {
		this(fieldLabel);
		this.getElement().setId(fieldId);
		this.getInputElement().setId(fieldId + "_INPUT");
	}

	/**
	 * Метод добавляющий на панель Редактирование соответствующее поле (компонент).<br/>
	 * Перегружается в наследниках для добавления соответсвующих наследникам полей.
	 * 
	 * Особенности поля редактирования:
	 * При вводе осуществляется проверка является ли символ допустимым для ввода, а именно цифровым или точкой.
	 */
	@Override
	protected void addEditableCard() {
		editableCard = new DoubleBox(){
			@Override
			public void setValue(Double value) {
				super.setText(DoubleRenderer.instance().render(value).replaceAll(groupingSeparator, ""));
			}
		};
		editablePanel.add(editableCard);
		
		// Добавляем обработчик события "нажатия клавиши" для проверки ввода символов.
		initKeyPressHandler();
	}
}
