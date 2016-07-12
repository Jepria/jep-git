package com.technology.jep.jepria.client.history.place;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;

public abstract class JepPlace extends Place {
  public static final String SCOPE_MODULES_PARAMETER_NAME = "sm";
  public static final String WORKSTATE_PARAMETER_NAME = "ws";
  public static final String ACTIVE_MODULE_PARAMETER_NAME = "am";
  public static final String SEARCH_REQUEST_PARAMETER_NAME = "sr";
  public static final String LOAD_CONFIG_PARAMETER_NAME = "lc";
  public static final String PRIMARY_KEY_PARAMETER_NAME = "pk";
  // TODO Поддержать сохранение templateProperties для всех модулей scope
  public static final String TEMPLATE_PROPERTIES_PARAMETER_NAME = "tp"; // Необходимо для воспроизведения результатов поиска 

  abstract public String getDisplayName();

  public String getActiveModuleId() {
    return JepScopeStack.instance.peek().getActiveModuleId();
  }

  public String toString() {
    return this.getClass().getName() + ": '" + JepScopeStack.instance.toString() + "'";
  }
}
