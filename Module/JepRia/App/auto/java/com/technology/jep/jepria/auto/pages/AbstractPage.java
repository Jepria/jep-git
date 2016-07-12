package com.technology.jep.jepria.auto.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import com.technology.jep.jepria.auto.util.WebDriverFactory;

public abstract class AbstractPage<P extends PageManagerBase> {
    protected P pages;

    public AbstractPage(P pages) {
        this.pages = pages;
    }

    public WebDriver getWebDriver() {
        return WebDriverFactory.getDriver();
    }

    public String getTitle() {
        return WebDriverFactory.getDriver().getTitle();
    }

    /**
     * Метод должен быть переопределён потомками
     */
    public AbstractPage<P> ensurePageLoaded() {
      return this;
    }

    public boolean waitPageLoaded() {
        try {
            ensurePageLoaded();
            return true;
        } catch (TimeoutException to) {
            return false;
        }
    }
    
  public boolean isElementPresent(By locator) {
    return WebDriverFactory.getDriver().findElements(locator).size() > 0;
  }

}
