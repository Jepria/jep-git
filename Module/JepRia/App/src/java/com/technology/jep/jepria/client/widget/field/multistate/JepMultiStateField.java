package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_HEIGHT;
import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_WIDTH;
import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_INVALID_COLOR;
import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_LABEL_DEFAULT_WIDTH;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;
import static com.technology.jep.jepria.client.JepRiaClientConstant.REQUIRED_MARKER;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.AutomationConstant;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.event.JepObservable;
import com.technology.jep.jepria.client.widget.event.JepObservableImpl;
import com.technology.jep.jepria.client.widget.field.JepField;
import com.technology.jep.jepria.client.widget.field.validation.Validator;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Базовый класс полей, поддерживающих несколько рабочих состояний.<br/>
 * В общем случае для использования поля необходимо выполнить следующие шаги:<br/>
 * <code>
 * <br/>
 *   // Создать экземпляр управляющего полями класса.<br/>
 *   FieldManager fieldManager = new FieldManager();<br/>
 *   // Создать экземпляр поля (например: поля ввода даты).<br/>
 *   JepDateField dateField = new JepDateField(formText.work_beginDate());<br/>
 *   // Разместить поле в родительском контейнере.<br/>
 *   getFormPanel().add(dateField);<br/>
 *   // Зарегистрировать поле в управляющем классе.<br/>
 *   fieldManager.put(BEGIN_DATE, dateField);<br/>
 * <br/>
 * </code>
 * Для стандартных детальных форм, все эти шаги могут быть &quot;обернуты&quot; в реализации view детальной формы.<br/>
 * <br/>
 * Иерархия полей поддерживается аналогичной (по возможности) иерархии соответствующих полей Gwt.<br/>
 * <br/>
 * Концепция поддержки обработки событий отражена в описании пакета {@link com.technology.jep.jepria.client.widget}.
 */
public abstract class JepMultiStateField<E extends Widget, V extends Widget> extends DeckPanel implements JepField<E, V>, JepMultiState, JepObservable, Validator {
	
	/**
	 * Карта для режима Просмотра.<br/> 
	 * Режим просмотра определяется методом {@link com.technology.jep.jepria.client.ui.WorkstateEnum#isViewState(WorkstateEnum workstate)}.<br/>
	 * <br/>
	 * Основная идея Jep-полей: они должны быть легкими. Поэтому, карта Просмотра должна быть именно текстовым (или простым Html) представлением
	 * значения карты Редактирования.<br/>
	 * В тех случаях, когда чисто текстовое представление нецелесообразно (списки, деревья и т.п.) - в поле используется ТОЛЬКО одна карта - 
	 * карта Редактирования (т.е. карта Просмотра - вообще НЕ используется).
	 */
	protected V viewCard;
	
	protected HTML viewCardLabel, editableCardLabel;
	
	/**
	 * Панель, на которой располагается карта режима Просмотра.<br/>
	 * Панель необходима для применения требуемого layout'а для компонента карты Просмотра.<br/>
	 */
	protected HorizontalPanel viewPanel;

	/**
	 * Карта для режима Редактирование.<br/> 
	 * Режим редактирования определяется методом {@link com.technology.jep.jepria.client.ui.WorkstateEnum#isEditableState(WorkstateEnum workstate)}
	 */
	protected E editableCard;
	
	/**
	 * Панель, на которой располагается карта режима Редактирование.<br/>
	 * Панель необходима для применения требуемого layout'а для компонента карты Редактирование.<br/>
	 */
	protected HorizontalPanel editablePanel;
	
	/**
	 * Текущее состояние поля.
	 */
	protected WorkstateEnum _workstate;
	
	/**
	 * Признак возможности отображения карты Редактирования поля.
	 */
	protected boolean editable = true;
	
	/**
	 * Признак доступности пустого значения поля.
	 */
	protected boolean allowBlank = true;
	
