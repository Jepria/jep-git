package com.technology.jep.jepria.shared.exceptions;


/**
 * Выбрасывается при обнаружении нереализованной функциональности
 */
@SuppressWarnings("serial")
public class NotImplementedYetException extends RuntimeException {
  public NotImplementedYetException() {
  }
  
  public NotImplementedYetException(String message) {
    super(message);
  }

}
