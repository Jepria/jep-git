package com.technology.jep.jepria.auto.entrance.pages;

import com.technology.jep.jepria.auto.pages.Page;

/**
 * Интерфейс логин-страницы
 */
public interface LoginPage extends Page {

  LoginPage setUsername(String username);

  LoginPage setPassword(String password);

  void doLogin();
}