package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_HEIGHT;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.widget.field.DualListBox.BUTTON_PANEL_WIDTH;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.DualListBox;
import com.technology.jep.jepria.client.widget.field.JepOptionField;
import com.technology.jep.jepria.client.widget.field.multistate.JepMultiStateField;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле, состоящее из двух списков.<br>
 * Левый список содержит доступные опции, правый - выбранные. Перенос между
 * списками осуществляется либо с помощью кнопок между списками, либо двойным кликом
 * по элементу списка.
 */
public class JepDualListField extends JepMultiStateField<DualListBox, HTML> implements JepOptionField {	
	
	public JepDualListField() {
		this("");
	}

	public JepDualListField(String fieldLabel) {
		super(fieldLabel);
		setFieldHeight(5 * (FIELD_DEFAULT_HEIGHT + 2));
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
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		super.clear();
		setValue(new ArrayList<JepOption>());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JepOption> getValue() {
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
	public void setOptions(List<JepOption> options) {
		editableCard.setOptions(options);		
	}

	/**
	 * Задаёт высоту компонента редактирования.<br>
	 * Компонент просмотра данный метод не затрагивает.
	 * @param fieldHeight высота компонента редактирования
	 */
	@Override
	public void setFieldHeight(int fieldHeight) {
		editableCard.setHeight(fieldHeight + Unit.PX.getType());
	}

	/**
	 * Задаёт ширину поля.<br>
	 * @param fieldWidth ширина
	 */
	@Override
	public void setFieldWidth(int fieldWidth) {
		viewCard.setWidth(fieldWidth + Unit.PX.getType());
		editableCard.setListBoxWidth((fieldWidth - BUTTON_PANEL_WIDTH) / 2 + Unit.PX.getType());
	}

	/**
	 * Установка или скрытие изображения загрузки (справа от карты редактирования).<br>
	 * Метод переопределён в связи с особенностями компонента редактирования
	 * данного поля.
	 * @param imageVisible показать/скрыть изображение загрузки
	 */
	@Override
	public void setLoadingImage(boolean imageVisible) {
		if (loadingIcon == null) {
			loadingIcon = new Image(JepImages.loading());
			loadingIcon.addStyleName(FIELD_INDICATOR_STYLE);
			
		} 
		if (!loadingIcon.isAttached()) {
			editableCard.add(loadingIcon);
		}
		loadingIcon.setTitle(imageVisible ? JepTexts.loadingPanel_dataLoading() : "");
		loadingIcon.setAltText(imageVisible ? JepTexts.loadingPanel_dataLoading() : "");
		loadingIcon.setVisible(imageVisible);
	}
	
	/**
	 * Установка сообщения об ошибке.<br>
	 * Метод переопределён в связи с особенностями компонента редактирования
	 * данного поля.
	 * @param error сообщение обшибке
	 */
	@Override
	public void markInvalid(String error) {
		if (errorIcon == null) {
			errorIcon = new Image(JepImages.field_invalid());
			errorIcon.addStyleName(FIELD_INDICATOR_STYLE);
		} 
		if (!errorIcon.isAttached()) {
			editableCard.add(errorIcon);
		}
		errorIcon.setTitle(error);
		errorIcon.setAltText(error);
		errorIcon.setVisible(true);
	}
	
	/**
	 * Очистка сообщения об ошибке.<br>
	 * Метод переопределён в связи с особенностями компонента редактирования
	 * данного поля.
	 */
	public void clearInvalid(){
		if (errorIcon != null) {
			errorIcon.setTitle("");
			errorIcon.setAltText("");
			errorIcon.setVisible(false);			
		}
	}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public boolean isValid() {
		clearInvalid();
		if (!allowBlank && JepRiaUtil.isEmpty(editableCard.getValue())){
			markInvalid(JepTexts.field_blankText());
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Object value) {
		editableCard.setValue(value != null ? (List<JepOption>) value : new ArrayList<JepOption>());
		setViewValue(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addEditableCard() {
		editableCard = new DualListBox();
		editablePanel.add(editableCard);
	}

	/**
	 * Задаёт текст на компоненте просмотра.<br>
	 * По умолчанию устанавливается список имён опций через запятую и пробел.
	 * @param value значение
	 */
	@Override
	protected void setViewValue(Object value) {
		super.setViewValue(value != null ? JepOption.getOptionNamesAsString((List<JepOption>) value) : "");
	}

	/**
	 * Установка слушателя события {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_SELECTION_EVENT}.
	 */
	private void addChangeSelectionListener() {
		editableCard.addValueChangeHandler(
				new ValueChangeHandler<List<JepOption>>() {
					public void onValueChange(ValueChangeEvent<List<JepOption>> event) {
						notifyListeners(JepEventType.CHANGE_SELECTION_EVENT, new JepEvent(JepDualListField.this, getValue()));
					}
				}
			);
	}

}
