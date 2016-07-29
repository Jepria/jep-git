package com.technology.jep.jepria.shared.exceptions;

/**
 * Инициируется при обнаружении неверного формата
 */
public class WrongFormatException extends SystemException {
  
  private static final long serialVersionUID = 4781584672406212570L;

  public WrongFormatException() {
  }
  
  /**
   * Конструктор
   * 
   * @param message сообщение
   * @param cause причина исключения, может принимать значение null
   */
  public WrongFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  public WrongFormatException(String message) {
    super(message);
  }
}
