package com.technology.jep.jepria.client.ui.statusbar;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.*;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

/**
 * Реализация панели состояния.
 */
public class StatusBarViewImpl implements StatusBarView {

  protected JepPresenter<?,?> presenter = null;
  protected Label label;
  
  protected final String moduleId;
  
  public StatusBarViewImpl(String moduleId) {
    this.moduleId = moduleId;
    
    setWidget(new Label());
    
    /*
     * Предполагаем, что look & feel аналогичен панели инструментов.
     */
    label.addStyleName(STATUSBAR_DEFAULT_STYLE);
    
    setHeight(DEFAULT_HEIGHT);
  }

  public void setHeight(int height) {
    label.setHeight(height + Unit.PX.getType());
  }

  public void setWidget(Widget widget) {
    label = (Label)widget;
    setWebIds();
  }
  
  public Widget asWidget() {
    return label;
  }
  
  public void setPresenter(JepPresenter<?,?> presenter) {
    this.presenter = presenter;
  }
  
  @Override
  public void showWorkstate(WorkstateEnum workstate) {
    String displayState = "";
    if (CREATE.equals(workstate)) {
      displayState = JepTexts.workstate_add();
    } else if (VIEW_DETAILS.equals(workstate)) {
      displayState = JepTexts.workstate_viewDetails();
    } else if (VIEW_LIST.equals(workstate)) {
      displayState = JepTexts.workstate_viewList();
    } else if (EDIT.equals(workstate)) {
      displayState = JepTexts.workstate_edit();
    } else if (SEARCH.equals(workstate)) {
      displayState = JepTexts.workstate_search();
    } else if (SELECTED.equals(workstate)) {
      displayState = JepTexts.workstate_selected();
    }
    label.setText(displayState);
    label.getElement().setAttribute(JepRiaAutomationConstant.STATUSBAR_PANEL_WORKSTATE_HTML_ATTR, workstate.getId());
  }
  
  protected void setWebIds() {
    label.getElement().setId(JepRiaAutomationConstant.STATUSBAR_PANEL_ID);
    label.getElement().setAttribute(JepRiaAutomationConstant.STATUSBAR_PANEL_MODULE_HTML_ATTR, moduleId);
  }
  
}
