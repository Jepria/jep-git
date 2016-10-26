package com.technology.jep.jepria.auto.pages;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.util.WebDriverFactory;

public abstract class PlainPage extends AbstractPage {

    @Override
    public PlainPage ensurePageLoaded() {
        super.ensurePageLoaded();
        
        WebDriverFactory.getDriver().switchTo().defaultContent();
        getWait().until(presenceOfElementLocated(By.xpath("//body")));
        return this;
    }

    public PlainPage getContent() {
      WebDriverFactory.getDriver()
                .switchTo()
                .defaultContent();
//                .switchTo()
//                .frame(content);
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
}
