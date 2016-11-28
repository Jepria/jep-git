package com.technology.jep.jepria.auto.application.entrance;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JAVASSO_LOGIN_FORM_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGGED_IN_USER_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.application.entrance.page.DefaultLoginPage;
import com.technology.jep.jepria.auto.application.entrance.page.JepRiaLoginPage;
import com.technology.jep.jepria.auto.application.entrance.page.LoginPage;
import com.technology.jep.jepria.auto.application.page.JepRiaApplicationPage;
import com.technology.jep.jepria.auto.application.page.JepRiaApplicationPageImpl;
import com.technology.jep.jepria.auto.condition.ConditionChecker;
import com.technology.jep.jepria.auto.condition.DisplayChecker;
import com.technology.jep.jepria.auto.condition.ExpectedConditions;
import com.technology.jep.jepria.auto.exception.AutomationException;
import com.technology.jep.jepria.auto.util.WebDriverFactory;

public class EntranceAutoImpl implements EntranceAuto {
  
  /**
   * Логин-страница приложения.<br>
   * <b>Обращаться не напрямую, а только через {@link #getLoginPage()}!</b>
   * В противном случае страница будет неинициализированной.
   */
  private LoginPage loginPage;
  
  /**
   * Собственно страница приложения.<br>
   * <b>Обращаться не напрямую, а только через {@link #getApplicationPage()}!</b> <br/>
   * В противном случае страница будет неинициализированной.
   */
  protected JepRiaApplicationPage applicationPage;
  
  /**
   * Метод доступа к логин-странице, реализующий её отложенную инициализацию.
   */
  private LoginPage getLoginPage() {
    if (loginPage == null) {
      determineLoggedState();
    }
    return loginPage;
  }
  
  /**
   * Метод доступа к странице уровня абстракции приложения. 
   */
  protected JepRiaApplicationPage getApplicationPage() {
    if(applicationPage == null){
      applicationPage = new JepRiaApplicationPageImpl();
    }
    return applicationPage;
  }
  
  /*
   * Константы для оптимизации ожидания реакции на login/logout
   * Чтобы ожидание появления приложения/страницы логин можно было отложить 
   */
  private static final int LAST_ENTRANCE_OPERATION_LOGIN = 1;
  private static final int LAST_ENTRANCE_OPERATION_LOGOUT = 2;
  private int lastEntranceOperation;

  @Override
  public void login(String username, String password) {
    if(!isLoggedIn()) {
      getLoginPage().ensurePageLoaded();
      
      getLoginPage().setUsername(username);
      getLoginPage().setPassword(password);
      getLoginPage().doLogin();
          
      lastEntranceOperation = LAST_ENTRANCE_OPERATION_LOGIN;
      
      // wait until the application is loaded after logging in
      getApplicationPage().ensurePageLoaded();
    }
  }

  @Override
  public boolean isLoggedIn() {
    if (lastEntranceOperation == LAST_ENTRANCE_OPERATION_LOGIN) {
      return true;
    } else if (lastEntranceOperation == LAST_ENTRANCE_OPERATION_LOGOUT) {
      return false;
    } else {
      return determineLoggedState();
    }
  }

  @Override
  public void logout() {
    if (isLoggedIn()) {
      getApplicationPage().ensurePageLoaded();
      getApplicationPage().clickLogoutButton();
          
      lastEntranceOperation = LAST_ENTRANCE_OPERATION_LOGOUT;
      
      getLoginPage().ensurePageLoaded();
    }
  }
  
  /**
   * Метод определяет состояние приложения (залогинено или нет) по трём элементам:
   * имя залогиненного пользователя, JAVASSO-логин форме, либо по логин-полю.
   * В случае, если найдена одна из двух логин-форм, поле {@link #loginPage} инициализируется.
   * 
   * @return <code>true</code>, если в приложение выполнен вход; <code>false</code> иначе (даже в случае ошибки).
   */
  private boolean determineLoggedState() {
    // Ничего не известно о состоянии приложения (вход выполнен или нет).

    WebDriver wd = WebDriverFactory.getDriver();
    final ConditionChecker conditionChecker;
    
    ConditionChecker javassoChecker = new DisplayChecker(wd, JAVASSO_LOGIN_FORM_ID);
    ConditionChecker loginUsernameChecker = new DisplayChecker(wd, LOGIN_USERNAME_FIELD_ID);
    ConditionChecker loggedInChecker = new DisplayChecker(wd, LOGGED_IN_USER_ID);
    
    if (lastEntranceOperation == LAST_ENTRANCE_OPERATION_LOGOUT) {
      // Только что была нажата кнопка выхода, поэтому дожидаемся появления любого из двух элементов:
      // JAVASSO-логин форму (стандартную), либо логин-поле некоторой кастомной логин-формы.
      conditionChecker = WebDriverFactory.getWait().until(
          ExpectedConditions.atLeastOneOfConditionIsSatisfied(
              javassoChecker, loginUsernameChecker)
      );
    } else {
      // Попробуем лоцировать любой из трёх элементов: имя залогиненного пользователя,
      // либо JAVASSO-логин форму (стандартную), либо логин-поле некоторой кастомной логин-формы.
      conditionChecker = WebDriverFactory.getWait().until(
          ExpectedConditions.atLeastOneOfConditionIsSatisfied(
              loggedInChecker, javassoChecker, loginUsernameChecker)
      );
    }
    
    if (conditionChecker == loggedInChecker) {
      // Найден залогиненный пользователь
      return true;
    } else if (conditionChecker == javassoChecker) {
      loginPage = new DefaultLoginPage();
      return false;
    } else {
      loginPage = new JepRiaLoginPage();
      return false;
    }
//    TODO WebDriverWait might throw TimeOutException...
//    
//    } catch (Exception e) {
//      throw new NoSuchElementException("Expected to locate any of three elements: X X X, but none of them loacted.", e);
//    }
//    // Условно считаем, что при возникновении ошибки - состояние=незалогинено.
//    return false;
  }
  
  @Override
  public void switchTab(String moduleId) {
    
    try{
        WebElement moduleTab = getApplicationPage().getModuleTabPanel().findElement(By.id(moduleId));
        getWait().until(elementToBeClickable(moduleTab));
        moduleTab.click();
     } catch(NoSuchElementException e){
       throw new AutomationException("Can't click " + moduleId + " module tab.", e);
     } 
  }
}
