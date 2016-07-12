package com.technology.jep.jepria.client.ui.form.detail;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.widget.field.FieldManager;

/**
 * Стандартная детальная форма.<br/>
 * В отличие от {@link com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl}, при создании представления формы 
 * не требуется ручное добавление базовых графических элементов (VerticalPanel, ScrollPanel) на форму.
 */
public class StandardDetailFormViewImpl extends DetailFormViewImpl {

  /**
   * VerticalPanel для возможности управления виджетами
   * (добавление, выравнивание).
   */
  protected VerticalPanel panel;
  
  public StandardDetailFormViewImpl() {
    this(new FieldManager());
  }
  
  public StandardDetailFormViewImpl(FieldManager fields) {
    super(fields);
    
    ScrollPanel scrollPanel = new ScrollPanel();
    setWidget(scrollPanel);
    
    scrollPanel.setSize("100%", "100%");
    
    panel = new VerticalPanel();
    panel.getElement().getStyle().setMarginTop(5, Unit.PX);
    scrollPanel.add(panel);
  }
}