	/**
	 * Признак невалидности поля.
	 */
	private boolean markedInvalid = false;

	/**
	 * Объект для работы со слушателями событий поля.
	 */
	protected JepObservable observable;
	
	/**
	 * Индикатор загрузки данных.
	 */
	protected Image loadingIcon;

	/**
	 * Ошибка при валидации данных.
	 */
	protected Image errorIcon;
	
	/**
	 * Метка поля.
	 */
	private String fieldLabel;
	
	/**
	 * Разделитель для метки поля (по умолчанию, двоеточие). 
	 */
	private String labelSeparator = ":";
	
	/**
	 * Наименование селектора (класса стилей) текстовой области ввода.
	 */
	public static final String FIELD_INDICATOR_STYLE = "jepRia-Field-Icon";
	
	/**
	 * Наименование селектора (класса стилей) меток карт редактирования и просмотра.
	 */
	public static final String LABEL_FIELD_STYLE = "jepRia-MultiStateField-Label";
	
	/**
	 * Наименование селектора (класса стилей) для карты редактирования.
	 */
	public static final String EDITABLE_CARD_STYLE = "jepRia-MultiStateField-EditableCard";
	
	/**
	 * Наименование селектора (класса стилей) для карты просмотра.
	 */
	public static final String VIEW_CARD_STYLE = "jepRia-MultiStateField-ViewCard";
	
	/**
	 * Наименование атрибута выравнивания DOM-элемента.
	 */
	public static final String ALIGN_ATTRIBUTE_NAME = "align";
	
	@Deprecated
	public JepMultiStateField() {
		this(null);
	}
	
	@Deprecated
	public JepMultiStateField(String fieldLabel) {
		this(null, fieldLabel);
	}
	
	public JepMultiStateField(String fieldId, String fieldLabel) {
		// Корректировка параметров
		fieldLabel = (fieldLabel != null) ? fieldLabel : "";
		// Если ID поля явно не задано, указываем в качестве ID его подпись + набор цифр
		fieldId = (fieldId != null) ? fieldId : (fieldLabel + "_" + System.currentTimeMillis());
		
		viewCardLabel = new HTML();
		viewCardLabel.getElement().addClassName(MAIN_FONT_STYLE);
		editableCardLabel = new HTML();
		editableCardLabel.getElement().addClassName(MAIN_FONT_STYLE);
		
		viewPanel = new HorizontalPanel();
		viewPanel.add(viewCardLabel);
		// Получим ссылку на родительский td-элемент
		Element tdLabel = viewCardLabel.getElement().getParentElement();
		// По умолчанию, в горизонтальной панели добавление элемента сопровождается
		// выставлением атрибута выравнивания. Более правильно проводить стилизацию CSS-преобразованием
		tdLabel.removeAttribute(ALIGN_ATTRIBUTE_NAME);
		tdLabel.addClassName(LABEL_FIELD_STYLE);
		add(viewPanel);
		
		editablePanel = new HorizontalPanel();
		editablePanel.add(editableCardLabel);
		// Получим ссылку на родительский td-элемент
		tdLabel = editableCardLabel.getElement().getParentElement();
		// По умолчанию, в горизонтальной панели добавление элемента сопровождается
		// выставлением атрибута выравнивания. Более правильно проводить стилизацию CSS-преобразованием
		tdLabel.removeAttribute(ALIGN_ATTRIBUTE_NAME);
		tdLabel.addClassName(LABEL_FIELD_STYLE);
		add(editablePanel);
		// Если у добавляемого виджета не задана ширина, DeckPanel выставит 100%.
		// Нам это не нужно, т.к. приводит к смещению вправо индикаторов загрузки и некорректного значения.
		editablePanel.getElement().getStyle().clearWidth();

		observable = new JepObservableImpl();
		// Добавляем карту просмотра.
		addViewCard();
		// Получим ссылку на родительский td-элемент
		Element tdField = getViewCard().getElement().getParentElement();
		// По умолчанию, в горизонтальной панели добавление элемента сопровождается
		// выставлением атрибута выравнивания. Более правильно проводить стилизацию CSS-преобразованием
		tdField.removeAttribute(ALIGN_ATTRIBUTE_NAME);
		tdField.addClassName(VIEW_CARD_STYLE);
		// Добавляем карту редактирования.
		addEditableCard();
		// Получим ссылку на родительский td-элемент
		tdField = getEditableCard().getElement().getParentElement();
		tdField.removeAttribute(ALIGN_ATTRIBUTE_NAME);
		tdField.addClassName(EDITABLE_CARD_STYLE);

		// Устанавливаем значения лейблов поля.
		setFieldLabel(fieldLabel);
		
		// Установка размеров карт просмотра и редактирования.
		setFieldWidth(FIELD_DEFAULT_WIDTH);
		setFieldHeight(FIELD_DEFAULT_HEIGHT);
		
		// Установка ширин лейблов и их разделителей.
		setLabelWidth(FIELD_LABEL_DEFAULT_WIDTH);
		
		// Применение стилей к полю.
		applyStyle();
		
		changeWorkstate(SEARCH);
		
		
		// Установка ID самого поля как Web-элемента и его Input-элемента
		this.getElement().setId(fieldId);
		setInnerIds(fieldId);
	}
	
