package com.technology.jep.jepria.client.exception;


/**
 * Интерфейс обработки исключений.
 */
public interface ExceptionManager {

  /**
   * Обработка исключения.
   * 
   * @param th обрабатываемое исключение
   */
  void handleException(Throwable th);


  /**
   * Обработка исключения с выводом сообщения.
   * 
   * @param th обрабатываемое исключение
   * @param message сообщение об ошибке
   */
  void handleException(Throwable th, String message);
}
