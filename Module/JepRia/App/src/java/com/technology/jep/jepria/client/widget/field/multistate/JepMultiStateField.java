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
import com.google.gwt.dom.client.Style;
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
 * ������� ����� �����, �������������� ��������� ������� ���������.<br/>
 * � ����� ������ ��� ������������� ���� ���������� ��������� ��������� ����:<br/>
 * <code>
 * <br/>
 *   // ������� ��������� ������������ ������ ������.<br/>
 *   FieldManager fieldManager = new FieldManager();<br/>
 *   // ������� ��������� ���� (��������: ���� ����� ����).<br/>
 *   JepDateField dateField = new JepDateField(formText.work_beginDate());<br/>
 *   // ���������� ���� � ������������ ����������.<br/>
 *   getFormPanel().add(dateField);<br/>
 *   // ���������������� ���� � ����������� ������.<br/>
 *   fieldManager.put(BEGIN_DATE, dateField);<br/>
 * <br/>
 * </code>
 * ��� ����������� ��������� ����, ��� ��� ���� ����� ���� &quot;��������&quot; � ���������� view ��������� �����.<br/>
 * <br/>
 * �������� ����� �������������� ����������� (�� �����������) �������� ��������������� ����� Gwt.<br/>
 * <br/>
 * ��������� ��������� ��������� ������� �������� � �������� ������ {@link com.technology.jep.jepria.client.widget}.
 */
public abstract class JepMultiStateField<E extends Widget, V extends Widget> extends Composite implements JepField<E, V>, JepMultiState, JepObservable, Validator {

  /**
   * �������� ������ �������
   */
  protected DeckPanel mainPanel;

  /**
   * ������, �� ������� ������������� ����� ������ ���������.<br/>
   * ������ ���������� ��� ���������� ���������� layout'� ��� ���������� �����
   * ���������.<br/>
   */
  @UiField
  protected FlowPanel viewPanel;
  
  public FlowPanel getViewPanel() {
        return viewPanel;
    }

    @UiField
  protected HTML viewCardLabel;

  /**
   * ����� ��� ������ ���������.<br/>
   * ����� ��������� ������������ ������� {@link com.technology.jep.jepria.client.ui.WorkstateEnum#isViewState(WorkstateEnum workstate)}.<br/>
   * <br/>
   * �������� ���� Jep-�����: ��� ������ ���� �������. �������, ����� ��������� ������ ���� ������ ��������� (��� ������� Html) ��������������
   * �������� ����� ��������������.<br/>
   * � ��� �������, ����� ����� ��������� ������������� ��������������� (������, ������� � �.�.) - � ���� ������������ ������ ���� ����� - 
   * ����� �������������� (�.�. ����� ��������� - ������ �� ������������).
   */
  @UiField
  protected V viewCard;

  /**
   * ������, �� ������� ������������� ����� ������ ��������������.<br/>
   * ������ ���������� ��� ���������� ���������� layout'� ��� ���������� ����� ��������������.<br/>
   */
  @UiField
  protected FlowPanel editablePanel;
  
  public FlowPanel getEditablePanel() {
        return editablePanel;
    }

    @UiField
  protected HTML editableCardLabel;

  /**
   * ����� ��� ������ ��������������.<br/>
   * ����� �������������� ������������ �������{@link com.technology.jep.jepria.client.ui.WorkstateEnum#isEditableState(WorkstateEnum workstate)}
   */
  protected E editableCard;

  /**
   * ������� ��������� ����.
   */
  protected WorkstateEnum _workstate;

  /**
   * ������� ����������� ����������� ����� �������������� ����.
   */
  protected boolean editable = true;

  /**
   * ������� ����������� ������� �������� ����.
   */
  protected boolean allowBlank = true;

  /**
   * ������� ������������ ����.
   */
  private boolean markedInvalid = false;

  /**
   * ������ ��� ������ �� ����������� ������� ����.
   */
  protected JepObservable observable;

  /**
   * ��������� �������� ������.
   */
  protected Image loadingIcon;

  /**
   * ������ ��� ��������� ������.
   */
  protected Image errorIcon;

  /**
   * ����� ����.
   */
  private String fieldLabel;

  /**
   * ID ������� Jep-���� ��� Web-��������.
   */
  protected String fieldIdAsWebEl;

  /**
   * ����������� ��� ����� ���� (�� ���������, ���������).
   */
  private String labelSeparator = ":";

