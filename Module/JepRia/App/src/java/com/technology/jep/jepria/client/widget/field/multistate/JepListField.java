package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_HEIGHT;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.widget.event.JepEventType.CHANGE_SELECTION_EVENT;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.CheckBoxListField;
import com.technology.jep.jepria.client.widget.field.JepOptionField;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле, разрешающее выбор одновременно нескольких опций (множественный выбор).
 */
public class JepListField extends JepMultiStateField<CheckBoxListField<JepOption>, HTML> implements JepOptionField {
	
	/**
	 * Наименование селектора (класса стилей) элемента с пустым текстом.
	 */
	public static final String LIST_FIELD_EMPTYTEXT_STYLE = "jepRia-ListField-emptyText";
	
	public JepListField() {
		this(null);
	}
	
	public JepListField(String fieldLabel){
		this(null, fieldLabel);
	}
	
	public JepListField(String fieldIdAsWebEl, String fieldLabel) {
        super(fieldIdAsWebEl, fieldLabel);
        
        // Установка высоты карты редактирования, по умолчанию видны 5 опций.
 		// Высота каждой опции складывается из обычной высоты и по 1px границы сверху и снизу + 1.5px верхний отступ.
 		setFieldHeight(5 * (FIELD_DEFAULT_HEIGHT + 3.5));
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addEditableCard() {
		editableCard = new CheckBoxListField<JepOption>(fieldIdAsWebEl);
		editablePanel.add(editableCard);
	}
	
	@Override
	protected void setWebIds() {
		editableCard.setCompositeWebIds(fieldIdAsWebEl);
	}
	
	/**
	 * Установка значения поля.
	 * @param value значение поля - список вида List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		Object oldValue = getValue();
		if(!JepRiaUtil.equalWithNull(oldValue, value)) {
			editableCard.setSelection((List<JepOption>) value);
			setViewValue(value);
		}
	}
	
	/**
	 * Установка значения для карты просмотра.<br/>
	 * Особенность: на входе имеем список опций, 
	 * который преобразуется к соответствующему списку их наименований, перечисленных через запятую
	 * 
	 * @param value список опций
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void setViewValue(Object value) {
		viewCard.setHTML(JepOption.getOptionNamesAsString((List<JepOption>) value));	
	}
	
	/**
	 * Очищает значение поля.<br/>
	 * При очистке значения поля, очищаются значения для обеих карт сразу: для карты Редактирования и карты Просмотра.
	 */
	@Override
	public void clear() {
		super.clear();
		editableCard.setSelection(null);
	}

	/**
	 * Получение значения поля.<br/>
	 * Значение берется, как значение карты Редактирования.
	 *
	 * @return значение поля - список вида List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<JepOption> getValue() {
		return editableCard.getSelection();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		// Перед проверкой очищаем предыдущие ошибки.
		clearInvalid();
		if(!allowBlank) {
			if(getValue().size() == 0) {
				markInvalid(JepTexts.checkForm_mandatoryField());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Установка опций JepListField.
	 *
	 * @param options список опций
	 */
	@Override
	public void setOptions(List<JepOption> options) {
		editableCard.setData(options);
	}
	
	/**
	 * Метод не поддерживается данным полем.
	 */
	@Override
	public String getRawValue(){
		throw new UnsupportedOperationException("ListField does not have a raw value.");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addListener(JepEventType eventType, JepListener listener) {
	
		switch(eventType) {
			case CHANGE_SELECTION_EVENT:
				addChangeSelectionListener();
				break;
		}
		
		super.addListener(eventType, listener);
	}
	
	/**
	 * Добавление листенеров для перехвата события 
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_SELECTION_EVENT } .
	 */
	protected void addChangeSelectionListener() {
		editableCard.addValueChangeHandler(new ValueChangeHandler<JepOption>() {
			@Override
			public void onValueChange(ValueChangeEvent<JepOption> event) {
				notifyListeners(CHANGE_SELECTION_EVENT, new JepEvent(JepListField.this, getValue()));
			}
		});
		
	}
	
	// TODO Есть ощущение, что данный метод не работает. Необходимо разобраться.
	
	/**
	 * Установка текста по умолчанию для пустого (незаполненного значением) поля.
	 * 
	 * @param emptyText пустой текст
	 */
	public void setEmptyText(String emptyText){
		HTML emptyTextWidget = new HTML(emptyText);
		emptyTextWidget.setStyleName(LIST_FIELD_EMPTYTEXT_STYLE);
		editableCard.replaceWidget(emptyTextWidget);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFieldHeight(int fieldHeight) {
		setFieldHeight(fieldHeight + .0);
	}
	
	public void setFieldHeight(double fieldHeight){
		editableCard.setHeight(fieldHeight + Unit.PX.getType());
	}

	/**
	 * Установка доступности или недоступности (карты Редактирования) поля для редактирования.<br>
	 * Метод переопределён, т.к. компонент редактирования {@link CheckBoxListField}
	 * представляет собой составной виджет.
	 * @param enabled true - поле доступно для редактирования, false - поле не доступно для редактирования
	 */
	@Override
	public void setEnabled(boolean enabled) {
		editableCard.setEnabled(enabled);
	}
	
	/**
	 * Установка видимости флага "Выделить все".<br>
	 * По умолчанию флаг невидим.
	 * @param visible если true, то показать, в противном случае - скрыть
	 */
	public void setSelectAllCheckBoxVisible(boolean visible) {
		editableCard.setSelectAllCheckBoxVisible(visible);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Особенности:<br/>
	 * Обновляем данные в {@link JepMultiStateField#editableCard} после показа виджета, если он редактируем.
	 */
	@Override
	public void setVisible(boolean visible){
		super.setVisible(visible);
		if (visible && editable){
			editableCard.refreshData();
		}
	}
}
