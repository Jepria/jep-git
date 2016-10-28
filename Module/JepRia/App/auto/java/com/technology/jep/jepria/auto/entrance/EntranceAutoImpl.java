package com.technology.jep.jepria.auto.entrance;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JAVASSO_LOGIN_FORM_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.entrance.pages.DefaultLoginPage;
import com.technology.jep.jepria.auto.entrance.pages.JepRiaLoginPage;
import com.technology.jep.jepria.auto.entrance.pages.LoginPage;
import com.technology.jep.jepria.auto.exceptions.AutomationException;
import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;
import com.technology.jep.jepria.auto.pages.AbstractApplicationPage;
import com.technology.jep.jepria.auto.util.WebDriverFactory;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;

public class EntranceAutoImpl implements EntranceAuto {
  
  /**
   * Логин-страница приложения.<br>
   * <b>Обращаться не напрямую, а только через {@link #getLoginPage()}!</b> 
   */
  private LoginPage loginPage;
  
  /**
   * Собственно страница приложения.<br>
   * <b>Обращаться не напрямую, а только через {@link #getApplicationPage()}!</b> 
   */
  private AbstractApplicationPage applicationPage;
  
  /**
   * Метод доступа к логин-странице, реализующий её отложенную инициализацию.
   */
  private LoginPage getLoginPage() {
    if (loginPage == null) {
      try {
        getWait().until(presenceOfElementLocated(By.id(JAVASSO_LOGIN_FORM_ID)));
        WebElement loginForm = WebDriverFactory.getDriver().findElement(By.id(JAVASSO_LOGIN_FORM_ID));
        assert(loginForm != null);
        
        loginPage = new DefaultLoginPage();
      } catch (NoSuchElementException ex) {
        getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID)));
        getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.LOGIN_PASSWORD_FIELD_ID)));
        
        loginPage = new JepRiaLoginPage();
      }
    }
    return loginPage;
  }
  
  /**
   * Метод доступа к странице приложения, реализующий её отложенную инициализацию.
   */
  private AbstractApplicationPage getApplicationPage() {
    if (applicationPage == null) {
      this.applicationPage = new JepRiaApplicationPage();
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
      // В данном тесте еще не было ни логина, ни логаута. С вероятностью 99%, входим под уже залогиненным пользователем // TODO а если нет?
      try {
        getApplicationPage().ensurePageLoaded();
        WebElement usernameField = WebDriverFactory.getDriver().findElement(By.id(JepRiaAutomationConstant.LOGGED_IN_USER_ID));
        return usernameField.isDisplayed(); 
      } catch (Exception ex) {
        return false;
      }
    }
  }

  @Override
  public void logout() {
    if (isLoggedIn()) {
      getApplicationPage().ensurePageLoaded();
      getApplicationPage().clickLogoutButton();
          
      lastEntranceOperation = LAST_ENTRANCE_OPERATION_LOGOUT;
      
      // wait until the login page is loaded after logging out
      getLoginPage().ensurePageLoaded();
    }
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
