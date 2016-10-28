package com.technology.jep.jepria.auto.util;

import static com.technology.jep.jepria.server.JepRiaServerConstant.JEP_RIA_RESOURCE_BUNDLE_NAME;

import java.util.Locale;
import java.util.ResourceBundle;

import com.technology.jep.jepria.client.ui.WorkstateEnum;

public class Util {

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
}
