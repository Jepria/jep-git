package com.technology.jep.jepria.auto.util;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_ADD_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_EDIT_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_FIND_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_LIST_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_SEARCH_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_VIEW_DETAILS_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;

import com.technology.jep.jepria.client.ui.WorkstateEnum;

public class WorkstateTransitionUtil {

  public static boolean isWorkstateTransitionAcceptable(WorkstateEnum workstateFrom, WorkstateEnum workstateTo) {
    switch(workstateFrom) {
    case CREATE:
      switch(workstateTo) {
      case EDIT:
        return false;
      case CREATE:
      case VIEW_DETAILS:
      case SEARCH:
      case VIEW_LIST:
      case SELECTED:
        return true;
      }
    case EDIT:
      return true;
    case SEARCH:
      switch(workstateTo) {
      case EDIT:
      case SELECTED:
      case VIEW_DETAILS:
        return false;
      case CREATE:
      case SEARCH:
      case VIEW_LIST:
        return true;
      }
    case SELECTED:
      return true;
    case VIEW_DETAILS:
      return true;
    case VIEW_LIST:
      switch(workstateTo) {
      case EDIT:
      case VIEW_DETAILS:
        return false;
      case SELECTED:
      case CREATE:
      case SEARCH:
      case VIEW_LIST:
        return true;
      }
    }
    
    return false;
  }

  /**
   * Получение кнопки toolbar для перехода в заданное состояние.
   * 
   * @param workstateFrom Cостояние перехода из.
   * @param workstateTo Cостояние перехода в.
   * @return id кнопки Toolbar
   */
  public static String getToolbarButtonId(WorkstateEnum workstateFrom, WorkstateEnum workstateTo) {
    String toolbarButtonId = null;
    if(isWorkstateTransitionAcceptable(workstateFrom, workstateTo)) { // Проверка возможности перехода (во избежание "бесконечного ожидания")
      switch(workstateTo) {
      case CREATE:
        toolbarButtonId = TOOLBAR_ADD_BUTTON_ID;
        break;
      case EDIT:
        toolbarButtonId = TOOLBAR_EDIT_BUTTON_ID;
        break;
      case SEARCH:
        toolbarButtonId = TOOLBAR_SEARCH_BUTTON_ID;
        break;
      case SELECTED:
        // TODO Что здесь делать ?
        toolbarButtonId = TOOLBAR_SEARCH_BUTTON_ID;
        break;
      case VIEW_DETAILS:
        toolbarButtonId = TOOLBAR_VIEW_DETAILS_BUTTON_ID;
        break;
      case VIEW_LIST:
        toolbarButtonId = workstateFrom == SEARCH ? TOOLBAR_FIND_BUTTON_ID : TOOLBAR_LIST_BUTTON_ID;
        break;
      }
    }
    
    return toolbarButtonId;
  }
}
