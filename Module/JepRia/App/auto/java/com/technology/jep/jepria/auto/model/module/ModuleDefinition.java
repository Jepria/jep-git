package com.technology.jep.jepria.auto.model.module;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;

/**
 * Класс, описывающий модуль приложения (также определяющий вкладку модуля для переключения между модулями).
 * 
 * @param <A> - Интерфейс взаимодействия с конкретным модулем.
 */

public class ModuleDefinition<A extends JepRiaModuleAuto> {

  /**
   * Конструктор.
   * 
   * @param moduleID - Идентификатор модуля.
   * @param entranceURL - URL для входа в модуль.
   * @param moduleAuto - Реализация интерфейса автоматизации
   */
  public ModuleDefinition(String moduleID, String entranceURL, A moduleAuto) {
    super();
    this.moduleID = moduleID;
    this.entranceURL = entranceURL;
    this.moduleAuto = moduleAuto;
  }

  private String moduleID;
  private String entranceURL;
  private A moduleAuto;
  
  /**
   * @return the moduleAuto
   */
  public A getModuleAuto() {
    return moduleAuto;
  }

  /**
   * @return the moduleID
   */
  public String getModuleID() {
    return moduleID;
  }

  /**
   * @return the entraceURL
   */
  public String getEntranceURL() {
    return entranceURL;
  }
}
