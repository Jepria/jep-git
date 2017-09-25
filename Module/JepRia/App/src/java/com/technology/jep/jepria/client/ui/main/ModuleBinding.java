package com.technology.jep.jepria.client.ui.main;

import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;

/**
 * Класс-связка ID модуля и его клиентской фабрики
 */
public class ModuleBinding {
  
  public final String moduleId;
  public final PlainClientFactoryImpl.Creator plainFactoryCreator;
  
  /**
   * @param moduleId ID модуля
   * @param plainFactoryCreator создатель экземпляра клиентской фабрики
   */
  public ModuleBinding(String moduleId, PlainClientFactoryImpl.Creator plainFactoryCreator) {
    this.moduleId = moduleId;
    this.plainFactoryCreator = plainFactoryCreator;
  }
  
}