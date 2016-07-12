package com.technology.jep.jepria.auto.exceptions;

/**
 * Выбрасывается при обнаружении исключительной ситуации в коде Automation
 */
@SuppressWarnings("serial")
public class AutomationException extends RuntimeException {

  public AutomationException() {
  }
  
  public AutomationException(String message) {
    super(message);
  }
  
  public AutomationException(String message, Throwable th) {
    super(message, th);
  }
}
