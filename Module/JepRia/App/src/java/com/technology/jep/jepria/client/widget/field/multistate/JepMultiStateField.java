package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_FIELD_ALLOW_BLANK_POSTFIX;
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.util.JepClientUtil;
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
public abstract class JepMultiStateField<E extends Widget, V extends Widget> extends Composite implements JepField<E, V>, JepMultiState, JepObservable, Validator {

  /**
   * Основная панель виджета
   */
  protected DeckPanel mainPanel;

  /**
   * Панель, на которой располагается карта режима Просмотра.<br/>
   * Панель необходима для применения требуемого layout'а для компонента карты
   * Просмотра.<br/>
   */
  @UiField
  protected FlowPanel viewPanel;
  
  public FlowPanel getViewPanel() {
        return viewPanel;
    }

  @UiField
  protected HTML viewCardLabel;

  /**
   * Карта для режима Просмотра.<br/>
   * Режим просмотра определяется методом {@link com.technology.jep.jepria.client.ui.WorkstateEnum#isViewState(WorkstateEnum workstate)}.<br/>
   * <br/>
   * Основная идея Jep-полей: они должны быть легкими. Поэтому, карта Просмотра должна быть именно текстовым (или простым Html) представлением
   * значения карты Редактирования.<br/>
   * В тех случаях, когда чисто текстовое представление нецелесообразно (списки, деревья и т.п.) - в поле используется ТОЛЬКО одна карта - 
   * карта Редактирования (т.е. карта Просмотра - вообще НЕ используется).
   */
  @UiField
  protected V viewCard;

  /**
   * Панель, на которой располагается карта режима Редактирование.<br/>
   * Панель необходима для применения требуемого layout'а для компонента карты Редактирование.<br/>
   */
  @UiField
  protected FlowPanel editablePanel;
  
  public FlowPanel getEditablePanel() {
    return editablePanel;
  }

  @UiField
  protected HTML editableCardLabel;

  /**
   * Карта для режима Редактирование.<br/>
   * Режим редактирования определяется методом{@link com.technology.jep.jepria.client.ui.WorkstateEnum#isEditableState(WorkstateEnum workstate)}
   */
  protected E editableCard;

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
   * ID данного Jep-поля как Web-элемента.
   */
  protected String fieldIdAsWebEl;

  /**
   * Разделитель для метки поля (по умолчанию, двоеточие).
   */
  private String labelSeparator = ":";

  /**
   * Наименование селектора (класса стилей) текстовой области ввода.
   */
  public static final String FIELD_INDICATOR_STYLE = "jepRia-Field-Icon";
  
  /**
   * Автоматический расчет высоты поля и лайбла в зависимости от наполнения определяется в стиле - мин размер 2-px
   */
  public static final String FIELD_AUTO_HEIGTH_STYLE = "jepRia-AutoHeight";

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
   * Наименование селектора (класса стилей) для панели поля вертикальной ориентации.
   */
  public static final String PANEL_ORIENTATION_VERTICAL_STYLE = "jepRia-vPanelStyle";

  /**
   * Наименование селектора (класса стилей) для панели поля горизонтальной ориентации.
   */
  public static final String PANEL_ORIENTATION_HORIZONTAL_STYLE = "jepRia-hPanelStyle";
  
  /**
   * Наименование атрибута выравнивания DOM-элемента.
   */
  public static final String ALIGN_ATTRIBUTE_NAME = "align";

  protected JepMultiStateFieldLayoutUiBinder uiBinder = GWT.create(JepMultiStateFieldLayoutUiBinder.class);

  @SuppressWarnings("rawtypes")
  @UiTemplate("JepMultiStateField.ui.xml")
  interface JepMultiStateFieldLayoutUiBinder extends UiBinder<DeckPanel, JepMultiStateField> {}
  
  protected DeckPanel getMainWidget() {
    return uiBinder.createAndBindUi(this);
  }
  
  public HTML getEditableCardLabel() {
    return editableCardLabel;
  }
  
  public HTML getViewCardLabel() {
    return viewCardLabel;
  }
  
  @Deprecated
  public JepMultiStateField() {
    this(null);
  }
  
