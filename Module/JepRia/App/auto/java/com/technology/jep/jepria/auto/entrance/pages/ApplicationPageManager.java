package com.technology.jep.jepria.auto.entrance.pages;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getDriver;
import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;
import com.technology.jep.jepria.auto.pages.JepRiaEntranceApplicationPage;
import com.technology.jep.jepria.auto.pages.PageManagerBase;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;



public class ApplicationPageManager extends PageManagerBase {
  
  private LoginPage loginPage;

  private JepRiaEntranceApplicationPage applicationPage;
  
  
    public JepRiaApplicationPage getApplicationPage() {
      if(applicationPage == null) {
            applicationPage = initElements(JepRiaApplicationPage.getInstance(this));
      }
      
    return (JepRiaApplicationPage) applicationPage;
  }
    
    
    
      
      public LoginPage getLoginPage() {
        if(loginPage == null) {
          try {
            getWait().until(presenceOfElementLocated(By.id("loginForm")));
              WebElement loginForm = getDriver().findElement(By.id("loginForm"));
              assert(loginForm != null);
              
              loginPage = initElements(new DefaultLoginPage());
          } catch (NoSuchElementException ex) {
              getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID)));
              getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.LOGIN_PASSWORD_FIELD_ID)));
                loginPage = initElements(new JepRiaLoginPage());
          }
        }
        
      return loginPage;
    }
}
