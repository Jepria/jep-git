package com.technology.jep.jepriashowcase.auto;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ENTRANCE_PANEL_LOGOUT_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGGED_IN_USER_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGIN_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGIN_PASSWORD_FIELD_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.entrance.pages.LoginPage;
import com.technology.jep.jepria.auto.pages.JepRiaEntranceApplicationPage;
import com.technology.jep.jepriashowcase.custom.client.CustomAutomationConstant;

public class CustomerPage<P extends JepRiaShowcasePageManager> extends JepRiaEntranceApplicationPage<P> implements LoginPage<P> {
  private static Logger logger = Logger.getLogger(CustomerPage.class.getName());

  // Singleton
  static private CustomerPage<JepRiaShowcasePageManager> instance;
  static public CustomerPage<JepRiaShowcasePageManager> getInstance(JepRiaShowcasePageManager pageManager) {
    if(instance == null) {
      instance = new CustomerPage<JepRiaShowcasePageManager>(pageManager);
      logger.info(instance.getClass() + " has created");
    }
    
    return instance;
  }
  
  private CustomerPage(P pages) {
      super(pages);
  }

  @FindBy(id = CustomAutomationConstant.CUSTOM_MAINPAGE_SEARCH_FORM_BUTTON_ID)
  public WebElement searchFormButton;

  @FindBy(id = CustomAutomationConstant.CUSTOM_MAINPAGE_FULLSCREEN_MODULES_BUTTON_ID)
  public WebElement fullScreenModulesButton;

  @FindBy(id = CustomAutomationConstant.CUSTOM_MAINPAGE_ERROR_DIALOG_BUTTON_ID)
  public WebElement errorDialogButton;

  @FindBy(id = CustomAutomationConstant.CUSTOM_MAINPAGE_EMBEDDED_MODULES_BUTTON_ID)
  public WebElement embeddedModulesButton;

  @FindBy(id = CustomAutomationConstant.CUSTOM_MAINPAGE_CURRENT_USER_BUTTON_ID)
  public WebElement currentUserButton;
    
  // ...

  @FindBy(id = LOGIN_USERNAME_FIELD_ID)
  public WebElement usernameField;
  
  @FindBy(id = LOGIN_PASSWORD_FIELD_ID)
  public WebElement passwordField;
  
  @FindBy(id = LOGIN_BUTTON_ID)
  public WebElement loginButton;
  
  @FindBy(id = ENTRANCE_PANEL_LOGOUT_BUTTON_ID)
  public WebElement exitButton;
  
  @FindBy(id = LOGGED_IN_USER_ID)
  public WebElement loggedInUser;
        
    
  @Override
  public CustomerPage<P> ensurePageLoaded() {
//        super.ensurePageLoaded();
      
      getWait().until(presenceOfElementLocated(By.id(CustomAutomationConstant.CUSTOM_MAINPAGE_SEARCH_FORM_BUTTON_ID)));
      getWait().until(presenceOfElementLocated(By.id(CustomAutomationConstant.CUSTOM_MAINPAGE_FULLSCREEN_MODULES_BUTTON_ID)));
      getWait().until(presenceOfElementLocated(By.id(CustomAutomationConstant.CUSTOM_MAINPAGE_ERROR_DIALOG_BUTTON_ID)));
      getWait().until(presenceOfElementLocated(By.id(CustomAutomationConstant.CUSTOM_MAINPAGE_EMBEDDED_MODULES_BUTTON_ID)));
      getWait().until(presenceOfElementLocated(By.id(CustomAutomationConstant.CUSTOM_MAINPAGE_CURRENT_USER_BUTTON_ID)));
        
      return this;
  }

  @Override
  public LoginPage<P> setUsername(String username) {
    usernameField.sendKeys(username);
        return this;
  }

  @Override
  public LoginPage<P> setPassword(String password) {
    passwordField.sendKeys(password);
        return this;
  }

  @Override
  public void doLogin() {
    loginButton.click();
  }

}
