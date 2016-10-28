package com.technology.jep.jepria.auto.util;

import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.BROWSER_NAME_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.BROWSER_PATH_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.DRIVER_PATH_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.get;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties;

/*
 * Factory to instantiate a WebDriver object. It returns an instance of the driver (local invocation) or an instance of RemoteWebDriver
 */
public class WebDriverFactory {
  private static Logger logger = Logger.getLogger(WebDriverFactory.class.getName());

  private static final int WEBDRIVER_WAIT_DEFAULT_TIMEOUT = 10;

  /* Browsers constants */
  public static final String CHROME = "chrome";
  public static final String FIREFOX = "firefox";
  public static final String INTERNET_EXPLORER = "ie";

  private static WebDriver webDriver = null;

  public static WebDriver getDriver() {

    if (webDriver != null) {
        return webDriver;
    }
    
    logger.info("JepAutoProperties = " + JepRiaAutoProperties.asString());

    String browserName = get(BROWSER_NAME_KEY);

    if (CHROME.equals(browserName)) {
      System.setProperty("webdriver.chrome.driver", get(DRIVER_PATH_KEY));
      webDriver = new ChromeDriver();
      logger.info("ChromeDriver has created");
    } else if (FIREFOX.equals(browserName)) {
      FirefoxProfile ffProfile = new FirefoxProfile();
      ffProfile.setPreference("network.http.phishy-userpass-length", 255);

      FirefoxBinary binary = new FirefoxBinary(new File(get(BROWSER_PATH_KEY)));
      webDriver = new FirefoxDriver(binary, new FirefoxProfile());

      logger.info("FirefoxDriver has created");
    } else if (INTERNET_EXPLORER.equals(browserName)) {
      webDriver = new InternetExplorerDriver();
      logger.info("InternetExplorerDriver has created");
    } else {
      webDriver = new ChromeDriver();
      logger.info("ChromeDriver has created");
    }

    return webDriver;
  }

  public static void destroyInstance() {
    if(webDriver != null) {
      webDriver.quit();
      webDriver = null;
    }
  }

  public static WebDriverWait getWait(int timeout) {
    return new WebDriverWait(WebDriverFactory.getDriver(), timeout);
  }
  
  public static WebDriverWait getWait() {
    return new WebDriverWait(WebDriverFactory.getDriver(), WEBDRIVER_WAIT_DEFAULT_TIMEOUT);
  }

}