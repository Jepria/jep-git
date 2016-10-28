package com.technology.jep.jepria.auto.application.entrance.page;

import com.technology.jep.jepria.auto.page.Page;

/**
 * Интерфейс логин-страницы
 */
public interface LoginPage extends Page {

  void setUsername(String username);

  void setPassword(String password);

  void doLogin();
}