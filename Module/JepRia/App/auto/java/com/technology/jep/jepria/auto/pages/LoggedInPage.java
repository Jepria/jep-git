package com.technology.jep.jepria.auto.pages;

import org.openqa.selenium.WebElement;

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
  
  /**
   * Получить панель табов
   * 
   * @return панель табов.
   */
  WebElement getModuleTabPanel();
}