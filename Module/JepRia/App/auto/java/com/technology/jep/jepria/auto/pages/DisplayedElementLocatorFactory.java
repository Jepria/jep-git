package com.technology.jep.jepria.auto.pages;

import java.lang.reflect.Field;

import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class DisplayedElementLocatorFactory implements ElementLocatorFactory {
        private final int timeOutInSeconds;

        public DisplayedElementLocatorFactory(int timeOutInSeconds) {
            this.timeOutInSeconds = timeOutInSeconds;
        }

    @Override
    public ElementLocator createLocator(Field field) {
        return new DisplayedElementLocator(field, timeOutInSeconds);
    }
}
