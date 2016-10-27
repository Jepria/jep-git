package com.technology.jep.jepria.auto.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;
import com.technology.jep.jepria.auto.entrance.EntranceAutoImpl;
import com.technology.jep.jepria.auto.entrance.EntranceAuto;
import com.technology.jep.jepria.auto.exceptions.AutomationException;
import com.technology.jep.jepria.auto.manager.AutomationManager;
import com.technology.jep.jepria.auto.model.module.Module;
import com.technology.jep.jepria.auto.model.user.User;
import com.technology.jep.jepria.auto.model.user.dao.UserData;
import com.technology.jep.jepria.auto.util.WebDriverFactory;

public abstract class JepApplicationAutoTest<A extends AutomationManager> extends AssertJUnit {
  
  protected JepRiaModuleAuto cut;
  
  protected A automationManager;
  
  /**
   * Интерфейс для осуществления авторизации
   */
  protected EntranceAuto authorizationAuto;
  
  /**
   * Пользователи, которые были созданы и использовались во время тестирования. 
   */
  protected Map<String, User> users = new HashMap<String, User>();
  
  /**
   * "Дефолтный" юзер с логином и паролем из XML
   */
  private User defaultUser;
  
  private String baseUrl;
  
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
  //TODO: Продумать введение новых "кастомных" групп
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
    
    this.baseUrl = baseUrl;
    
    authorizationAuto = new EntranceAutoImpl();
    
    // Создадим "дефолтного" юзера с логином и паролем из XML
    defaultUser = User.fromLoginAndPassword(username, password);
    
    afterSetUp();
  }
  
  protected abstract void afterSetUp();
  
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
  
  protected void loginDefault() {
    login(defaultUser);
  }
  
  protected void login(User user) {
    if (authorizationAuto.isLoggedIn()) {
      authorizationAuto.logout();
    }
    authorizationAuto.login(user.getLogin(), user.getPassword());
  }
  
  protected void logout() {
    if (authorizationAuto.isLoggedIn()) {
      authorizationAuto.logout();
    }
  }

  public void enterModule(Module module){
    
    //вход в нужный модуль приложения для теста
    //TODO: разобраться с baseUrl
    WebDriverFactory.getDriver().get(baseUrl + "/" + module.getEntranceURL());
    
    //установка в cut текущего модуля
    cut = module.getModuleAuto();
  }
  
  /**
   * Получает пользователя для теста.
   * @param dao - DAO, реализующий интерфейс создания пользователя.
   * @param login - Логин.
   * @param rolesNameList - Список ролей.
   * @return Пользователь. {@link com.technology.jep.jepria.auto.model.user.User}
   * @throws Exception 
   */
  public User getUser(UserData dao, String login, List<String> rolesNameList) {
    
    User user = null;
    
    if(users.containsKey(login)){
      user = users.get(login);
    }else{
      try {
        user = dao.createUser(login, rolesNameList);
      } catch (Exception e) {
        throw new AutomationException("Can't create user with role "+rolesNameList+" for test.", e);
      }
      users.put(login, user);
    }
    
    return user;
  }
}
