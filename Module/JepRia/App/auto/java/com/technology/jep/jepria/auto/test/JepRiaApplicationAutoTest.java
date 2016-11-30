package com.technology.jep.jepria.auto.test;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.STATUSBAR_PANEL_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.STATUSBAR_PANEL_MODULE_HTML_ATTR;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.technology.jep.jepria.auto.application.JepRiaApplicationAuto;
import com.technology.jep.jepria.auto.application.entrance.EntranceAuto;
import com.technology.jep.jepria.auto.application.entrance.EntranceAutoImpl;
import com.technology.jep.jepria.auto.exception.AutomationException;
import com.technology.jep.jepria.auto.model.module.ModuleDescription;
import com.technology.jep.jepria.auto.model.user.User;
import com.technology.jep.jepria.auto.model.user.dao.UserDao;
import com.technology.jep.jepria.auto.model.user.dao.UserData;
import com.technology.jep.jepria.auto.module.JepRiaModuleAuto;
import com.technology.jep.jepria.auto.util.WebDriverFactory;

/**
 * Класс, наследники которого содержат тесты приложения.
 *
 * @param <A> Интерфейс данного приложения.
 */
public abstract class JepRiaApplicationAutoTest<A extends JepRiaApplicationAuto> extends AssertJUnit {
  
  /**
   * Class Under Test 
   * Класс, который тестируется в текущий момент.
   */
  private JepRiaModuleAuto cut;
  
  protected A applicationAuto;
  
  /**
   * Интерфейс для осуществления авторизации
   */
  protected EntranceAuto entranceAuto;
  
  /**
   * Пользователи, которые были созданы и использовались во время тестирования. 
   */
  protected Map<String, User> users = new HashMap<String, User>();
  
  /**
   * "Дефолтный" юзер с логином и паролем из XML
   */
  private User defaultUser;
  
  /**
   * Базовый URL, <br/>включает в себя протокол, порт, домен, имя приложения (без слеша на конце).
   * Указывается xml теста (*AutoTest.xml).
   */
  protected String baseUrl;

  /**
   * DAO для создания пользователей.
   */
  private UserData userDao;
  
  /**
   * Получает текущий cut.
   * @return the cut
   */
  @SuppressWarnings("unchecked")
  protected <C extends JepRiaModuleAuto> C getCut() {
    
    if(cut == null){
      throw new AutomationException("Cut is null, use enterModule() method.");
    }
    
    return (C) cut;
  }

