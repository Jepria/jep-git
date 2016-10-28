package com.technology.jep.jepria.auto.application.entrance.page;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JAVASSO_LOGIN_FORM_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.page.PlainPage;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;

/**
 * Некоторая "дефолтная" реализация интерфейса логин-страницы. 
 */
public class DefaultLoginPage extends PlainPage implements LoginPage {

  @FindBy(id = JepRiaAutomationConstant.JAVASSO_LOGIN_USERNAME_FIELD_ID)
  private WebElement loginField;

  @FindBy(id = JepRiaAutomationConstant.JAVASSO_LOGIN_PASSWORD_FIELD_ID)
  private WebElement pswdField;

  @FindBy(id = "login.registration")
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
    loginButton.click();
  }

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#ensurePageLoaded()
   */
  @Override
  public void ensurePageLoaded() {
    super.ensurePageLoaded();
    super.getContent();

    getWait().until(presenceOfElementLocated(By.xpath(String.format("//form[@name='%s']", JAVASSO_LOGIN_FORM_ID))));
  }
}
