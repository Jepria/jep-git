package com.technology.jep.jepria.auto.pages;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.technology.jep.jepria.auto.util.WebDriverFactory;

public abstract class PlainPage implements Page {

  public PlainPage() {
    PageFactory.initElements(new DisplayedElementLocatorFactory(10), this);
  }
  
  @Override
  public void ensurePageLoaded() {
    WebDriverFactory.getDriver().switchTo().defaultContent();
    getWait().until(presenceOfElementLocated(By.xpath("//body")));
  }

  @Override
  public Page getContent() {
    WebDriverFactory.getDriver()
        .switchTo()
        .defaultContent();
    return this;
  }
  
  public WebElement waitContentElementToLoad(By by) {
      return getWait().until(presenceOfElementLocated(by));
  }
}
