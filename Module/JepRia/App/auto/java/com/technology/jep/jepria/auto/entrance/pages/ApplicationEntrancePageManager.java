package com.technology.jep.jepria.auto.entrance.pages;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getDriver;
import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.pages.JepRiaEntranceApplicationPage;
import com.technology.jep.jepria.auto.pages.PageManagerBase;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;

/**
 * TODO Нужна ли такая унификация ?
 */
public class ApplicationEntrancePageManager extends PageManagerBase {
  private static Logger logger = Logger.getLogger(ApplicationEntrancePageManager.class.getName());  
  private LoginPage<ApplicationEntrancePageManager> loginPage;

  protected JepRiaEntranceApplicationPage<PageManagerBase> applicationPage;
  
    public JepRiaEntranceApplicationPage<PageManagerBase> getApplicationPage() {
      if(applicationPage == null) {
            applicationPage = initElements(JepRiaEntranceApplicationPage.getInstance(this));
      }
      
      logger.info(this.getClass() + ": applicationPage has created");
    return applicationPage;
  }
    
    public LoginPage<ApplicationEntrancePageManager> getLoginPage() {
      if(loginPage == null) {
        try {
          getWait().until(presenceOfElementLocated(By.id("loginForm")));
            WebElement loginForm = getDriver().findElement(By.id("loginForm"));
            assert(loginForm != null);
            
            loginPage = initElements(new DefaultLoginPage<ApplicationEntrancePageManager>(this));
        } catch (NoSuchElementException ex) {
            getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID)));
            getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.LOGIN_PASSWORD_FIELD_ID)));
              loginPage = initElements(new JepRiaLoginPage<ApplicationEntrancePageManager>(this));
        }
      }
      
    return loginPage;
  }
}
