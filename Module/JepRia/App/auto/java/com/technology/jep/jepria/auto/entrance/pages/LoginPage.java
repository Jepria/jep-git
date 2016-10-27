package com.technology.jep.jepria.auto.entrance.pages;

/**
 * Интерфейс логин-страницы
 */
public interface LoginPage {

  LoginPage setUsername(String username);

  LoginPage setPassword(String password);

  void doLogin();
}