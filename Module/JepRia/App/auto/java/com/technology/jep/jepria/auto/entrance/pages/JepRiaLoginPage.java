package com.technology.jep.jepria.auto.entrance.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.pages.PageManagerBase;
import com.technology.jep.jepria.auto.pages.PlainPage;
import com.technology.jep.jepria.client.AutomationConstant;

public class JepRiaLoginPage<P extends PageManagerBase> extends PlainPage<P> implements LoginPage<P> {

    public JepRiaLoginPage(P pages) {
        super(pages);
    }

    @FindBy(id = AutomationConstant.LOGIN_USERNAME_INPUT_FIELD_ID)
    private WebElement loginField;

    @FindBy(id = AutomationConstant.LOGIN_PASSWORD_INPUT_FIELD_ID)
    private WebElement passwordField;

    @FindBy(id = AutomationConstant.LOGIN_BUTTON_ID)
    private WebElement loginButton;

    /* (non-Javadoc)
	 * @see com.technology.jep.auto.entrance.pages.LoginPage#setUsername(java.lang.String)
	 */
    @Override
	public LoginPage<P> setUsername(String login) {
        getContent().loginField.sendKeys(login);
        return this;
    }

    /* (non-Javadoc)
	 * @see com.technology.jep.auto.entrance.pages.LoginPage#setPassword(java.lang.String)
	 */
    @Override
	public LoginPage<P> setPassword(String pswd) {
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
    public JepRiaLoginPage<P> ensurePageLoaded() {
        super.ensurePageLoaded().getContent();
        
        getWait().until(presenceOfElementLocated(By.id(AutomationConstant.LOGIN_USERNAME_INPUT_FIELD_ID)));
        getWait().until(presenceOfElementLocated(By.id(AutomationConstant.LOGIN_PASSWORD_INPUT_FIELD_ID)));
        
        return this;
    }

    @Override
    public JepRiaLoginPage<P> getContent() {
        return (JepRiaLoginPage<P>) super.getContent();
    }
}
