package com.technology.jep.jepria.auto.application.entrance.page;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.page.PlainPage;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;

/**
 * Реализация стандартной для JepRia логин-страницы. 
 */
public class JepRiaLoginPage extends PlainPage implements LoginPage {

  @FindBy(id = JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID)
  private WebElement loginField;

  @FindBy(id = JepRiaAutomationConstant.LOGIN_PASSWORD_FIELD_ID)
  private WebElement passwordField;

  @FindBy(id = JepRiaAutomationConstant.LOGIN_BUTTON_ID)
  private WebElement loginButton;

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#setUsername(java.lang.String)
   */
  @Override
  public void setUsername(String login) {
    getContent();
    loginField.sendKeys(login);
  }

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#setPassword(java.lang.String)
   */
  @Override
  public void setPassword(String pswd) {
    passwordField.sendKeys(pswd);
  }

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#doLogin()
   */
  @Override
  public void doLogin() {
    loginButton.click();
  }

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#ensurePageLoaded()
   */
  @Override
  public void ensurePageLoaded() {
    super.ensurePageLoaded();
    getContent();
        
    getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID)));
    getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.LOGIN_PASSWORD_FIELD_ID)));
  }
}
