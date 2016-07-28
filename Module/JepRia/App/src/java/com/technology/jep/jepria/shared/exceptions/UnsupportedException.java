package com.technology.jep.jepria.shared.exceptions;

/**
 * Сигнализирует об отсутствии поддержки функции
 */
public class UnsupportedException extends SystemException {
  
  private static final long serialVersionUID = 4781584672406212570L;

  public UnsupportedException() {
  }
  
  /**
   * Конструктор
   * 
   * @param message сообщение
   * @param cause причина исключения, может принимаь значение null
   */
  public UnsupportedException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedException(String message) {
    super(message);
  }
}