	/**
	 * Установка ID внутренних компонентов Jep-поля.
	 * @param baseFieldId ID Jep-поля, который берется за основу ID внутренних компонентов
	 */
	protected void setInnerIds(String baseFieldId) {
		this.getInputElement().setId(baseFieldId + AutomationConstant.FIELD_INPUT_POSTFIX);
	}
	
	/**
	 * Метод добавляющий на панель Просмотра соответствующее поле (компонент) Gwt.<br/>
	 * Перегружается в наследниках, в случае, если карта просмотра отлична от LabelField.
	 */
	@SuppressWarnings("unchecked")
	protected void addViewCard() {
		viewCard = (V) new HTML();
		viewPanel.add(viewCard);
	}
	
	/**
	 * Получение карты Просмотра.
	 */
	public V getViewCard() {
		return viewCard;
	}
	
	/**
	 * Метод, добавляющий на панель Редактирования соответствующее поле (компонент) Gwt.<br/>
	 */
	protected abstract void addEditableCard();
	
	/**
	 * Получение карты Редактирования (с возможностью преобразования к указанному виджету).
	 */
	public E getEditableCard() {
		return editableCard;
	}
	
	/**
	 * Установка наименования поля и сброс разделителя метки, если оно пустое.
	 * 
	 * @param fieldLab наименование поля
	 */
	public void setFieldLabel(String fieldLab) {
		if (JepRiaUtil.isEmpty(fieldLab)){
			setLabelSeparator("");
		}
		
		this.fieldLabel = fieldLab;
		
		this.viewCardLabel.setHTML(fieldLab + this.labelSeparator);
		this.editableCardLabel.setHTML((this.allowBlank ? "" : REQUIRED_MARKER) + fieldLab + this.labelSeparator);
	}
	
	/**
	 * Получение наименования поля.
	 *
	 * @return наименование поля
	 */
	public String getFieldLabel() {
		return this.fieldLabel;
	}

	/**
	 * Установка нового состояния
	 * 
	 * @param newWorkstate новое состояние
	 */
	public void changeWorkstate(WorkstateEnum newWorkstate) {
		// Только в случае, если действительно изменяется состояние.
		if(newWorkstate != null && !newWorkstate.equals(_workstate)) {
			onChangeWorkstate(newWorkstate);
			_workstate = newWorkstate;
		}
	}
	
