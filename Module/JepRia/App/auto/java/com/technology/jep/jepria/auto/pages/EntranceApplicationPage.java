package com.technology.jep.jepria.auto.pages;


public interface EntranceApplicationPage {

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