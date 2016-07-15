package com.technology.jep.jepria.client.ui.plain;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.ui.JepPresenter;

public class PlainModuleViewImpl implements PlainModuleView {
  
  protected JepPresenter presenter = null;
  protected Widget widget = null;
  
  public PlainModuleViewImpl() {

  }
  
  public PlainModuleViewImpl(Widget widget) {
    setWidget(widget);
  }
  
  public void setWidget(Widget widget) {
    this.widget = widget;
    this.widget.getElement().setId(JepRiaAutomationConstant.MODULE_PANEL_ID);
  }

  public Widget asWidget() {
    return widget;
  }
  
  public void setPresenter(JepPresenter presenter) {
    this.presenter = presenter;    
  }
  
}