	/**
	 * Обработчик нового состояния
	 * 
	 * @param newWorkstate новое состояние
	 */
	protected void onChangeWorkstate(WorkstateEnum newWorkstate) {
		// При смене состояния, очищаем ошибки валидации, если таковые присутствуют.
		clearInvalid();
		
		// Если произошло переключение из режима Редактирования в режим Просмотра, то обновим значение карты режима Просмотра.
		if(WorkstateEnum.isEditableState(_workstate) && WorkstateEnum.isViewState(newWorkstate)) {
			setViewValue(getValue());
		}
		// Если выставлен признак нередактируемости в режиме Редактирования, то переключаем карту в режим Просмотра. 
		if(WorkstateEnum.isEditableState(newWorkstate) && editable) {
			showWidget(getWidgetIndex(editablePanel));
		} else {
			showWidget(getWidgetIndex(viewPanel));
		}
	}

	/**
	 * Установка значения для карты Просмотра.<br/>
	 * При перегрузке данного метода в наследниках необходимо обеспечить, чтобы данный метод был быстрым/НЕ ресурсо-затратным.<br/>
	 * 
	 * Основная идея Jep-полей: они должны быть легкими. Поэтому, карта Просмотра должна быть именно текстовым (или простым Html) представлением
	 * значения карты Редактирования.<br/>
	 * В тех случаях, когда чисто текстовое представление нецелесообразно (списки, деревья и т.п.) - в поле используется ТОЛЬКО одна карта - 
	 * карта Редактирования (т.е. карта Просмотра - вообще НЕ используется).
	 * 
	 * Для строковых значений производится кодирование специальных символов в html-эквивалент для корректного отображения на странице.
	 *  
	 * @param value значение для карты Просмотра
	 */
	protected void setViewValue(Object value) {
		((HTML) viewCard).setHTML(value instanceof String ? SafeHtmlUtils.htmlEscape((String) value) : (value != null ? value.toString() : null));
	}
	
	/**
	 * Очистка значения для карты Просмотра.<br/>
	 * При перегрузке данного метода в наследниках необходимо обеспечить, чтобы данный метод был быстрым/НЕ ресурсо-затратным.<br/>
	 * 
	 * Основная идея Jep-полей: они должны быть легкими. Поэтому, карта Просмотра должна быть именно текстовым (или простым Html) представлением
	 * значения карты Редактирования.<br/>
	 * В тех случаях, когда чисто текстовое представление нецелесообразно (списки, деревья и т.п.) - в поле используется ТОЛЬКО одна карта - 
	 * карта Редактирования (т.е. карта Просмотра - вообще НЕ используется).
	 */
	protected void clearView() {
		setViewValue(null);
	}
	
	/**
	 * Установка ширины наименования поля.
	 * 
	 * @param labelWidth ширина наименования поля
	 */
	public void setLabelWidth(int labelWidth) {
		viewPanel.setCellWidth(viewCardLabel, labelWidth + Unit.PX.getType());
		editablePanel.setCellWidth(editableCardLabel, labelWidth + Unit.PX.getType());
	}
	
	/**
	 * Установка ширины компонента редактирования поля.
	 * 
	 * @param fieldWidth ширина компонента редактирования поля
	 */
	public void setFieldWidth(int fieldWidth) {
		viewCard.setWidth(fieldWidth + Unit.PX.getType());
		editableCard.setWidth(fieldWidth + Unit.PX.getType());
	}
	
	/**
	 * Установка высоты поля.<br>
	 * По умолчанию методы выставляет высоту как для компонента редактирования,
	 * так и для компонента просмотра. Если необходимо другое поведение,
	 * данный метод переопределяется в классе-наследнике.
	 * @param fieldHeight высота
	 */
	public void setFieldHeight(int fieldHeight) {
		String height = fieldHeight + Unit.PX.getType();
		// Инициализируем высоту карты редактирования.
		editableCard.setHeight(height);
		// Инициализируем высоту карты просмотра.
		viewPanel.setCellHeight(viewCardLabel, height);
		viewPanel.setCellHeight(viewCard, height);
	}
		
