package com.technology.jep.jepria.auto.model.module;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

/**
 * Класс, описывающий модуль приложения (также определяющий вкладку модуля для переключения между модулями).
 * 
 * @param <A> - Интерфейс взаимодействия с конкретным модулем.
 */

public class ModuleDescription<A extends JepRiaModuleAuto> {

  /**
   * Конструктор.
   * 
   * @param moduleID - Идентификатор модуля.
   * @param entranceURL - URL для входа в модуль.
   * @param moduleAuto - Реализация интерфейса автоматизации.
   * @param moduleAuto - Стартовое состояние при входе в модуль.
   */
  public ModuleDescription(String moduleID, String entranceURL, WorkstateEnum entranceWorkstate, A moduleAuto) {
    super();
    this.moduleID = moduleID;
    this.entranceURL = entranceURL;
    this.entranceWorkstate = entranceWorkstate;
    this.moduleAuto = moduleAuto;
  }

  private WorkstateEnum entranceWorkstate;
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
  
  /**
   * @return the entranceWorkstate
   */
  public WorkstateEnum getEntranceWorkstate() {
    return entranceWorkstate;
  }
}
