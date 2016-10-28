package com.technology.jep.jepria.auto.exception;

/**
 * Выбрасывается при выполнении недопустимого кода
 */
@SuppressWarnings("serial")
public class NotExpectedException extends RuntimeException {

  public NotExpectedException() {
  }
  
  public NotExpectedException(String message) {
    super(message);
  }
}
