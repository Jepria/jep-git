package com.technology.jep.jepria.client.widget.list;

/**
 * Интерфейс-маркер для колонок {@link com.technology.jep.jepria.client.widget.list.cell.TreeCell} или {@link com.technology.jep.jepria.client.widget.list.cell.EditTreeCell}, 
 * свызываемых с реализацией {@link TreeGridManager} для управления их состоянием.
 */
public interface HasTreeGridManager {
  
  /**
   * Устанавливается менеджер для управления состоянием колонок.
   * 
   * @param treeGridManager  менеджер древовидного справочника
   */
  void setTreeGridManager(TreeGridManager<?, ?, ?> treeGridManager);
}