  /**
   * Устанавливает cut.
   * @param cut
   */
  protected void setCut(JepRiaModuleAuto cut) {
    this.cut = cut;
  }

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
   * @param dbUrl - URL, по которому подключаемся к DB.
   * @param dbUser - Пользователей, под которым подключаемся к DB.
   * @param dbPassword - Пароль, под которым подключаемся к DB.
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
    "password",
    "dbUrl", 
    "dbUser", 
    "dbPassword"})
  /*
   * Поддержка групп делегирована в прикладные классы тестов. 
   * 
   * Пример работы с группами:
   * 1) В *Test.xml использовать тег define:
   *  <!-- Стандартный набор групп -->
   *  <define name="standard">
   *    <include name="find" />
   *    <include name="create" />
   *    <include name="delete" />
   *    <include name="edit" />
   *    <include name="fieldStates" /> 
   *    <include name="setAndGetFields" />
   *  </define>
   *
   * 2) В прикладаном классе необходимо переопределить setUp и tearDown.
   * @BeforeMethod(groups = {"standard"})
   * public void setUp(...) {
   *  super.setUp(...);
   * }
   */
  @BeforeMethod
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
      String password,
      @Optional String dbUrl, 
      @Optional String dbUser, 
      @Optional String dbPassword) {
    
    // Создадим новый менеджер
    if (applicationAuto == null || "Yes".equalsIgnoreCase(forceNewBrowser)) {
      applicationAuto = provideAutomationManager(baseUrl, browserName, browserVersion, browserPlatform, browserPath,
          driverPath, jepriaVersion, username, password, dbUrl, dbUser, dbPassword);
    }
    // Запустим его
    if(!applicationAuto.isStarted()) {
      applicationAuto.start(baseUrl);
    }
    
    createEntranceAuto();
    
    this.baseUrl = baseUrl;
    
    //"обnullяем" cut перед запуском теста.
    this.cut = null;
    
    // Создадим "дефолтного" юзера с логином и паролем из XML
    defaultUser = User.fromLoginAndPassword(username, password);
    
    // Инициализируем DAO для создания тестовых пользователей
    if(dbUrl != null && dbUser != null && dbPassword != null){
      userDao = new UserDao(dbUrl,
          dbUser,
          dbPassword);
    }
    
    beforeTestLaunch();
  }
  
  /**
   * Метод инстанциирует интерфейс для осуществления авторизации.
   */
  protected void createEntranceAuto(){
    entranceAuto = new EntranceAutoImpl();
  }
  
  /**
   * Метод вызывается в конце {@link #setUp(String, String, String, String, String, String, String, String, String, String, String, String, String, String) setUp},
   * непосредственно перед запуском каждого теста. Например, для того, чтобы в нем осуществлять переход в модуль
   * с помощью {@link #enterModule(ModuleDescription)}.
   * Предназначен для переопределния потомками.  
   */
  protected abstract void beforeTestLaunch();
  
  protected abstract A provideAutomationManager(
      String baseUrl,
      String browserName,
      String browserVersion,
      String browserPlatform,
      String browserPath,
      String driverPath,
      String jepriaVersion,
      String username,
      String password,
      String dbURL, 
      String dbUser, 
      String dbPassword);
  
  /**
   * Действия после окончания тестового метода
   * 
   * @param forceNewBrowser - условие запуска нового браузера: если true - запускать 
   * @param forceLogin - условие перелогинивания: если true - перелогиниваться
   */
  @AfterMethod
  @Parameters({"forceNewBrowser", "forceLogin"})
  public void tearDown(
      @Optional("No") String forceNewBrowser,
      @Optional("No") String forceLogin) {

      if("Yes".equalsIgnoreCase(forceNewBrowser)) {
          applicationAuto.stop();
      } else {
        if ("Yes".equalsIgnoreCase(forceLogin)) {
          logout();
        } else {
//        TODO: По окончанию теста проверять, что приложение находится в рабочем состоянии,
//        не висит сообщение (MESSAGE_BOX, ALERT_BOX или gwt-DialogBox)  
//        иначе новый тест не запуститься, возвращать в работоспособное состояние (Переход по ссылке baseUrl?).
//        Считывать сообщение и логировать (выбросить исключение?) 
//        На данный момент, во избежание подобного, приходится перезапускать браузер для каждого нового теста, 
//        используя forceNewBrowser
        }
      }
  }
  
  /**
   * Метод для входа в приложение под "дефолтным" пользователем (чьи логин и пароль были считаны из XML-сценария тестирования).
   * Если вход в приложение уже выполнен, то выполняется выход, затем вход.
   * После успешного входа метод дожидается полной загрузки страницы.
   */
  protected void loginDefault() {
    login(defaultUser);
  }
  
  /**
   * Метод для входа в приложение под заданным пользователем.
   * Если вход в приложение уже выполнен, то выполняется выход, затем вход.
   * После успешного входа метод дожидается полной загрузки страницы.
   * @param user
   */
  protected void login(User user) {
    if (entranceAuto.isLoggedIn()) {
      entranceAuto.logout();
    }
    entranceAuto.login(user.getLogin(), user.getPassword());
  }
  
  /**
   * Метод для выхода из приложения.
   * Если входа еще не было, ничего не происходит.
   * После успешного выхода метод дожидается полной загрузки логин-страницы.
   */
  protected void logout() {
    if (entranceAuto.isLoggedIn()) {
      entranceAuto.logout();
    }
  }

  /**
   * Вход в модуль для прохождения теста.
   * @param module - Модуль.
   */
  public void enterModule(ModuleDescription<?> module) {
    
    //вход в приложения, в модуль, для теста
    //baseUrl - заканчивается именем приложения, без /
    WebDriverFactory.getDriver().get(baseUrl + "/" + module.getEntranceURL());

    //установка в cut текущего модуля
    setCut(module.getModuleAuto());
  }
  
  /**
   * Клик по вкладке на панеле модулей.
   * @param module - Модуль.
   */
  public void switchTab(ModuleDescription<?> module){
    // Переключим вкладку
    entranceAuto.switchTab(module.getModuleID());
    
    // Установим новый cut и дождемся его загрузки
    setCut(module.getModuleAuto());
    
    // Ждем, пока новый модуль отобразится (признаком является атрибут статусбара)
    WebDriverFactory.getWait().until(presenceOfElementLocated(By.xpath(
        String.format("//*[@id='%s' and @%s='%s']",
            STATUSBAR_PANEL_ID,
            STATUSBAR_PANEL_MODULE_HTML_ATTR,
            module.getModuleID()))));
  }
  
  /**
   * Получает пользователя для теста.
   * @param login - Логин.
   * @param rolesNameList - Список ролей.
   * @return Пользователь. {@link com.technology.jep.jepria.auto.model.user.User}
   * @throws Exception 
   */
  public User getUser(String login, List<String> rolesNameList) {
    
    User user = null;
    
    if(users.containsKey(login)){
      user = users.get(login);
    }else{
      
      if(userDao == null){
        throw new AutomationException("User DAO is null.  Check dbUrl, dbUser, dbPassword in test.properties.");
      }
      
      try {
        user = userDao.createUser(login, rolesNameList);
      } catch (Exception e) {
        throw new AutomationException("Can't create user with role "+rolesNameList+" for test.", e);
      }
      users.put(login, user);
    }
    
    return user;
  }
}
