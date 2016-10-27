package com.technology.jep.jepria.auto.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;

import com.technology.jep.jepria.auto.util.WebDriverFactory;

public abstract class AbstractPage {
  
  public AbstractPage() {
    PageFactory.initElements(new DisplayedElementLocatorFactory(10), this);
  }
  
  public String getTitle() {
    return WebDriverFactory.getDriver().getTitle();
  }

  /**
   * Проверка, загрузилась ли страница
   */
  public abstract AbstractPage ensurePageLoaded();

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
