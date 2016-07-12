package com.technology.jep.jepria.client.history.place;

import com.technology.jep.jepria.client.ui.WorkstateEnum;

/**
 * Places, соответствующие Workstates
 */
public class JepWorkstatePlace extends JepPlace {
  private final WorkstateEnum _workstate;

  /**
   * Получение Workstate конкретного Place
   * Возможно, временное решение на переходный период от Workstates к Places
   * 
   * @return _workstate
   */
  public WorkstateEnum getWorkstate() {
    return _workstate;
  }
  
  public JepWorkstatePlace(WorkstateEnum workstate) {
    _workstate = workstate;
  }

  @Override
  public String getDisplayName() {
    return _workstate != null ? _workstate.name() : "unknown";
  }
}
