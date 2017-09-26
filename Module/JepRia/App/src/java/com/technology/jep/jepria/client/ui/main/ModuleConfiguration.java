package com.technology.jep.jepria.client.ui.main;

/**
 * Класс-связка ID модуля с конфигурацей его представления (view)
 */
public class ModuleConfiguration {
  
  public final String moduleId;
  public final String title;
  
  /**
   * @param id ID модуля
   * @param title заголовок, отображаемый на вкладке
   */
  public ModuleConfiguration(String id, String title) {
    this.moduleId = id;
    this.title = title;
  }
  
}