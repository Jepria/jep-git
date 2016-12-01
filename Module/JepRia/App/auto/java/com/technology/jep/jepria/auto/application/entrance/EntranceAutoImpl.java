package com.technology.jep.jepria.auto.application.entrance;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.application.entrance.page.DefaultLoginPage;
import com.technology.jep.jepria.auto.application.entrance.page.LoginPage;
import com.technology.jep.jepria.auto.application.page.JepRiaApplicationPage;
import com.technology.jep.jepria.auto.application.page.JepRiaApplicationPageImpl;
import com.technology.jep.jepria.auto.exception.AutomationException;

public class EntranceAutoImpl implements EntranceAuto {
  
  /**
   * Логин-страница приложения.<br>
   * <b>Обращаться не напрямую, а только через {@link #getLoginPage()}!</b>
   * В противном случае страница будет неинициализированной.
   */
  protected LoginPage loginPage;
  
  /**
   * Собственно страница приложения.<br>
   * <b>Обращаться не напрямую, а только через {@link #getApplicationPage()}!</b> <br/>
   * В противном случае страница будет неинициализированной.
   */
  protected JepRiaApplicationPage applicationPage;
  
  /**
   * Метод доступа к логин-странице, реализующий её отложенную инициализацию.
   */
  protected LoginPage getLoginPage() {
    if (loginPage == null) {
      loginPage = new DefaultLoginPage();
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

  @Override
  public void login(String username, String password) {
    getLoginPage().ensurePageLoaded();
    
    getLoginPage().setUsername(username);
    getLoginPage().setPassword(password);
    getLoginPage().doLogin();
        
    // wait until the application is loaded after logging in
    getApplicationPage().ensurePageLoaded();
  }

  @Override
  public boolean isLoggedIn() {
    return getApplicationPage().isLoggedIn();
  }

  @Override
  public void logout() {
    getApplicationPage().ensurePageLoaded();
    getApplicationPage().clickLogoutButton();
    
    getLoginPage().ensurePageLoaded();
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
