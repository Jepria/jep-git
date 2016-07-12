package com.technology.jep.jepria.client.widget.container;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class JepTabLayoutPanel extends TabLayoutPanel {

  public JepTabLayoutPanel(String id, double barHeight, Unit barUnit) {
    super(barHeight, barUnit);
    
    getElement().setId(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Widget getWidget() {
    return super.getWidget();
  }

  /**
   * Получение ссылки на панель вкладки по индексу.
   * 
   * @param index    указанный индекс
   * @return  панель вкладки
   */
  public SimplePanel getTab(int index) {
    return (SimplePanel) getTabWidget(index).getParent();
  }

  /**
   * Добавление обработчика клика для панели вкладки по индексу. 
   * 
   * @param index    указанный индекс
   * @param handler  обработчик клика
   * @return  регистрация события
   */
  public HandlerRegistration addTabClickHandler(int index, ClickHandler handler) {
    return getTab(index).addDomHandler(handler, ClickEvent.getType());
  }
  
  /**
   * Получение ссылки на лейбл вкладки по индексу.
   * 
   * @param index    указанный индекс
   * @return  лейбл вкладки
   */
  public Label getTabLabel(int index){
    return (Label) getTabWidget(index);
  }
}
