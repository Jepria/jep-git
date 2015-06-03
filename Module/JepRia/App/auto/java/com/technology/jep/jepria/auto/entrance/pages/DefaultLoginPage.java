package com.technology.jep.jepria.auto.entrance.pages;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.pages.PageManagerBase;
import com.technology.jep.jepria.auto.pages.PlainPage;
import com.technology.jep.jepria.client.AutomationConstant;

public class DefaultLoginPage<P extends PageManagerBase> extends PlainPage<P> implements LoginPage<P> {

    public DefaultLoginPage(P pages) {
        super(pages);
    }

    @FindBy(id = AutomationConstant.JAVASSO_LOGIN_USERNAME_INPUT_FIELD_ID)
    private WebElement loginField;

    @FindBy(id = AutomationConstant.JAVASSO_LOGIN_PASSWORD_INPUT_FIELD_ID)
    private WebElement pswdField;

    @FindBy(id = "login.registration")
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
        pswdField.sendKeys(pswd);
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
    public DefaultLoginPage<P> ensurePageLoaded() {
        super.ensurePageLoaded().getContent();

        getWait().until(presenceOfElementLocated(By.xpath("//form[@name='loginForm']")));
        return this;
    }

	@Override
    public DefaultLoginPage<P> getContent() {
        return (DefaultLoginPage<P>) super.getContent();
    }
}
