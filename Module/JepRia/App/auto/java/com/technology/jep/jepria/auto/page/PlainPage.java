package com.technology.jep.jepria.auto.page;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getDriver;
import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.lang.reflect.Field;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

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
  public void getContent() {
    WebDriverFactory.getDriver()
        .switchTo()
        .defaultContent();
  }
  
  public WebElement waitContentElementToLoad(By by) {
      return getWait().until(presenceOfElementLocated(by));
  }
  
  private class DisplayedElementLocatorFactory implements ElementLocatorFactory {
    private final int timeOutInSeconds;

    public DisplayedElementLocatorFactory(int timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }

    @Override
    public ElementLocator createLocator(Field field) {
      return new AjaxElementLocator(getDriver(), field, timeOutInSeconds) {
        @Override
        protected boolean isElementUsable(WebElement element) {
            return element.isDisplayed();
        }
      };
    }
  }
}
