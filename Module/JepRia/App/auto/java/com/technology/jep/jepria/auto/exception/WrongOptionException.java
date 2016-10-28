package com.technology.jep.jepria.auto.exception;

/**
 * Выбрасывается при обнаружении некорректной опции в коде Automation
 */
@SuppressWarnings("serial")
public class WrongOptionException extends AutomationException {

  public WrongOptionException() {
  }
  
  public WrongOptionException(String message) {
    super(message);
  }
  
  public WrongOptionException(String message, Throwable th) {
    super(message, th);
  }
}
