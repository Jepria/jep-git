package com.technology.jep.jepria.auto.application;

import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.BASE_URL_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.BROWSER_NAME_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.BROWSER_PATH_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.BROWSER_PLATFORM_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.BROWSER_VERSION_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.DB_PASSWORD_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.DB_URL_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.DB_USER_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.DRIVER_PATH_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.JEPRIA_VERSION_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.PASSWORD_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.USERNAME_KEY;
import static com.technology.jep.jepria.auto.application.property.JepRiaAutoProperties.set;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.auto.util.WebDriverFactory;

public abstract class JepRiaApplicationAuto  {
  private static Logger logger = Logger.getLogger(JepRiaApplicationAuto.class.getName());
  
  public JepRiaApplicationAuto(String baseUrl,
        String browserName,
        String browserVersion,
        String browserPlatform,
        String browserPath,
        String driverPath,
        String jepriaVersion,
        String username,
        String password,
        String dbURL,
        String dbUser,
        String dbPassword) {
    
    set(BASE_URL_KEY, baseUrl);
    set(BROWSER_NAME_KEY, browserName);
    set(BROWSER_VERSION_KEY, browserVersion);
    set(BROWSER_PLATFORM_KEY, browserPlatform);
    set(BROWSER_PATH_KEY, browserPath);
    set(DRIVER_PATH_KEY, driverPath);
    set(JEPRIA_VERSION_KEY, jepriaVersion);
    set(USERNAME_KEY, username);
    set(PASSWORD_KEY, password);
    set(DB_URL_KEY, dbURL);
    set(DB_USER_KEY, dbUser);
    set(DB_PASSWORD_KEY, dbPassword);
      
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
}


