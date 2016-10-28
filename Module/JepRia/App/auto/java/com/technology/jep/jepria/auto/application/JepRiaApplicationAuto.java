package com.technology.jep.jepria.auto.application;

public interface JepRiaApplicationAuto {

  /**
   * Старт приложения (открытие страницы браузера)
   */
  void start(String baseUrl);

  /**
   * Остановка приложения (закрытие страницы браузера)
   */
  void stop();


  /**
   * Возвращает true, если приложение стартовано, иначе - false
   */
  boolean isStarted();
}