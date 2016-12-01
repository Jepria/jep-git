package com.technology.jep.jepria.auto.application.page;

import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.page.Page;

/**
 * Интерфейс некоторой (любой) страницы <b>уровня приложения</b>, в которое был выполен вход.
 */
public interface JepRiaApplicationPage extends Page {

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
  
  /**
   * Проверка, авторизован ли пользователь.
   * 
   * @return true, если да, иначе false.
   */
  boolean isLoggedIn();
}