package com.technology.jep.jepria.client.ui.plain;

import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.Widget;

public class StandardModuleViewImpl extends PlainModuleViewImpl implements StandardModuleView {

  private HeaderPanel panel;
  
  public StandardModuleViewImpl() {
    panel = new HeaderPanel();
  
    // Компонент будет занимать всю область родителя.
    panel.setSize("100%", "100%");
    
    setWidget(panel);
  }
  
  /**
   * Установка/удаление виджета в центральную область.
   * 
   * @param newWidget устанавливаемый виджет
   */
  public void setBody(Widget newWidget) {
    // Если передана "команда" удалить ContentWidget - newWidget == null - удаляем виджет.
    if(newWidget == null) {
      clearContentWidget();
    } 
    // Если передан непустой новый виджет, то сравним его с ContentWidget : если он является отличным от переданного, то заменим его новым;
    // если это тот же самый виджет, то оставим все без изменения.
    else {
      Widget currentWidget = panel.getContentWidget();
      if(currentWidget != newWidget) {
        clearContentWidget();
        // Используем именно метод add(...), а не setContentWidget(...), т.к. после повторного использования последнего центральная панель
        // дополняется css свойством "display: none;" (станаовясь невидимой со всеми элементами на ней), которое затем нужно удалять 
        // "ручными манипуляциями".
        panel.add(newWidget);
      }
    }
  }
  
  /**
   * Очистка области содержимого.
   */
  private void clearContentWidget(){
    Widget currentWidget = panel.getContentWidget();
    if(currentWidget != null) {
      panel.remove(currentWidget);
    }
  }
  
  /**
   * Установка/удаление виджета в области инструментальной панели.
   * 
   * @param newWidget устанавливаемый виджет
   */
  public void setHeader(Widget newWidget) {
    // Если передана "команда" удалить HeaderWidget - newWidget == null - удаляем виджет.
    if(newWidget == null) {
      clearHeaderWidget();
    } 
    // Если передан непустой новый виджет, то сравним его с HeaderWidget : если он является отличным от переданного, то заменим его новым;
    // если это тот же самый виджет, то оставим все без изменения.
    else {
      Widget currentWidget = panel.getHeaderWidget();
      if(currentWidget != newWidget) {
        clearHeaderWidget();
        panel.setHeaderWidget(newWidget);
      }
    }
  }
  
  /**
   * Очистка области инструментальной панели.
   */
  private void clearHeaderWidget(){
    Widget currentWidget = panel.getHeaderWidget();
    if(currentWidget != null) {
      panel.remove(currentWidget);
    }
  }
  
  /**
   * Установка/удаление виджета в области панели состояния.
   * 
   * @param newWidget устанавливаемый виджет
   */
  public void setFooter(Widget newWidget) {
    // Если передана "команда" удалить FooterWidget - newWidget == null - удаляем виджет.
    if(newWidget == null) {
      clearFooterWidget();
    } 
    // Если передан непустой новый виджет, то сравним его с FooterWidget : если он является отличным от переданного, то заменим его новым;
    // если это тот же самый виджет, то оставим все без изменения.
    else {
      Widget currentWidget = panel.getFooterWidget();
      if(currentWidget != newWidget) {
        clearFooterWidget();
        panel.setFooterWidget(newWidget);
      }
    }
  }
  
  /**
   * Очистка области панели состояния.
   */
  private void clearFooterWidget(){
    Widget currentWidget = panel.getFooterWidget();
    if(currentWidget != null) {
      panel.remove(currentWidget);
    }
  }
  
}
