package com.technology.jep.jepria.client.ui.toolbar;

import static com.technology.jep.jepria.client.JepRiaClientConstant.TOOLBAR_DEFAULT_STYLE;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarViewImpl;

/**
 * Реализация инструментальной панели с заголовкой. 
 * Простая надстройка над стандартной инструментальной панелью. 
 */
public class ToolBarWithHeaderViewImpl extends ToolBarViewImpl
    implements ToolBarWithHeaderView {

  /**
   * Виджет заголовка.
   */
  private Label headerText = new Label();
  
  /**
   * Виджет инструментальной панели с заголовком.
   */
  private VerticalPanel toolBarWithHeader = new VerticalPanel();
  
  /**
   * Конструктор.
   */
  public ToolBarWithHeaderViewImpl() {
    super();
    
    SimplePanel header = new SimplePanel();
    header.setWidget(headerText);
    header.setStyleName(TOOLBAR_DEFAULT_STYLE);
    
    toolBarWithHeader.setWidth("100%");
    toolBarWithHeader.add(toolBar);
    toolBarWithHeader.add(header);
  }
  
  @Override
  public VerticalPanel asWidget() {
    return toolBarWithHeader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHeaderText(String text) {
    headerText.setText(text);
  }
}
