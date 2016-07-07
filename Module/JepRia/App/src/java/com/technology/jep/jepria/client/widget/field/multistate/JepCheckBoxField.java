package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.widget.event.JepEventType.CHANGE_CHECK_EVENT;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.technology.jep.jepria.client.AutomationConstant;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для установки логического значения (выбрано/не выбрано, отмечено/не отмечено).<br/>
 * <br/>
 * Концепция поддержки обработки событий отражена в описании пакета {@link com.technology.jep.jepria.client.widget}.
 * <dl>
 *   <dt>Поддерживаемые типы событий {@link com.technology.jep.jepria.client.widget.event.JepEvent}:</dt>
 *   <dd>{@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_CHECK_EVENT CHANGE_CHECK_EVENT}</dd>
 * </dl>
 */
public class JepCheckBoxField extends JepMultiStateField<CheckBox, HTML> {

	/**
	 * Наименование селектора (класса стилей) чекбокса.
	 */
	private static final String CHECK_BOX_FIELD_STYLE = "jepRia-CheckBox-Input";
	
	private Element inputCheck;
	
	public JepCheckBoxField() {
		this(null);
	}
	
	public JepCheckBoxField(String fieldLabel) {
		this(null, fieldLabel);
	}
	
	public JepCheckBoxField(String fieldIdAsWebEl, String fieldLabel) {
		super(fieldIdAsWebEl, fieldLabel);
	}
	
	/**
	 * The inner class aimed to provide access to the protected CheckBox constructor. 
	 * @author RomanovAS
	 */
	private class CheckBox2 extends CheckBox {
		protected CheckBox2(Element elem) {
			super(elem);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addEditableCard() {
		inputCheck = DOM.createInputCheck();
		editableCard = new CheckBox2(inputCheck);
		editablePanel.add(editableCard);
	}
	
	@Override
	protected void setWebIds() {
		inputCheck.setId(fieldIdAsWebEl + AutomationConstant.JEP_FIELD_INPUT_POSTFIX);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Object value) {
		Object oldValue = getValue();
		if(!JepRiaUtil.equalWithNull(oldValue, value)) {
			editableCard.setValue(value != null && ((Boolean) value).booleanValue());
			setViewValue(value);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Boolean getValue() {
		return editableCard.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean enabled) {
		editableCard.setEnabled(enabled);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setViewValue(Object value) {
		super.setViewValue(JepRiaUtil.isEmpty(value) ? "" : (getValue() ? JepTexts.yes() : JepTexts.no()));
	}
	
	/**
	 * Очищает значение поля.<br/>
	 * При очистке значения поля, очищаются значения для обеих карт сразу: для карты Редактирования и карты Просмотра.
	 */
	@Override
	public void clear() {
		super.clear();
		editableCard.setValue(null);
	}
	
	/**
	 * Добавление слушателя определенного типа собитий.<br/>
	 * Концепция поддержки обработки событий и пример реализации метода отражен в описании пакета {@link com.technology.jep.jepria.client.widget}.
	 *
	 * @param eventType тип события
	 * @param listener слушатель
	 */
	@Override
	public void addListener(JepEventType eventType, JepListener listener) {
		switch(eventType) {
			case CHANGE_CHECK_EVENT:
				addChangeCheckListener();
				break;
		}
		super.addListener(eventType, listener);
	}
	
	/**
	 * Добавление прослушивателей для реализации прослушивания события 
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_CHECK_EVENT }.
	 */
	protected void addChangeCheckListener() {
		editableCard.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				notifyListeners(CHANGE_CHECK_EVENT, new JepEvent(JepCheckBoxField.this, getValue()));
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Особенности перегруженного метода:<br>
	 * Для чекбоксов выставление признака обязательности является нелогичным.
	 */
	@Override
	public void setAllowBlank(boolean allowBlank) {
		throw new UnsupportedOperationException("CheckBox can't be mandatory field!");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void applyStyle(){
		super.applyStyle();
		// Сделаем отображение элемента блочным.
		getInputElement().getStyle().setDisplay(Display.BLOCK);
		// Удалим умолчательные отступы чекбокса.
		for (int i = 0; i < getInputElement().getChildCount(); i++){
			Node child = getInputElement().getChild(i);
			// Найдем соответствующий input-элемент.
			if (child instanceof InputElement){
				InputElement childElement = (InputElement) child;
				childElement.addClassName(CHECK_BOX_FIELD_STYLE);
				removeMarginsAndPaddings(childElement);
				break;
			}
		}
	}
}
