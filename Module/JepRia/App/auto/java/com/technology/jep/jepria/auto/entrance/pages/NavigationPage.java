package com.technology.jep.jepria.auto.entrance.pages;

import org.openqa.selenium.By;

import com.technology.jep.jepria.auto.pages.FramePage;

public class NavigationPage<M extends NavigationEntrancePageManager> extends FramePage<M> {

    public NavigationPage(M pages) {
        super(pages);
    }

//    @FindBy(className = "x-entrance-bar-panel")
//    private WebElement entranceBarPanel;
//
//    @FindBy(id = "x-auto-7") // TODO Сделать нормальный локатор
//    private WebElement entranceBarLogoutButton;
      
    // Singleton
    static private NavigationPage<NavigationEntrancePageManager> instance;
    static public NavigationPage<NavigationEntrancePageManager> getInstance(NavigationEntrancePageManager pageManager) {
    	if(instance == null) {
    		instance = new NavigationPage<NavigationEntrancePageManager>(pageManager);
    	}
    	
    	return instance;
    }

    @Override
    public NavigationPage<M> getContent() {
        return (NavigationPage<M>) super.getContent();
    }

    @Override
    public NavigationPage<M> ensurePageLoaded() {
        super.ensurePageLoaded(); // TODO ...
//        super.ensurePageLoaded().getContent();
//        wait.until(presenceOfElementLocated(By.xpath("//form[@name='loginForm']")));
        return this;
//        throw new NotImplementedYetException();
    }

//    @Override
//    public DefaultLoginPage ensurePageLoaded() {
//        super.ensurePageLoaded().getContent();
//        wait.until(presenceOfElementLocated(By.xpath("//form[@name='loginForm']")));
//        return this;
//    }
    
    
	public boolean isChangePasswordNodeDisplayed() {
        return this.getNavigation().isElementPresent(By.xpath("//a[@title='Выход']"));        
	}

//	public void clickLogoutButton() {
//		getContent().entranceBarLogoutButton.click();;
//	}
}
