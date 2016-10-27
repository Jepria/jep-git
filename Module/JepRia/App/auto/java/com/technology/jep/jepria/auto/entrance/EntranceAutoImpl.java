package com.technology.jep.jepria.auto.entrance;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JAVASSO_LOGIN_FORM_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.entrance.pages.DefaultLoginPage;
import com.technology.jep.jepria.auto.entrance.pages.JepRiaLoginPage;
import com.technology.jep.jepria.auto.entrance.pages.LoginPage;
import com.technology.jep.jepria.auto.exceptions.AutomationException;
import com.technology.jep.jepria.auto.pages.AbstractPage;
import com.technology.jep.jepria.auto.pages.JepRiaLoggedInPage;
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
  private JepRiaLoggedInPage applicationPage;
  
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
  private JepRiaLoggedInPage getApplicationPage() {
    if (applicationPage == null) {
      this.applicationPage = new JepRiaLoggedInPage();
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
    if(lastEntranceOperation != LAST_ENTRANCE_OPERATION_LOGIN) {
      ((AbstractPage)getLoginPage()).ensurePageLoaded();
      
      getLoginPage()
          .setUsername(username)
          .setPassword(password)
          .doLogin();
          
      lastEntranceOperation = LAST_ENTRANCE_OPERATION_LOGIN;
    }
  }

  @Override
  public boolean isLoggedIn() {
    boolean result = false;
    switch (lastEntranceOperation) {
    case LAST_ENTRANCE_OPERATION_LOGIN:
      try {
        getApplicationPage()
            .ensurePageLoaded();
            result = true;
      } catch (NoSuchElementException e) {
        System.out.println("[NoSuchElementException] login page not loaded, " + e.toString());
        result = false; 
      }
      break;
    case LAST_ENTRANCE_OPERATION_LOGOUT:
      try {
        ((AbstractPage)getLoginPage()).ensurePageLoaded();//TODO do not cast
      } catch (NoSuchElementException e) {
        System.out.println("[NoSuchElementException] login page not loaded, " + e.toString());
        result = true; 
      }
      result = false;
      break;
    default:
      // Проверка первого входа на уже залогиненную страницу
      try {
        WebElement usernameField = WebDriverFactory.getDriver().findElement(By.id(JepRiaAutomationConstant.LOGGED_IN_USER_ID));
        result = usernameField.isDisplayed(); 
      } catch (Exception ex) {
        result = false;
      }
      
      break;
    }    
      
    return result;
  }

  @Override
  public void logout() {
    getApplicationPage()
        .ensurePageLoaded()
        .clickLogoutButton();
        
    lastEntranceOperation = LAST_ENTRANCE_OPERATION_LOGOUT;
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
