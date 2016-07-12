package com.technology.jep.jepria.auto.entrance.pages;

import com.technology.jep.jepria.auto.pages.PageManagerBase;

public interface LoginPage<P extends PageManagerBase> {

  LoginPage<P> setUsername(String username);

  LoginPage<P> setPassword(String password);

  void doLogin();

  LoginPage<P> ensurePageLoaded();
}