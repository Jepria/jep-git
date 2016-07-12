package com.technology.jep.jepria.client.widget.field.multistate;

import com.technology.jep.jepria.client.ui.WorkstateEnum;

/**
 * Интерфейс элементов пользовательского интерфейса, поддерживающих несколько
 * рабочих состояний.
 */
public interface JepMultiState {
  
  /**
   * Установка нового состояния
   * 
   * @param workstate новое состояние
   */
  void changeWorkstate(WorkstateEnum workstate);
  
}
