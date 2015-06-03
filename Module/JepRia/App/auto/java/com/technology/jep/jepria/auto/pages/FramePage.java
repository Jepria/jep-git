package com.technology.jep.jepria.auto.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class FramePage<P extends PageManagerBase> extends AbstractPage<P> {

    @FindBy(name = "navigation")
    public WebElement navigation;

    @FindBy(name = "content")
    public WebElement content;

    @FindBy(xpath = "//a[contains(@href,'switchLanguage')]")
    private WebElement locale;

    public enum Locale {
        RU, ENG;
    }

    public FramePage(P pages) {
        super(pages);
    }

    @Override
    public FramePage<P> ensurePageLoaded() {
        super.ensurePageLoaded();
        
        getWebDriver().switchTo().defaultContent();
        getWait().until(presenceOfElementLocated(By.xpath("//frameset")));

        if (verifyLocale(Locale.ENG)) {
            return switchLocale().ensurePageLoaded();
        }

        getWebDriver().switchTo().defaultContent();
        return this;
    }

    public FramePage<P> getNavigation() {
        this.getWebDriver()
                .switchTo()
                .defaultContent()
                .switchTo()
                .frame(navigation);
        return this;
    }

    public FramePage<P> getContent() {
        this.getWebDriver()
                .switchTo()
                .defaultContent()
                .switchTo()
                .frame(content);
        return this;
    }
    
    
/*
    public ArrayList<WebElement> waitContentsElementToLoad(By by) {
        getContent().wait.until(presenceOfAllElementsLocatedBy(by));
        return (ArrayList) getContent().wait.until(visibilityOfAllElementsLocatedBy(by));
    }

    public ArrayList<WebElement> waitAllNavElementsToLoad(By by) {
        getNavigation().wait.until(presenceOfAllElementsLocatedBy(by));
        return (ArrayList) getNavigation().wait.until(visibilityOfAllElementsLocatedBy(by));
    }
*/

    public WebElement waitContentElementToLoad(By by) {
        return getWait().until(presenceOfElementLocated(by));
    }

    public boolean verifyLocale(Locale locale) {

        this.getNavigation();

        if (locale.equals(Locale.RU) && this.locale.getText().equals("English")) {
            return true;
        }

        if (locale.equals(Locale.ENG) && this.locale.getText().equals("Русский")) {
            return true;
        }

        return false;
    }

    public FramePage<P> switchLocale() {
        this.ensurePageLoaded().getNavigation().locale.click();
        return this.ensurePageLoaded().getNavigation();
    }
}
