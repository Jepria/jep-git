package com.technology.jep.jepria.shared.exceptions;

/**
 * Сигнализирует об отсутствии идентификатора (модуля, поля и т.д.)
 */
public class IdNotFoundException extends SystemException {
  
  private static final long serialVersionUID = -1L;

  public IdNotFoundException() {
  }
  
  public IdNotFoundException(String message) {
    super(message);
  }
}
