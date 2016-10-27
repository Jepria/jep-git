package com.technology.jep.jepria.auto.pages;

/**
 * Интерфейс любой страницы приложения, в которое был выполен вход.  
 */
public interface LoggedInPage {

  /**
   * Получение username залогиненного пользователя
   * 
   * @return username
   */
  String getLoggedInUsername();

  /**
   * Click кнопки выхода
   */
  void clickLogoutButton();
}