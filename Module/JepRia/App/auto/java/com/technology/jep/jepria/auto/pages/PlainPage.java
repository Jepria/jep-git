package com.technology.jep.jepria.auto.pages;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.util.WebDriverFactory;

public abstract class PlainPage extends AbstractPage {

  @Override
  public PlainPage ensurePageLoaded() {
    WebDriverFactory.getDriver().switchTo().defaultContent();
    getWait().until(presenceOfElementLocated(By.xpath("//body")));
    return this;
  }

  public PlainPage getContent() {
    WebDriverFactory.getDriver()
        .switchTo()
        .defaultContent();
    return this;
  }
  
  public WebElement waitContentElementToLoad(By by) {
      return getWait().until(presenceOfElementLocated(by));
  }
}
