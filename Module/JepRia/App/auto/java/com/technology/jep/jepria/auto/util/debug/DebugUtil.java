package com.technology.jep.jepria.auto.util.debug;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

public class DebugUtil {
	private static Logger logger = Logger.getLogger(DebugUtil.class.getName());

	public static void inspectWebElement(WebElement webElement) {
		logger.debug("webElement.getTagName() = " + webElement.getTagName());
		logger.debug("webElement.getText() = " + webElement.getText());
	}

}
