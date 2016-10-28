package com.technology.jep.jepria.auto.pages;

import org.openqa.selenium.WebElement;

/**
 * Интерфейс любой страницы <b>уровня приложения</b>, в которое был выполен вход.
 */
public interface AbstractApplicationPage extends Page {

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