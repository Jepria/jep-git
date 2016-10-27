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
import static com.technology.jep.jepria.auto.JepAutoProperties.set;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.auto.util.WebDriverFactory;

public abstract class AutomationManagerImpl implements AutomationManager {
  private static Logger logger = Logger.getLogger(AutomationManagerImpl.class.getName());
  
  private boolean isStarted = false;
    
  public AutomationManagerImpl(String baseUrl,
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
      
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          WebDriverFactory.destroyInstance();
        } catch(Throwable th) {
          //th.printStackTrace(); TODO  Разобраться в причинах org.openqa.selenium.remote.UnreachableBrowserException
          logger.error(this.getClass() + ": Shutdown error", th);
        }
      }
    });
  }

  @Override
  public void start(String baseUrl) {
    WebDriverFactory.getDriver().get(baseUrl);
    this.isStarted = true;
  }

  public void stop() {
    WebDriverFactory.destroyInstance();
        
    this.isStarted = false;
  }

  @Override
  public boolean isStarted() {
    return isStarted;
  }
}


