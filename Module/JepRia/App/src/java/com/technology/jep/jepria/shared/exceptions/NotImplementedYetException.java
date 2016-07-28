package com.technology.jep.jepria.shared.exceptions;

/**
 * Выбрасывается при обнаружении нереализованной функциональности
 */
public class NotImplementedYetException extends SystemException {
  
  private static final long serialVersionUID = -1260238884177943712L;

  public NotImplementedYetException() {
  }
  
  public NotImplementedYetException(String message) {
    super(message);
  }

}
