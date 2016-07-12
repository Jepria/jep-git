package com.technology.jep.jepria.auto.entrance;

import com.technology.jep.jepria.auto.manager.JepRiaAuto;

/**
 * EntranceAppAuto поддерживает функции входа/выхода через EntranceAuto
 */
public interface EntranceAppAuto extends JepRiaAuto {

  /**
   * Получение интерфейса работы с EntranceAuto
   * 
   * @return интерфейс EntranceAuto
   */
  EntranceAuto getEntranceAuto();
}
