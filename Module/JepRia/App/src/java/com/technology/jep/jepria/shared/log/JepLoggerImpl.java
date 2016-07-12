package com.technology.jep.jepria.shared.log;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Обёртка для gwt-log.<br/>
 * Нестатичность методов вызвана необходимостью реализации интерфейса JepLogger.
 */
public class JepLoggerImpl implements JepLogger {
  public static JepLoggerImpl instance = new JepLoggerImpl();

  private JepLoggerImpl() {
  }

  public void trace(String message) {
    Log.trace(message);
  }

  public void debug(String message) {
    Log.debug(message);
  }

  public void error(String message) {
    Log.error(message);
  }

  public void info(String message) {
    Log.info(message);
  }

  public void warn(String message) {
    Log.warn(message);
  }

}
