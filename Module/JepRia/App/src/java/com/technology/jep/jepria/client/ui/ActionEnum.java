package com.technology.jep.jepria.client.ui;

import static com.technology.jep.jepria.client.ui.UiSecurity.DO_DELETE_EVENT_NAME;
import static com.technology.jep.jepria.client.ui.UiSecurity.DO_PRINT_EVENT_NAME;
import static com.technology.jep.jepria.client.ui.UiSecurity.DO_SEARCH_EVENT_NAME;
import static com.technology.jep.jepria.client.ui.UiSecurity.SAVE_EVENT_NAME;
import static com.technology.jep.jepria.client.ui.UiSecurity.SHOW_EXCEL_EVENT_NAME;

/**
 * Базовые состояния Jep-модуля.
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
public enum ActionEnum {
  DELETE(DO_DELETE_EVENT_NAME),
  PRINT(DO_PRINT_EVENT_NAME),
  SAVE(SAVE_EVENT_NAME),      // TODO Нужно ли это ?
  SEARCH(DO_SEARCH_EVENT_NAME),
  SHOW_EXCEL(SHOW_EXCEL_EVENT_NAME);

  private String actionId = null;

  private ActionEnum(String actionId) {
    this.actionId = actionId;
  }

  @Override
  public String toString() {
    return actionId;
  }
}
