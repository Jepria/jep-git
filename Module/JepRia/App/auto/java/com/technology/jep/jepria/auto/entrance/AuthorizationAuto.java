package com.technology.jep.jepria.auto.entrance;



/**
 * Базовый интерфейс функциональности доступа
 */
public interface AuthorizationAuto {
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
   * This is a legacy method from AutoBase. Consider useless and remove.
   * TODO
   */
  @Deprecated
  void openMainPage(String url);
}