  @Deprecated
  public JepMultiStateField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepMultiStateField(String fieldIdAsWebEl, String fieldLabel) {
    initWidget(mainPanel = getMainWidget());
    // Корректировка параметров
    fieldLabel = (fieldLabel != null) ? fieldLabel : "";

    // Если fieldIdAsWebEl не пустое, то необходимо инициализировать в
    // начале конструктора,
    // чтобы корректное значение передалось в карты
    // редактирования/просмотра.
    this.fieldIdAsWebEl = fieldIdAsWebEl;

    // Если у добавляемого виджета не задана ширина, DeckPanel выставит 100%.
    // Нам это не нужно, т.к. приводит к смещению вправо индикаторов
    // загрузки и некорректного значения.
    editablePanel.getElement().getStyle().clearWidth();
    observable = new JepObservableImpl();
    
    // Добавляем карту просмотра.
    addViewCard();
    
    Element fieldViewCard = getViewCard().getElement();
        
    // Инициализируем карту редактирования.
    addEditableCard();
    
    Element fieldEditableCard = getEditableCard().getElement();
    
    fieldViewCard.addClassName(VIEW_CARD_STYLE);
    fieldEditableCard.addClassName(EDITABLE_CARD_STYLE);

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

    // Установка web-ID поля
    if (this.fieldIdAsWebEl != null) {
      setWebId(this.fieldIdAsWebEl);
    }

    // Установка атрибутов карт
    setCardWebAttrs();
  }

  /**
   * Получает web-ID поля.
   * 
   * @return web-ID поля.
   */
  public String getWebId() {
    return fieldIdAsWebEl;
  }

  /**
   * Установка web-ID поля
   * 
   * @param fieldIdAsWebEl web-ID поля
   */
  public void setWebId(String fieldIdAsWebEl) {
    this.fieldIdAsWebEl = fieldIdAsWebEl;
    this.getElement().setId(fieldIdAsWebEl);
    // Установка web-ID других элементов поля
    setWebIds();
  }

  /**
   * Установка web-ID двух карт данного Jep-поля.
   */
  protected void setCardWebAttrs() {
    editableCard.getElement().setAttribute(JepRiaAutomationConstant.JEP_CARD_TYPE_HTML_ATTR,JepRiaAutomationConstant.JEP_CARD_TYPE_VALUE_EDTB);
    viewCard.getElement().setAttribute(JepRiaAutomationConstant.JEP_CARD_TYPE_HTML_ATTR, JepRiaAutomationConstant.JEP_CARD_TYPE_VALUE_VIEW);
  }