	/**
	 * Добавление слушателя определенного типа собитий.<br/>
	 * Реализуется вызовом метода {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#addListener(JepEventType eventType, JepListener listener)}
	 * объекта {@link #observable}.
	 *
	 * @param eventType тип события
	 * @param listener слушатель
	 */
	public void addListener(JepEventType eventType, JepListener listener) {
		observable.addListener(eventType, listener);
	}

	/**
	 * Удаление слушателя определенного типа собитий.<br/>
	 * Реализуется вызовом метода {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#removeListener(JepEventType eventType, JepListener listener)}
	 * объекта {@link #observable}.
	 *
	 * @param eventType тип события
	 * @param listener слушатель
	 */
	public void removeListener(JepEventType eventType, JepListener listener) {
		observable.removeListener(eventType, listener);
	}
	
	/**
	 * Уведомление слушателей определенного типа о событии.<br/>
	 * Реализуется вызовом метода {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#notifyListeners(JepEventType eventType, JepEvent event)}
	 * объекта {@link #observable}.
	 *
	 * @param eventType тип события
	 * @param event событие
	 */
	public void notifyListeners(JepEventType eventType, JepEvent event) {
		observable.notifyListeners(eventType, event);
	}

	/**
	 * Получение списка слушателей определенного типа собитий.<br/>
	 * Реализуется вызовом метода {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#getListeners(JepEventType eventType)}
	 * объекта {@link #observable}.
	 *
	 * @param eventType тип события
	 *
	 * @return список слушателей
	 */
	public List<JepListener> getListeners(JepEventType eventType) {
		return observable.getListeners(eventType);
	}

	/**
	 * Установка доступности или недоступности (карты Редактирования) поля для редактирования.
	 * 
	 * @param enabled true - поле доступно для редактирования, false - поле не доступно для редактирования
	 */
	public abstract void setEnabled(boolean enabled);

	/**
	 * Установка возможности отображения карты Редактирования поля.
	 * 
	 * @param editable true - карта Редактирования поля отображается (обычный режим), false - всегда отображается только карта Просмотра поля
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
		
		if(WorkstateEnum.isEditableState(_workstate) && editable) {
			showWidget(getWidgetIndex(editablePanel));
		} else {
			showWidget(getWidgetIndex(viewPanel));
		}
	}
	/**
	 * Установка или скрытие изображения загрузки (справа от карты редактирования).
	 * 
	 * @param imageVisible показать/скрыть изображение загрузки
	 */
	public void setLoadingImage(boolean imageVisible) {
		if (loadingIcon == null) {
			loadingIcon = new Image(JepImages.loading());
			loadingIcon.addStyleName(FIELD_INDICATOR_STYLE);
			
		} 
		if (!loadingIcon.isAttached()) {
			editablePanel.add(loadingIcon);
		}
		loadingIcon.setTitle(imageVisible ? JepTexts.loadingPanel_dataLoading() : "");
		loadingIcon.setAltText(imageVisible ? JepTexts.loadingPanel_dataLoading() : "");
		loadingIcon.setVisible(imageVisible);
	}
	
	/**
	 * Установка сообщения об ошибке.
	 * 
	 * @param error текст сообщения об ошибке
	 */
	public void markInvalid(String error) {
		if (errorIcon == null) {
			errorIcon = new Image(JepImages.field_invalid());
			errorIcon.addStyleName(FIELD_INDICATOR_STYLE);
		} 
		if (!errorIcon.isAttached()) {
			editablePanel.add(errorIcon);
		}
		errorIcon.setTitle(error);
		errorIcon.setAltText(error);
		errorIcon.setVisible(true);
		
		markedInvalid = true;
		
		getInputElement().getStyle().setBorderColor(FIELD_INVALID_COLOR);
	}
	
