package com.technology.jep.jepria.auto.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class PlainPage<P extends PageManagerBase> extends AbstractPage<P> {

    public PlainPage(P pages) {
        super(pages);
    }

    @Override
    public PlainPage<P> ensurePageLoaded() {
        super.ensurePageLoaded();
        
        getWebDriver().switchTo().defaultContent();
        getWait().until(presenceOfElementLocated(By.xpath("//body")));
        return this;
    }

    public PlainPage<P> getContent() {
        this.getWebDriver()
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
