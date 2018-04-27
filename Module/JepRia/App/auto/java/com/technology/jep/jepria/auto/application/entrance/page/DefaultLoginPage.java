package com.technology.jep.jepria.auto.application.entrance.page;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGIN_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGIN_PASSWORD_FIELD_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.condition.ExpectedConditions;
import com.technology.jep.jepria.auto.page.PlainPage;

/**
 * Некоторая "дефолтная" реализация интерфейса логин-страницы. 
 */
public class DefaultLoginPage extends PlainPage implements LoginPage {

  @FindBy(id = LOGIN_USERNAME_FIELD_ID)
  private WebElement loginField;

  @FindBy(id = LOGIN_PASSWORD_FIELD_ID)
  private WebElement pswdField;

  @FindBy(id = LOGIN_BUTTON_ID)
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
    pswdField.sendKeys(pswd);
  }

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#doLogin()
   */
  @Override
  public void doLogin() {
    getWait().until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(By.id("loadingProgress")));
    getWait().until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(loginButton)).click();
  }

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#ensurePageLoaded()
   */
  @Override
  public void ensurePageLoaded() {
    super.ensurePageLoaded();
    super.getContent();

    getWait().until(ExpectedConditions.allElementsLocatedVisible(
        By.id(LOGIN_USERNAME_FIELD_ID),
        By.id(LOGIN_PASSWORD_FIELD_ID),
        By.id(LOGIN_BUTTON_ID)));
  }
}
