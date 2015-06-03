package com.technology.jep.jepria.auto.pages;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.AutomationConstant.LOGGED_IN_USER_ID;
import static com.technology.jep.jepria.client.AutomationConstant.LOGOUT_BUTTON_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class JepRiaEntranceApplicationPage<M extends PageManagerBase> extends PlainPage<M> implements EntranceApplicationPage<M> {
	private static Logger logger = Logger.getLogger(JepRiaEntranceApplicationPage.class.getName());
    
    // TODO Разобраться с тем, что сейчас страница используется и для незалогиненных состояний приложения
    @FindBy(id = LOGGED_IN_USER_ID)
    private WebElement userName;

    @FindBy(id = LOGOUT_BUTTON_ID)
    private WebElement logoutButton;

    
    // Singleton
    static protected JepRiaEntranceApplicationPage<PageManagerBase> instance;
    static public JepRiaEntranceApplicationPage<PageManagerBase> getInstance(PageManagerBase pageManager) {
    	if(instance == null) {
    		instance = new JepRiaEntranceApplicationPage<PageManagerBase>(pageManager);
    	}
    	
    	return instance;
    }

    protected JepRiaEntranceApplicationPage(M pages) {
        super(pages);
    }
    
    @Override
    public JepRiaEntranceApplicationPage<M> ensurePageLoaded() {
        super.ensurePageLoaded();
        
        getWait().until(visibilityOfElementLocated(By.id(LOGGED_IN_USER_ID)));
        
        return this;
    }
    
	public String getLoggedInUsername() {
		return userName.getText();
	}

	public void clickLogoutButton() {
		logoutButton.click();
		logger.trace(this.getClass() + "clickLogoutButton()");
	}
	
}
