package com.technology.jep.jepria.auto.entrance;

import com.technology.jep.jepria.auto.manager.AutomationManager;

/**
 * EntranceAppAuto поддерживает функции входа/выхода через EntranceAuto
 */
public interface EntranceAppAuto extends AutomationManager {

  /**
   * Получение интерфейса работы с EntranceAuto
   * 
   * @return интерфейс EntranceAuto
   */
  AuthorizationAuto getEntranceAuto();
}
