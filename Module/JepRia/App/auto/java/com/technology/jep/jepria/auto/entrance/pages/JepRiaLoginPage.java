package com.technology.jep.jepria.auto.entrance.pages;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.pages.PlainPage;
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
  public LoginPage setUsername(String login) {
        getContent().loginField.sendKeys(login);
        return this;
    }

    /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#setPassword(java.lang.String)
   */
    @Override
  public LoginPage setPassword(String pswd) {
        passwordField.sendKeys(pswd);
        return this;
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
    public JepRiaLoginPage ensurePageLoaded() {
        super.ensurePageLoaded().getContent();
        
        getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID)));
        getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.LOGIN_PASSWORD_FIELD_ID)));
        
        return this;
    }

    @Override
    public JepRiaLoginPage getContent() {
        return (JepRiaLoginPage) super.getContent();
    }
}
