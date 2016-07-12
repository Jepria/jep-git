package com.technology.jep.jepria.shared.log;

/**
 * Интерфейс логирования.
 */
public interface JepLogger {
  void trace(String message);
  void debug(String message);
  void warn(String message);
  void error(String message);
  void info(String message);
}
