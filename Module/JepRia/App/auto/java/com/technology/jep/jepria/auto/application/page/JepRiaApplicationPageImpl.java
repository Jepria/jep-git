package com.technology.jep.jepria.auto.application.page;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ENTRANCE_PANEL_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGGED_IN_USER_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGOUT_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.MODULE_TAB_PANEL_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.page.PlainPage;

/**
 * Реализация некоторой (любой) страницы <b>уровня приложения</b>, в которое был выполнен вход.
 */
public class JepRiaApplicationPageImpl extends PlainPage implements JepRiaApplicationPage {
  
  private static Logger logger = Logger.getLogger(JepRiaApplicationPageImpl.class.getName());
    
  // TODO Разобраться с тем, что сейчас страница используется и для незалогиненных состояний приложения
  @FindBy(id = LOGGED_IN_USER_ID)
  private WebElement userName;

  @FindBy(id = LOGOUT_BUTTON_ID)
  private WebElement logoutButton;
  
  @FindBy(id = ENTRANCE_PANEL_ID)
  private WebElement entranceBarPanel;

  @FindBy(id = MODULE_TAB_PANEL_ID)
  private WebElement moduleTabPanel;

  @Override
  public void ensurePageLoaded() {
    super.ensurePageLoaded();
    getWait().until(visibilityOfElementLocated(By.id(LOGGED_IN_USER_ID)));
  }
  
  @Override  
  public String getLoggedInUsername() {
    return userName.getText();
  }

  @Override
  public void clickLogoutButton() {
    logoutButton.click();
  }

  @Override
  public WebElement getModuleTabPanel() {
    return moduleTabPanel;
  }
  
  @Override
  public boolean isLoggedIn() {
    return userName.isDisplayed();
  }
}
