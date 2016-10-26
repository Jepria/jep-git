package com.technology.jep.jepria.auto.entrance;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.technology.jep.jepria.auto.HasText;
import com.technology.jep.jepria.auto.entrance.pages.ApplicationPageManager;
import com.technology.jep.jepria.auto.manager.HasWebDriver;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;

public class TODO_ModuleAuto<A extends HasWebDriver, P extends ApplicationPageManager> implements AuthorizationAuto {
  
  // fields from former AutoBaseImpl
  protected HasWebDriver applicationManager;
  protected P pages;
  
  /*
   * Константы для оптимизации ожидания реакции на login/logout
   * Чтобы ожидание появления приложения/страницы логин можно было отложить 
   */
    private static final int LOGIN_LAST_ENTRANCE_OPERATION = 1;
    private static final int LOGOUT_LAST_ENTRANCE_OPERATION = 2;
  private int lastEntranceOperation;

  public TODO_ModuleAuto(A app, P pageManager) {
    this.applicationManager = app;
    this.pages = pageManager;
  }

  protected void waitTextToBeChanged(HasText hasText, String currentWorkstateDisplayText) {
    
    getWait()
        .until(
            textToBeChangedInElementLocated(By.id(JepRiaAutomationConstant.STATUSBAR_PANEL_ID),
                currentWorkstateDisplayText));
  }
  private static ExpectedCondition<Boolean> textToBeChangedInElementLocated(final By locator, final String currentText) {

    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        try {
          String elementText = driver.findElement(locator).getText();
          if(currentText != null) {
            return !currentText.equals(elementText);
          } else {
            return elementText != null;
          }
        } catch (StaleElementReferenceException e) {
          return null;
        }
      }

      @Override
      public String toString() {
        return String.format("text ('%s') to be present in element found by %s", currentText, locator);
      }
    };
  }
  
  @Override
  public void login(String username, String password) {
    if(lastEntranceOperation != LOGIN_LAST_ENTRANCE_OPERATION) {
      pages.getLoginPage()
          .ensurePageLoaded()
          .setUsername(username)
          .setPassword(password)
          .doLogin();
          
          lastEntranceOperation = LOGIN_LAST_ENTRANCE_OPERATION;
    }
  }


  @Override
  public boolean isLoggedIn() {
    boolean result = false;
    switch (lastEntranceOperation) {
    case LOGIN_LAST_ENTRANCE_OPERATION:
      try {
        pages
          .getApplicationPage()
          .ensurePageLoaded();
        result = true;
          } catch (NoSuchElementException e) {
              System.out.println("[NoSuchElementException] login page not loaded, " + e.toString());
        result = false; 
          }
      break;
    case LOGOUT_LAST_ENTRANCE_OPERATION:
      try {
        pages.getLoginPage()
        .ensurePageLoaded();
          } catch (NoSuchElementException e) {
              System.out.println("[NoSuchElementException] login page not loaded, " + e.toString());
        result = true; 
          }
      result = false;
      break;
    default:
      // Проверка первого входа на уже залогиненную страницу
      try {
        WebElement usernameField = applicationManager.getWebDriver().findElement(By.id(JepRiaAutomationConstant.LOGGED_IN_USER_ID));
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
    pages
      .getApplicationPage()
          .ensurePageLoaded()
//          .getContent()
          .clickLogoutButton();
        
        lastEntranceOperation = LOGOUT_LAST_ENTRANCE_OPERATION;
  }
  
  @Deprecated
  @Override
  public void openMainPage(String url) {
    applicationManager.getWebDriver().get(url);
  }
}
