package com.technology.jep.jepria.client;

/**
 * Класс-агрегатор идентификатора модуля и его наименования
 * @author RomanovAS
 */
public class ModuleItem {
  public final String moduleId, title;

  public ModuleItem(String moduleId, String title) {
    this.moduleId = moduleId;
    this.title = title;
  }
  
  @Override
  public String toString() {
    return moduleId + "/" + title;
  }
}
