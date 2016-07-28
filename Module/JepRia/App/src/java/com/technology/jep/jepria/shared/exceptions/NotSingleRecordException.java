package com.technology.jep.jepria.shared.exceptions;

/**
 * Исключение, возникаемое в результате поискового запроса, возвращающего 
 * отличное от ожидаемого количество записей, не равное одному.
 */
public class NotSingleRecordException extends SystemException {
  
  private static final long serialVersionUID = 4781584672406212570L;

  public NotSingleRecordException() {
  }
  
  public NotSingleRecordException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotSingleRecordException(String message) {
    super(message);
  }
}
