package com.technology.jep.jepria.auto.test;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.technology.jep.jepria.auto.entrance.ApplicationEntranceAuto;
import com.technology.jep.jepria.auto.entrance.EntranceAuto;
import com.technology.jep.jepria.auto.manager.AutomationManager;

public abstract class JepApplicationAutoTest<A extends AutomationManager> {
  
  //
  protected A automationManager;
  
  // Интерфейс для осуществления авторизации
  protected EntranceAuto authorizationAuto;
  /**
   * Конфигурирование теста
   * 
   * @param baseUrl - URL запуска приложения
   * @param browserName - используемый браузер
   * @param browserVersion - версия браузера
   * @param browserPlatform - платформа, для которой реализован браузер
   * @param jepriaVersion - версия JepRia
   * @param forceNewBrowser - условие запуска нового браузера: если true - запускать 
   * @param forceLogin - условие перелогинивания: если true - перелогиниваться
   * @param username - имя пользователя
   * @param password - пароль пользователя
   */
  @Parameters({
    "baseUrl",
    "browserName",
    "browserVersion",
    "browserPlatform",
    "browserPath",
    "driverPath",
    "jepriaVersion",
    "forceNewBrowser",
    "forceLogin",
    "username",
    "password"})
  @BeforeMethod(groups = {"find", "create", "delete", "edit", "goto", "list", "setAndGetFields", "fieldStates"})
  public void setUp(
      String baseUrl,
      String browserName,
      @Optional("fake") String browserVersion,
      @Optional("fake") String browserPlatform,
      String browserPath,
      @Optional String driverPath,
      String jepriaVersion,
      @Optional("No") String forceNewBrowser,
      @Optional("No") String forceLogin,
      String username,
      String password) {
    
    // Создадим новый менеджер
    if (automationManager == null || "Yes".equalsIgnoreCase(forceNewBrowser)) {
      automationManager = provideAutomationManager(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password);
    }
    // Запустим его
    if(!automationManager.isStarted()) {
      automationManager.start(baseUrl);
    }
    
    authorizationAuto = new ApplicationEntranceAuto();
    
    // Сохраним параметры для последующих авторизаций
    this.username = username;
    this.password = password;
  }
  
  private String username, password;
  
  protected abstract A provideAutomationManager(
      String baseUrl,
      String browserName,
      String browserVersion,
      String browserPlatform,
      String browserPath,
      String driverPath,
      String jepriaVersion,
      String username,
      String password);
  
  /**
   * Действия после окончания тестового метода
   * 
   * @param forceNewBrowser - условие запуска нового браузера: если true - запускать 
   * @param forceLogin - условие перелогинивания: если true - перелогиниваться
   */
  @AfterMethod(groups = {"find", "create", "delete", "edit", "goto", "list", "setAndGetFields", "fieldStates"})
  @Parameters({"forceNewBrowser", "forceLogin"})
  public void tearDown(
      @Optional("No") String forceNewBrowser,
      @Optional("No") String forceLogin) {

      if("Yes".equalsIgnoreCase(forceNewBrowser)) {
          automationManager.stop();
      } else {
        if ("Yes".equalsIgnoreCase(forceLogin)) {
          logout();
        } else {
//        TODO: По окончанию теста проверять, что приложение находится в рабочем состоянии (не висит сообщение (алерт) об ошибке)
//        Иначе новый тест не запуститься, возвращать в работоспособное состояние (Переход по ссылке baseUrl?).
//        Считывать сообщение и логировать (выбросить исключение?) 
//        На данный момент, во избежание подобного, приходится перезапускать браузер для каждого нового теста, 
//        используя forceNewBrowser
        }
      }
  }
  
  protected void login() {
    if (authorizationAuto.isLoggedIn()) {
      authorizationAuto.logout();
    }
    authorizationAuto.login(username, password);
  }
  
  protected void logout() {
    if (authorizationAuto.isLoggedIn()) {
      authorizationAuto.logout();
    }
  }
}
