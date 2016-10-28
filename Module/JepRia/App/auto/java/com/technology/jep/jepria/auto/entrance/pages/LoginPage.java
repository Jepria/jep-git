package com.technology.jep.jepria.auto.entrance.pages;

import com.technology.jep.jepria.auto.pages.Page;

/**
 * Интерфейс логин-страницы
 */
public interface LoginPage extends Page {

  void setUsername(String username);

  void setPassword(String password);

  void doLogin();
}