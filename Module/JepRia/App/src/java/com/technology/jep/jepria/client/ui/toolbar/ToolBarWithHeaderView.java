package com.technology.jep.jepria.client.ui.toolbar;

import com.technology.jep.jepria.client.ui.toolbar.ToolBarView;

/**
 * Интерфейс инструментальной панели с заголовком.
 */
public interface ToolBarWithHeaderView extends ToolBarView {
  
  /**
   * Устанавливает текст заголовка.
   */
  void setHeaderHTML(String text);
}
