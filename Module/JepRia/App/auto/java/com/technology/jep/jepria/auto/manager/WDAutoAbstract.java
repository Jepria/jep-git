package com.technology.jep.jepria.auto.manager;

import static com.technology.jep.jepria.auto.JepAutoProperties.BASE_URL_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.BROWSER_NAME_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.BROWSER_PATH_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.BROWSER_PLATFORM_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.BROWSER_VERSION_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.DRIVER_PATH_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.JEPRIA_VERSION_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.PASSWORD_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.USERNAME_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.get;
import static com.technology.jep.jepria.auto.JepAutoProperties.set;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.technology.jep.jepria.auto.util.WebDriverFactory;

public abstract class WDAutoAbstract implements JepRiaAuto {
	private static Logger logger = Logger.getLogger(WDAutoAbstract.class.getName());
//    private WebDriver driver;
	private boolean isStarted = false;
    
    public WDAutoAbstract(String baseUrl,
    			String browserName,
    			String browserVersion,
    			String browserPlatform,
    			String browserPath,
    			String driverPath,
    			String jepriaVersion,
    			String username,
    			String password) {
		
		set(BASE_URL_KEY, baseUrl);
		set(BROWSER_NAME_KEY, browserName);
		set(BROWSER_VERSION_KEY, browserVersion);
		set(BROWSER_PLATFORM_KEY, browserPlatform);
		set(BROWSER_PATH_KEY, browserPath);
		set(DRIVER_PATH_KEY, driverPath);
		set(JEPRIA_VERSION_KEY, jepriaVersion);
		set(USERNAME_KEY, username);
		set(PASSWORD_KEY, password);
    	
//        Browser browser = new Browser();
//        browser.setName(JepAutoProperties.BROWSER_NAME_KEY);
//        browser.setVersion(JepAutoProperties.BROWSER_VERSION_KEY);
//        browser.setPlatform(JepAutoProperties.BROWSER_PLATFORM_KEY);
//
//        driver = WebDriverFactory.getInstance(browser, JepAutoProperties.USERNAME_KEY, JepAutoProperties.PASSWORD_KEY);
        
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					WebDriverFactory.destroyInstance();
//					getWebDriver().quit();
				} catch(Throwable th) {
					//th.printStackTrace(); TODO  Разобраться в причинах org.openqa.selenium.remote.UnreachableBrowserException
			    	logger.error(this.getClass() + ": Shutdown error", th);
				}
			}
		});
    }

    @Override
    public WebDriver getWebDriver() {
        return WebDriverFactory.getDriver();
    }
	
	@Override
	public void start(String baseUrl) {
		info("Application is starting...");
		
    	info("baseUrl = " + baseUrl);
    	info("jepria.version = " + get(JEPRIA_VERSION_KEY));
    	info("user.username = " + get(USERNAME_KEY));
        
    	this.isStarted = true;
    	
		info("Application has started");
	}

	public void stop() {
        WebDriverFactory.destroyInstance();
        
    	info("Application has stopped");
    	this.isStarted = false;
    }

	@Override
	public boolean isStarted() {
		return isStarted;
	}

    private void info(String message) {
    	logger.info(this.getClass() + ":  " + message);
	}

	private void debug(String message) {
    	logger.debug(this.getClass() + ":  " + message);
	}
}