  /**
   * ������������ ��������� (������ ������) ��������� ������� �����.
   */
  public static final String FIELD_INDICATOR_STYLE = "jepRia-Field-Icon";
  
  /**
   * �������������� ������ ������ ���� � ������ � ����������� �� ���������� ������������ � ����� - ��� ������ 2-px
   */
  public static final String FIELD_AUTO_HEIGTH_STYLE = "jepRia-AutoHeight";

  /**
   * ������������ ��������� (������ ������) ����� ���� �������������� � ���������.
   */
  public static final String LABEL_FIELD_STYLE = "jepRia-MultiStateField-Label";

  /**
   * ������������ ��������� (������ ������) ��� ����� ��������������.
   */
  public static final String EDITABLE_CARD_STYLE = "jepRia-MultiStateField-EditableCard";

  /**
   * ������������ ��������� (������ ������) ��� ����� ���������.
   */
  public static final String VIEW_CARD_STYLE = "jepRia-MultiStateField-ViewCard";
  
  /**
   * ������������ ��������� (������ ������) ��� ������ ���� ������������ ����������.
   */
  public static final String PANEL_ORIENTATION_VERTICAL_STYLE = "jepRia-vPanelStyle";

  /**
   * ������������ ��������� (������ ������) ��� ������ ���� �������������� ����������.
   */
  public static final String PANEL_ORIENTATION_HORIZONTAL_STYLE = "jepRia-hPanelStyle";
  
  /**
   * ������������ �������� ������������ DOM-��������.
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
    // ������������� ����������
    fieldLabel = (fieldLabel != null) ? fieldLabel : "";

    // ���� fieldIdAsWebEl �� ������, �� ���������� ���������������� �
    // ������ ������������,
    // ����� ���������� �������� ���������� � �����
    // ��������������/���������.
    this.fieldIdAsWebEl = fieldIdAsWebEl;

    // ���� � ������������ ������� �� ������ ������, DeckPanel �������� 100%.
    // ��� ��� �� �����, �.�. �������� � �������� ������ �����������
    // �������� � ������������� ��������.
    editablePanel.getElement().getStyle().clearWidth();
    observable = new JepObservableImpl();
    
    // ��������� ����� ���������.
    addViewCard();
    
    Element fieldViewCard = getViewCard().getElement();
        
    // �������������� ����� ��������������.
    addEditableCard();
    
    Element fieldEditableCard = getEditableCard().getElement();
    
    fieldViewCard.addClassName(VIEW_CARD_STYLE);
    fieldEditableCard.addClassName(EDITABLE_CARD_STYLE);

    // ������������� �������� ������� ����.
    setFieldLabel(fieldLabel);

    // ��������� �������� ���� ��������� � ��������������.
    setFieldWidth(FIELD_DEFAULT_WIDTH);
    setFieldHeight(FIELD_DEFAULT_HEIGHT);

    // ��������� ����� ������� � �� ������������.
    setLabelWidth(FIELD_LABEL_DEFAULT_WIDTH);

    // ���������� ������ � ����.
    applyStyle();

    changeWorkstate(SEARCH);

    // ��������� web-ID ����
    if (this.fieldIdAsWebEl != null) {
      setWebId(this.fieldIdAsWebEl);
    }

    // ��������� ��������� ����
    setCardWebAttrs();
  }

  /**
   * �������� web-ID ����.
   * 
   * @return web-ID ����.
   */
  public String getWebId() {
    return fieldIdAsWebEl;
  }

  /**
   * ��������� web-ID ����
   * 
   * @param fieldIdAsWebEl web-ID ����
   */
  public void setWebId(String fieldIdAsWebEl) {
    this.fieldIdAsWebEl = fieldIdAsWebEl;
    this.getElement().setId(fieldIdAsWebEl);
    // ��������� web-ID ������ ��������� ����
    setWebIds();
  }

  /**
   * ��������� web-ID ���� ���� ������� Jep-����.
   */
  private void setCardWebAttrs() {
    editableCard.getElement().setAttribute(JepRiaAutomationConstant.JEP_CARD_TYPE_HTML_ATTR,JepRiaAutomationConstant.JEP_CARD_TYPE_VALUE_EDTB);
    viewCard.getElement().setAttribute(JepRiaAutomationConstant.JEP_CARD_TYPE_HTML_ATTR, JepRiaAutomationConstant.JEP_CARD_TYPE_VALUE_VIEW);
  }