  /**
   * Установка web-ID внутренних компонентов, специфичных для конкретного Jep-поля. 
   * Метод предназначен для перекрытия потомками. За основу назначаемых идентификаторов следует брать значение поля
   * this.fieldIdAsWebEl .
   */
  protected void setWebIds() {
    this.getInputElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_FIELD_INPUT_POSTFIX);
  }

  /**
   * Получение карты Просмотра.
   */
  public V getViewCard() {
    return (V) viewCard;
  }

  /**
   * Метод, добавляющий на панель Просмотра соответствующее поле (компонент) Gwt.<br/>
   */
  protected void addViewCard() {}
  
  /**
   * Метод, добавляющий на панель Редактирования соответствующее поле (компонент) Gwt.<br/>
   */
  protected abstract void addEditableCard();

  /**
   * Получение карты Редактирования (с возможностью преобразования к
   * указанному виджету).
   */
  public E getEditableCard() {
    return editableCard;
  }

  /**
   * Установка наименования поля и сброс разделителя метки, если оно пустое.
   * 
   * @param label наименование поля
   */
  public void setFieldLabel(String label) {

    this.fieldLabel = label;
    
    if (JepRiaUtil.isEmpty(label)) {
      setLabelSeparator("");
    } else {
      label = label + this.labelSeparator;
    }
    
    this.viewCardLabel.setHTML(label);
    
    if (this.allowBlank) {
      this.editableCardLabel.setHTML(label);

    } else if (!JepRiaUtil.isEmpty(label)) {
      final String idAttr = fieldIdAsWebEl == null ? "" : ("id='" + fieldIdAsWebEl + JEP_FIELD_ALLOW_BLANK_POSTFIX + "'");

      this.editableCardLabel.setHTML(JepClientUtil.substitute(REQUIRED_MARKER, idAttr) + label);
    }
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
    if (newWorkstate != null && !newWorkstate.equals(_workstate)) {
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
    // При смене состояния, очищаем ошибки валидации, если таковые
    // присутствуют.

    clearInvalid();

    // Если произошло переключение из режима Редактирования в режим
    // Просмотра, то обновим значение карты режима Просмотра.

    if (WorkstateEnum.isEditableState(_workstate) && WorkstateEnum.isViewState(newWorkstate)) {
      setViewValue(getValue());
    }
    // Если выставлен признак нередактируемости в режиме Редактирования, то
    // переключаем карту в режим Просмотра.

    if (WorkstateEnum.isEditableState(newWorkstate) && editable) {
      showWidget(getWidgetIndex(editablePanel));
    } else {
      showWidget(getWidgetIndex(viewPanel));
    }
  }

  /**
   * Установка значения для карты Просмотра.<br/>
   * При перегрузке данного метода в наследниках необходимо обеспечить, чтобы
   * данный метод был быстрым/НЕ ресурсо-затратным.<br/>
   * 
   * Основная идея Jep-полей: они должны быть легкими. Поэтому, карта
   * Просмотра должна быть именно текстовым (или простым Html) представлением
   * значения карты Редактирования.<br/>
   * В тех случаях, когда чисто текстовое представление нецелесообразно
   * (списки, деревья и т.п.) - в поле используется ТОЛЬКО одна карта - карта
   * Редактирования (т.е. карта Просмотра - вообще НЕ используется).
   * 
   * Для строковых значений производится кодирование специальных символов в
   * html-эквивалент для корректного отображения на странице.
   * 
   * @param value значение для карты Просмотра
   */
  protected void setViewValue(Object value) {
    ((HTML) viewCard).setHTML(value instanceof String ? SafeHtmlUtils.htmlEscape((String) value) : (value != null ? value.toString() : null));
  }

  /**
   * Очистка значения для карты Просмотра.<br/>
   * При перегрузке данного метода в наследниках необходимо обеспечить, чтобы
   * данный метод был быстрым/НЕ ресурсо-затратным.<br/>
   * 
   * Основная идея Jep-полей: они должны быть легкими. Поэтому, карта
   * Просмотра должна быть именно текстовым (или простым Html) представлением
   * значения карты Редактирования.<br/>
   * В тех случаях, когда чисто текстовое представление нецелесообразно
   * (списки, деревья и т.п.) - в поле используется ТОЛЬКО одна карта - карта
   * Редактирования (т.е. карта Просмотра - вообще НЕ используется).
   */
  protected void clearView() {
    setViewValue(null);
  }

  /**
   * Установка ширины наименования поля (в пикселях).
   * 
   * @param width ширина наименования поля (в пикселях). 
   */
  public void setLabelWidth(int width) {
    setLabelWidth(width + Unit.PX.getType());
  }
  
  /**
   * Установка ширины наименования поля.
   * 
   * @param width ширина наименования поля.
   */
  public void setLabelWidth(String width) {
    viewCardLabel.setWidth(width);
    editableCardLabel.setWidth(width);
  }
  
  /**
   * Установка ширины компонента редактирования поля (в пикселях).
   * 
   * @param width ширина компонента редактирования поля (в пикселях).
   */
  public void setFieldWidth(int width) {
    setFieldWidth(width + Unit.PX.getType());
  }

  /**
   * Установка ширины компонента редактирования поля.
   * 
   * @param width ширина компонента редактирования поля
   */
  public void setFieldWidth(String width) {
    viewCard.setWidth(width);
    editableCard.setWidth(width);
  }
  
  /**
   * Установка высоты поля.<br>
   * По умолчанию методы выставляет высоту как для компонента редактирования,
   * так и для компонента просмотра. Если необходимо другое поведение, данный
   * метод переопределяется в классе-наследнике.
   * 
   * @param fieldHeight высота
   */
  public void setFieldHeight(int fieldHeight) {
    String height = "" + fieldHeight + Unit.PX;
    if (fieldHeight == FIELD_DEFAULT_HEIGHT) {
      setFieldAutoHeight(height);
    } else {
      // Инициализируем высоту карты редактирования.
      editableCard.setHeight(height);
      // Инициализируем высоту карты просмотра.
      viewCardLabel.setHeight(height);
      viewCard.setHeight(height);
    }
  }
  
  public void setFieldAutoHeight(String height) {
    // Инициализируем высоту карты редактирования.
    editableCard.setHeight(height);
    // Инициализируем высоту карты просмотра.
    viewCardLabel.getElement().addClassName(FIELD_AUTO_HEIGTH_STYLE);
    viewCard.getElement().addClassName(FIELD_AUTO_HEIGTH_STYLE);
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
   * Реализуется вызовом метода
   * {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#removeListener(JepEventType eventType, JepListener listener)}
   * объекта {@link #observable}.
   *
   * @param eventType тип события
   * @param listener  слушатель
   */
  public void removeListener(JepEventType eventType, JepListener listener) {
    observable.removeListener(eventType, listener);
  }

  /**
   * Уведомление слушателей определенного типа о событии.<br/>
   * Реализуется вызовом метода
   * {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#notifyListeners(JepEventType eventType, JepEvent event)}
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
   * Реализуется вызовом метода
   * {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#getListeners(JepEventType eventType)}
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
   * Установка доступности или недоступности (карты Редактирования) поля для
   * редактирования.
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

    if (WorkstateEnum.isEditableState(_workstate) && editable) {
      showWidget(getWidgetIndex(editablePanel));
    } else {
      showWidget(getWidgetIndex(viewPanel));
    }
  }
  
  public void showWidget(int index) {
    mainPanel.showWidget(index);
  }
  
  public int getWidgetIndex(Widget child) {
    return mainPanel.getWidgetIndex(child);
  }

  Widget loadingWidget = null;
  /**
   * Установка или скрытие изображения загрузки (справа от карты
   * редактирования).
   * 
   * @param imageVisible  показать/скрыть изображение загрузки
   */
  public void setLoadingImage(boolean imageVisible) {
    if (loadingIcon == null) {
      loadingIcon = new Image(JepImages.loading());
    }
    if (loadingWidget == null) {
      loadingWidget = wrapSpan(loadingIcon);
      loadingWidget.addStyleName(FIELD_INDICATOR_STYLE);
      editablePanel.add(loadingWidget);
    }
    loadingIcon.setTitle(imageVisible ? JepTexts.loadingPanel_dataLoading() : "");
    loadingIcon.setAltText(imageVisible ? JepTexts.loadingPanel_dataLoading() : "");
    loadingIcon.setVisible(imageVisible);
  }
  
  
  /**
   * Обертка виджета в DIV.
   * @param widget Виджет.
   * @return Виджет.
   */
  public Widget wrapDiv(Widget widget) {
    HTMLPanel div = new HTMLPanel("");
    div.add(widget);
    return div;
  }

  /**
   * Обертка в SPAN
   */
  public Widget wrapSpan(Widget widget) {
    InlineHTML spanBlock = new InlineHTML();
    spanBlock.getElement().appendChild(widget.getElement());
    return spanBlock;
  }

    
  Widget widgetIcon = null;
  /**
   * Установка сообщения об ошибке.
   * 
   * @param error  текст сообщения об ошибке
   */
  public void markInvalid(String error) {
    if (errorIcon == null) {
      errorIcon = new Image(JepImages.field_invalid());
    }
    if (widgetIcon == null) {
      widgetIcon = wrapSpan(errorIcon);
      widgetIcon.addStyleName(FIELD_INDICATOR_STYLE);
      editablePanel.add(widgetIcon);
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
  public void clearInvalid() {
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
   * Определяет, является ли пустое значение допустимым значением поля.
   * 
   * @param allowBlank true - допускает пустое значение поля, false - поле обязательное для заполнения.
   */
  public void setAllowBlank(boolean allowBlank) {
    this.allowBlank = allowBlank;
    setFieldLabel(getFieldLabel());
  }

  /**
   * Установка значения разделителя. По умолчанию, символ двоеточия.
   * 
   * @param labelSeparator новый разделитель
   */
  public void setLabelSeparator(String labelSeparator) {
    this.labelSeparator = labelSeparator;
  }

  /**
   * Получение значения компонента из DOM-дерева документа.
   * 
   * @return возвращаемое значение
   */
  public String getRawValue() {
    return getInputElement().getPropertyString("value");
  }

  /**
   * Проверяет, содержит ли поле допустимое значение. <br>
   * Предварительно очищает сообщение об ошибке. Если поле является
   * обязательным, а введённое значение пусто, устанавливает сообщение об
   * ошибке и возвращает false. Предназначен для переопределения в
   * классах-наследниках.
   *
   * @return true - если поле содержит допустимое значение, false - в  противном случае
   */
  @Override
  public boolean isValid() {
    // Перед проверкой, очищаем предыдущие ошибки.
    clearInvalid();
    if (!allowBlank && JepRiaUtil.isEmpty(getRawValue())) {
      markInvalid(JepTexts.field_blankText());
      return false;
    }
    return true;

  }

  /*
   * TODO Метод имеет смысл не для всех полей, а лишь для тех, в основе
   * которых лежит один элемент типа input. Для JepListField,
   * JepDualListField, JepTreeField и т.д. это, очевидно, не так. Следует
   * подумать над переносом данного метода и рефакторингом использующих его
   * методов.
   */

  /**
   * Получение DOM-элемента карты редактирования.
   * 
   * @return DOM-элемент
   */
  protected Element getInputElement() {
    return editableCard.getElement();
  }

  /**
   * Очищает значение поля.<br/>
   * При очистке значения поля, очищаются значения для обеих карт сразу: для
   * карты Редактирования и карты Просмотра.
   * 
   * Особенность:<br/>
   * Перекрытие метода обусловлено вызывом метода
   * {@link com.google.gwt.user.client.ui.Panel#clear}, удаляюшяего все
   * элементы с панели.
   */
  public void clear() {
    clearView();
  }

  /**
   * Метод для стилизации поля, а именно добавления классов на динамические элементы, напирмер, карту редактирования. 
   * Все стили должны определяется <b>в css-файле</b>.
   * <br/><br/>
   * Для применения новых стилей элемента необходимо в наследниках перекрывать
   * данный метод.
   */
  protected void applyStyle() {
    // Установка основного шрифта в карту редактирования.
    getInputElement().addClassName(MAIN_FONT_STYLE);
  }
}
