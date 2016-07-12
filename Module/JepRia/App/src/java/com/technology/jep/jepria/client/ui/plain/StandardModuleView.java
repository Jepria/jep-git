package com.technology.jep.jepria.client.ui.plain;

import com.google.gwt.user.client.ui.Widget;

public interface StandardModuleView extends PlainModuleView {

  /**
   * Установка/удаление виджета в центральную область.
   * 
   * @param body устанавливаемый виджет
   */
  void setBody(Widget body);

  /**
   * Установка/удаление виджета в области инструментальной панели.
   * 
   * @param newWidget устанавливаемый виджет
   */
  void setHeader(Widget newWidget);

  /**
   * Установка/удаление виджета в области панели состояния.
   * 
   * @param newWidget устанавливаемый виджет
   */
  void setFooter(Widget newWidget);

}
