package com.technology.jep.jepria.auto.pages;

import org.openqa.selenium.support.PageFactory;


public abstract class PageManagerBase {

    protected <T extends AbstractPage<?>> T initElements(T page) {
        //PageFactory.initElements(driver, page);
        //PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), page);
        PageFactory.initElements(new DisplayedElementLocatorFactory(10), page);
        return page;
    }
}
