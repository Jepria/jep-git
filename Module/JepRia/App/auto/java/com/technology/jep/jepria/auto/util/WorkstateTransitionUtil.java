package com.technology.jep.jepria.auto.util;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_ADD_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_EDIT_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_FIND_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_LIST_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_SEARCH_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_VIEW_DETAILS_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.server.JepRiaServerConstant.JEP_RIA_RESOURCE_BUNDLE_NAME;

import java.util.Locale;
import java.util.ResourceBundle;

import com.technology.jep.jepria.client.ui.WorkstateEnum;

public class WorkstateTransitionUtil {

  static public String getResourceString(String key) {
    return ResourceBundle.getBundle(JEP_RIA_RESOURCE_BUNDLE_NAME, new Locale("ru", "RU")).getString(key);
  }

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
   * Получение кнопки toolbar для перехода в заданное состояние
   * @param workstate
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
  
  public static String getStatusTextForWorkstate(WorkstateEnum workstate) {
    switch (workstate) {
      case CREATE: return getResourceString("workstate.add");
      case EDIT: return getResourceString("workstate.edit");
      case VIEW_DETAILS: return getResourceString("workstate.viewDetails");
      case SEARCH: return getResourceString("workstate.search");
      case SELECTED: return getResourceString("workstate.selected");
      case VIEW_LIST: return getResourceString("workstate.viewList");
    }
    throw new IllegalArgumentException("Unknown workstate: " + workstate);
  }
  
  public static WorkstateEnum getWorkstateForStatusText(String statusText) {
    if (statusText != null) {
      if (statusText.equals(getResourceString("workstate.add"))) {
        return WorkstateEnum.CREATE;
      } else if (statusText.equals(getResourceString("workstate.edit"))) {
        return WorkstateEnum.EDIT;
      } else if (statusText.equals(getResourceString("workstate.viewDetails"))) {
        return WorkstateEnum.VIEW_DETAILS;
      } else if(statusText.equals(getResourceString("workstate.search"))) {
        return SEARCH;
      } else if(statusText.equals(getResourceString("workstate.selected"))) {
        return WorkstateEnum.SELECTED;
      } else if(statusText.equals(getResourceString("workstate.viewList"))) {
        return WorkstateEnum.VIEW_LIST;
      }
    }
    throw new IllegalArgumentException("Unknown workstate status: " + statusText);
  }
}