  /**
   * ��������� web-ID ���������� �����������, ����������� ��� ����������� Jep-����. 
   * ����� ������������ ��� ���������� ���������. �� ������ ����������� ��������������� ������� ����� �������� ����
   * this.fieldIdAsWebEl .
   */
  protected void setWebIds() {
    this.getInputElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_FIELD_INPUT_POSTFIX);
  }

  /**
   * ��������� ����� ���������.
   */
  public V getViewCard() {
    return (V) viewCard;
  }

  /**
   * �����, ����������� �� ������ ��������� ��������������� ���� (���������) Gwt.<br/>
   */
  protected void addViewCard() {}
  
  /**
   * �����, ����������� �� ������ �������������� ��������������� ���� (���������) Gwt.<br/>
   */
  protected abstract void addEditableCard();

  /**
   * ��������� ����� �������������� (� ������������ �������������� �
   * ���������� �������).
   */
  public E getEditableCard() {
    return editableCard;
  }

  /**
   * ��������� ������������ ���� � ����� ����������� �����, ���� ��� ������.
   * 
   * @param label ������������ ����
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
   * ��������� ������������ ����.
   *
   * @return ������������ ����
   */
  public String getFieldLabel() {
    return this.fieldLabel;
  }

  /**
   * ��������� ������ ���������
   * 
   * @param newWorkstate ����� ���������

   */
  public void changeWorkstate(WorkstateEnum newWorkstate) {
    // ������ � ������, ���� ������������� ���������� ���������.
    if (newWorkstate != null && !newWorkstate.equals(_workstate)) {
      onChangeWorkstate(newWorkstate);
      _workstate = newWorkstate;
    }
  }

  /**
   * ���������� ������ ���������
   * 
   * @param newWorkstate ����� ���������

   */
  protected void onChangeWorkstate(WorkstateEnum newWorkstate) {
    // ��� ����� ���������, ������� ������ ���������, ���� �������
    // ������������.

    clearInvalid();

    // ���� ��������� ������������ �� ������ �������������� � �����
    // ���������, �� ������� �������� ����� ������ ���������.

    if (WorkstateEnum.isEditableState(_workstate) && WorkstateEnum.isViewState(newWorkstate)) {
      setViewValue(getValue());
    }
    // ���� ��������� ������� ����������������� � ������ ��������������, ��
    // ����������� ����� � ����� ���������.

    if (WorkstateEnum.isEditableState(newWorkstate) && editable) {
      showWidget(getWidgetIndex(editablePanel));
    } else {
      showWidget(getWidgetIndex(viewPanel));
    }
  }

  /**
   * ��������� �������� ��� ����� ���������.<br/>
   * ��� ���������� ������� ������ � ����������� ���������� ����������, �����
   * ������ ����� ��� �������/�� �������-���������.<br/>
   * 
   * �������� ���� Jep-�����: ��� ������ ���� �������. �������, �����
   * ��������� ������ ���� ������ ��������� (��� ������� Html) ��������������
   * �������� ����� ��������������.<br/>
   * � ��� �������, ����� ����� ��������� ������������� ���������������
   * (������, ������� � �.�.) - � ���� ������������ ������ ���� ����� - �����
   * �������������� (�.�. ����� ��������� - ������ �� ������������).
   * 
   * ��� ��������� �������� ������������ ����������� ����������� �������� �
   * html-���������� ��� ����������� ����������� �� ��������.
   * 
   * @param value �������� ��� ����� ���������
   */
  protected void setViewValue(Object value) {
    ((HTML) viewCard).setHTML(value instanceof String ? SafeHtmlUtils.htmlEscape((String) value) : (value != null ? value.toString() : null));
  }

  /**
   * ������� �������� ��� ����� ���������.<br/>
   * ��� ���������� ������� ������ � ����������� ���������� ����������, �����
   * ������ ����� ��� �������/�� �������-���������.<br/>
   * 
   * �������� ���� Jep-�����: ��� ������ ���� �������. �������, �����
   * ��������� ������ ���� ������ ��������� (��� ������� Html) ��������������
   * �������� ����� ��������������.<br/>
   * � ��� �������, ����� ����� ��������� ������������� ���������������
   * (������, ������� � �.�.) - � ���� ������������ ������ ���� ����� - �����
   * �������������� (�.�. ����� ��������� - ������ �� ������������).
   */
  protected void clearView() {
    setViewValue(null);
  }

  /**
   * ��������� ������ ������������ ���� (� ��������).
   * 
   * @param width ������ ������������ ���� (� ��������). 
   */
  public void setLabelWidth(int width) {
    setLabelWidth(width + Unit.PX.getType());
  }
  
  /**
   * ��������� ������ ������������ ����.
   * 
   * @param width ������ ������������ ����.
   */
  public void setLabelWidth(String width) {
    viewCardLabel.setWidth(width);
    editableCardLabel.setWidth(width);
  }
  
  /**
   * ��������� ������ ���������� �������������� ���� (� ��������).
   * 
   * @param width ������ ���������� �������������� ���� (� ��������).
   */
  public void setFieldWidth(int width) {
    setFieldWidth(width + Unit.PX.getType());
  }

  /**
   * ��������� ������ ���������� �������������� ����.
   * 
   * @param width ������ ���������� �������������� ����
   */
  public void setFieldWidth(String width) {
    viewCard.setWidth(width);
    editableCard.setWidth(width);
  }
  
  /**
   * ��������� ������ ����.<br>
   * �� ��������� ������ ���������� ������ ��� ��� ���������� ��������������,
   * ��� � ��� ���������� ���������. ���� ���������� ������ ���������, ������
   * ����� ���������������� � ������-����������.
   * 
   * @param fieldHeight ������
   */
  public void setFieldHeight(int fieldHeight) {
    String height = "" + fieldHeight + Unit.PX;
    if (fieldHeight == FIELD_DEFAULT_HEIGHT) {
      setFieldAutoHeight(height);
    } else {
      // �������������� ������ ����� ��������������.
      editableCard.setHeight(height);
      // �������������� ������ ����� ���������.
      viewCardLabel.setHeight(height);
      viewCard.setHeight(height);
    }
  }
  
  public void setFieldAutoHeight(String height) {
    // �������������� ������ ����� ��������������.
    editableCard.setHeight(height);
    // �������������� ������ ����� ���������.
    viewCardLabel.getElement().addClassName(FIELD_AUTO_HEIGTH_STYLE);
    viewCard.getElement().addClassName(FIELD_AUTO_HEIGTH_STYLE);
  }

  /**
   * ���������� ��������� ������������� ���� �������.<br/>
   * ����������� ������� ������ {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#addListener(JepEventType eventType, JepListener listener)}
   * ������� {@link #observable}.
   *
   * @param eventType ��� �������
   * @param listener ���������
   */
  public void addListener(JepEventType eventType, JepListener listener) {
    observable.addListener(eventType, listener);
  }

  /**
   * �������� ��������� ������������� ���� �������.<br/>
   * ����������� ������� ������
   * {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#removeListener(JepEventType eventType, JepListener listener)}
   * ������� {@link #observable}.
   *
   * @param eventType ��� �������
   * @param listener  ���������
   */
  public void removeListener(JepEventType eventType, JepListener listener) {
    observable.removeListener(eventType, listener);
  }

  /**
   * ����������� ���������� ������������� ���� � �������.<br/>
   * ����������� ������� ������
   * {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#notifyListeners(JepEventType eventType, JepEvent event)}
   * ������� {@link #observable}.
   *
   * @param eventType ��� �������
   * @param event �������
   */
  public void notifyListeners(JepEventType eventType, JepEvent event) {
    observable.notifyListeners(eventType, event);
  }

  /**
   * ��������� ������ ���������� ������������� ���� �������.<br/>
   * ����������� ������� ������
   * {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#getListeners(JepEventType eventType)}
   * ������� {@link #observable}.
   *
   * @param eventType ��� �������
   *
   * @return ������ ����������
   */
  public List<JepListener> getListeners(JepEventType eventType) {
    return observable.getListeners(eventType);
  }

  /**
   * ��������� ����������� ��� ������������� (����� ��������������) ���� ���
   * ��������������.
   * 
   * @param enabled true - ���� �������� ��� ��������������, false - ���� �� �������� ��� ��������������
   */
  public abstract void setEnabled(boolean enabled);

  /**
   * ��������� ����������� ����������� ����� �������������� ����.
   * 
   * @param editable true - ����� �������������� ���� ������������ (������� �����), false - ������ ������������ ������ ����� ��������� ����
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
   * ��������� ��� ������� ����������� �������� (������ �� �����
   * ��������������).
   * 
   * @param imageVisible  ��������/������ ����������� ��������
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
   * ������� ������� � DIV.
   * @param widget ������.
   * @return ������.
   */
  public Widget wrapDiv(Widget widget) {
    HTMLPanel div = new HTMLPanel("");
    div.add(widget);
    return div;
  }

  /**
   * ������� � SPAN
   */
  public Widget wrapSpan(Widget widget) {
    InlineHTML spanBlock = new InlineHTML();
    spanBlock.getElement().appendChild(widget.getElement());
    return spanBlock;
  }

    
  Widget widgetIcon = null;
  /**
   * ��������� ��������� �� ������.
   * 
   * @param error  ����� ��������� �� ������
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
   * ������� ��������� �� ������.
   */
  public void clearInvalid() {
    // ���������: ���� �� ���� ������������� ���������.
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
   * ����������: �������� �� ������ �������� ���������� ��������� ����.
   * 
   * @param allowBlank true - ��������� ������ �������� ����, false - ���� ������������ ��� ����������.
   */
  public void setAllowBlank(boolean allowBlank) {
    this.allowBlank = allowBlank;
    setFieldLabel(getFieldLabel());
  }

  /**
   * ��������� �������� �����������. �� ���������, ������ ���������.
   * 
   * @param labelSeparator ����� �����������
   */
  public void setLabelSeparator(String labelSeparator) {
    this.labelSeparator = labelSeparator;
  }

  /**
   * ��������� �������� ���������� �� DOM-������ ���������.
   * 
   * @return ������������ ��������
   */
  public String getRawValue() {
    return getInputElement().getPropertyString("value");
  }

  /**
   * ���������, �������� �� ���� ���������� ��������. <br>
   * �������������� ������� ��������� �� ������. ���� ���� ��������
   * ������������, � �������� �������� �����, ������������� ��������� ��
   * ������ � ���������� false. ������������ ��� ��������������� �
   * �������-�����������.
   *
   * @return true - ���� ���� �������� ���������� ��������, false - �  ��������� ������
   */
  @Override
  public boolean isValid() {
    // ����� ���������, ������� ���������� ������.
    clearInvalid();
    if (!allowBlank && JepRiaUtil.isEmpty(getRawValue())) {
      markInvalid(JepTexts.field_blankText());
      return false;
    }
    return true;

  }

  /*
   * TODO ����� ����� ����� �� ��� ���� �����, � ���� ��� ���, � ������
   * ������� ����� ���� ������� ���� input. ��� JepListField,
   * JepDualListField, JepTreeField � �.�. ���, ��������, �� ���. �������
   * �������� ��� ��������� ������� ������ � ������������� ������������ ���
   * �������.
   */

  /**
   * ��������� DOM-�������� ����� ��������������.
   * 
   * @return DOM-�������
   */
  protected Element getInputElement() {
    return editableCard.getElement();
  }

  /**
   * ������� �������� ����.<br/>
   * ��� ������� �������� ����, ��������� �������� ��� ����� ���� �����: ���
   * ����� �������������� � ����� ���������.
   * 
   * �����������:<br/>
   * ���������� ������ ����������� ������� ������
   * {@link com.google.gwt.user.client.ui.Panel#clear}, ����������� ���
   * �������� � ������.
   */
  public void clear() {
    clearView();
  }

  /**
   * ����� ��� ���������� ����.<br/>
   * ��� ���������� ����� ������ �������� ���������� � ����������� �����������
   * ������ �����.
   */
  protected void applyStyle() {
    // ������������� �������� �� ��������� ��� ���������� JepMultiStateField.
    mainPanel.getElement().getStyle().setMarginBottom(5, Unit.PX);
    // ��������� ��������� ������ � ����� ��������������.
    getInputElement().addClassName(MAIN_FONT_STYLE);
    // ������� ������� � ������� ����� ��������������.
    removeMarginsAndPaddings(getInputElement());
  }

  /**
   * �������� �������� � �������� ��������.
   * 
   * @param stylezedElement ����������� �������
   */
  protected static void removeMarginsAndPaddings(Element stylezedElement) {
    Style style = stylezedElement.getStyle();
    style.setMargin(0, Unit.PX);
    style.setPadding(0, Unit.PX);
  }
}
