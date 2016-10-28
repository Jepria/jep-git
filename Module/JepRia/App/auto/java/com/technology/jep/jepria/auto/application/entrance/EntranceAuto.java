package com.technology.jep.jepria.auto.application.entrance;

/**
 * Интерфейс входа в приложение (авторизация) 
 */
public interface EntranceAuto {
  /**
   * Вход в систему
   * 
   * @param username - идентификатор пользователя
   * @param password - пароль
   */
  void login(String username, String password);
  
  /**
   * Проверка был ли вход
   * 
   * @return true, если вход выполнен, иначе - false
   */
  boolean isLoggedIn();
  
  /**
   * Выход из приложения
   */
  void logout();
  
  /**
   * Переключение вкладок.
   *  
   * @param moduleId - Идентификатор вкладки.
   */
  void switchTab(String moduleId);
}
