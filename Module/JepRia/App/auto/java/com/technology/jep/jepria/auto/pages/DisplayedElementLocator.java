package com.technology.jep.jepria.auto.pages;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getDriver;

import java.lang.reflect.Field;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AjaxElementLocator;

public class DisplayedElementLocator extends AjaxElementLocator {
    public DisplayedElementLocator(Field field, int timeOutInSeconds) {
        super(getDriver(), field, timeOutInSeconds);
    }
    
    @Override
    protected boolean isElementUsable(WebElement element) {
        return element.isDisplayed();
    }
}
