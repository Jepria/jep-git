package com.technology.jep.jepria.client.message;


/**
 * Интерфейс вывода сообщений.
 */
public interface JepMessageBox {
  /**
   * Вывод сообщения.
   * 
   * @param message текст сообщения
   */
  MessageBox alert(String message);

  /**
   * Вывод сообщения с заголовком.
   * 
   * @param title заголовок
   * @param message текст сообщения
   */
  MessageBox alert(String title, String message);

  /**
   * Вывод сообщения об ошибке.
   * 
   * @param message текст сообщения
   */
  MessageBox showError(String message);

  /**
   * Вывод сообщения об ошибке по заданному Throwable.
   * 
   * @param caught исключение - причина ошибки
   */
  MessageBox showError(Throwable caught);

  /**
   * Вывод сообщения об ошибке по заданному Throwable и заданному тексту.
   * 
   * @param caught исключение - причина ошибки
   * @param message текст сообщения
   */
  MessageBox showError(Throwable caught, String message);

  /**
   * Запрос подтверждения.
   * 
   * @param isMultiple признак множественного удаления
   * @param callback обратный вызов
   */
  MessageBox confirmDeletion(boolean isMultiple, final ConfirmCallback callback);
}
