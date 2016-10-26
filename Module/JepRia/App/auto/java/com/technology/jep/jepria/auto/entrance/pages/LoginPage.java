package com.technology.jep.jepria.auto.entrance.pages;


public interface LoginPage {

  LoginPage setUsername(String username);

  LoginPage setPassword(String password);

  void doLogin();

  LoginPage ensurePageLoaded();
}