package com.technology.jep.jepria.client.ui;

import static com.technology.jep.jepria.client.ui.WorkstateConstant.*;

/**
 * Базовые состояния клиентского модуля.
 * 
 * TODO Комментарий ниже перенести в более надлежащее место документации системы.
 * Как таковое, базовое множество состояний не расширяется пользовательскими
 * модулями, в частности, по причине потенциального усложнения, которое может вызвать
 * неопределённость при переходе из базового состояния в пользовательское: новое
 * пользовательское состояние может как отменять базовое, так и детализировать его.
 * В случае отмены возникла бы неопределённость на системном уровне (система без состояния). 
 * В случае детализации одновременно возникали бы два состояния, что привело бы к неоправданным
 * усложнениям.
 * Поэтому на системном уровне пользовательские состояния не поддерживаются.
 */
public enum WorkstateEnum {
    
  VIEW_LIST(VIEW_LIST_WORKSTATE_ID),
  SELECTED(SELECTED_WORKSTATE_ID),
  VIEW_DETAILS(VIEW_DETAILS_WORKSTATE_ID),
  CREATE(CREATE_WORKSTATE_ID),
  EDIT(EDIT_WORKSTATE_ID),
  SEARCH(SEARCH_WORKSTATE_ID);
  
  /**
   * Идентификатор рабочего состояния
   */
  private String workstateId;

  private WorkstateEnum(String workstateId) {
    this.workstateId = workstateId;
  }
  
  public String getId() {
    return workstateId;
  }

  /**
   * Преобразование строки, содержащей идентификатор рабочего состояния, 
   * в соответствующее значение.
   * 
   * @param strWorkstate  идентификатор рабочего состояния
   * @return рабочее состояние
   */
  public static WorkstateEnum fromString(String strWorkstate) {
    if(VIEW_LIST.toString().equals(strWorkstate)) {
      return VIEW_LIST;
    } else if(SELECTED.toString().equals(strWorkstate)) {
      return SELECTED;
    } else if(CREATE.toString().equals(strWorkstate)) {
      return CREATE;
    } else if(EDIT.toString().equals(strWorkstate)) {
      return EDIT;
    } else if(SEARCH.toString().equals(strWorkstate)) {
      return SEARCH;
    } else if(VIEW_DETAILS.toString().equals(strWorkstate)) {
      return VIEW_DETAILS;
    }
    // Вернем Workstate "по умолчанию", если не удалось понять, что передано в строке (чаще всего пришедшее из History).
    return SEARCH;
  }
  
  @Override
  public String toString() {
    return workstateId;
  }

  public static boolean isViewState(WorkstateEnum workstate) {
    return VIEW_DETAILS.equals(workstate) || VIEW_LIST.equals(workstate) || SELECTED.equals(workstate);
  }

  public static boolean isEditableState(WorkstateEnum workstate) {
    return EDIT.equals(workstate) || CREATE.equals(workstate) || SEARCH.equals(workstate);
  }
}