	/**
	 * Очистка сообщения об ошибке.
	 */
	public void clearInvalid(){
		// Проверяем: было ли поле действительно невалидно.
		if (!markedInvalid) return;
		
		if (errorIcon != null) {
			errorIcon.setTitle("");
			errorIcon.setAltText("");
			errorIcon.setVisible(false);
			
		}
		
		markedInvalid = false;
		
		getInputElement().getStyle().clearBorderColor();
	}

	/**
	 * Определяет: является ли пустое значение допустимым значением поля.
	 * 
	 * @param allowBlank true - допускает пустое значение поля, false - поле обязательное для заполнения.
	 */
	@Override
	public void setAllowBlank(boolean allowBlank) {
		this.allowBlank = allowBlank;
		setFieldLabel(getFieldLabel());
	}

	/**
	 * Установка значения разделителя. По умолчанию, символ двоеточия.
	 * 
	 * @param labelSeparator	новый разделитель	
	 */
	public void setLabelSeparator(String labelSeparator) {
		this.labelSeparator = labelSeparator;
	}
	
	/**
	 * Получение значения компонента из DOM-дерева документа.
	 * 
	 * @return возвращаемое значение
	 */
	public String getRawValue(){
		return getInputElement().getPropertyString("value");
	}
	
	/**
	 * Проверяет, содержит ли поле допустимое значение.
	 * <br>
	 * Предварительно очищает сообщение об ошибке. Если поле является обязательным, а
	 * введённое значение пусто, устанавливает сообщение об ошибке и возвращает false. 
	 * Предназначен для переопределения в классах-наследниках.
	 *
	 * @return true - если поле содержит допустимое значение, false - в противном случае
	 */
	@Override
	public boolean isValid() {
		// Перед проверкой, очищаем предыдущие ошибки.
		clearInvalid();
		if (!allowBlank && JepRiaUtil.isEmpty(getRawValue())){
			markInvalid(JepTexts.field_blankText());
			return false;
		}
		return true;
		
	}
	
	/*
	 * TODO Метод имеет смысл не для всех полей, а лишь для тех, в основе которых лежит
	 * один элемент типа input. Для JepListField, JepDualListField, JepTreeField и т.д.
	 * это, очевидно, не так. Следует подумать над переносом данного метода и рефакторингом
	 * использующих его методов.
	 */
	
	/**
	 * Получение DOM-элемента карты редактирования.
	 * 
	 * @return DOM-элемент
	 */
	protected Element getInputElement(){
		return editableCard.getElement();
	}

	/**
	 * Очищает значение поля.<br/>
	 * При очистке значения поля, очищаются значения для обеих карт сразу: для карты Редактирования и карты Просмотра.
	 * 
	 * Особенность:<br/>
	 * Перекрытие метода обусловлено вызывом метода {@link com.google.gwt.user.client.ui.Panel#clear}, удаляюшяего все элементы с панели.
	 */
	public void clear(){
		clearView();
	}
	
	/**
	 * Метод для стилизации поля.<br/>
	 * Для применения новых стилей элемента необходимо в наследниках перекрывать данный метод.
	 */
	protected void applyStyle(){
		// Устанавливаем атрибуты по умолчанию для компонента JepMultiStateField.
		getElement().getStyle().setMarginBottom(5, Unit.PX);
		// Установка основного шрифта в карту просмотра и редактирования.
		viewCard.getElement().addClassName(MAIN_FONT_STYLE);
		getInputElement().addClassName(MAIN_FONT_STYLE);
		// Удаляем выступы и отступы карты редактирования.
		removeMarginsAndPaddings(getInputElement());
	}

	/**
	 * Удаление отступов и выступов элемента.
	 * 
	 * @param stylezedElement	стилизуемый элемент
	 */
	protected static void removeMarginsAndPaddings(Element stylezedElement) {
		Style style = stylezedElement.getStyle();
		style.setMargin(0, Unit.PX);
		style.setPadding(0, Unit.PX);
	}
}
