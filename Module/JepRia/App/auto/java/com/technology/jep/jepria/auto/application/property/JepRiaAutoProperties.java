package com.technology.jep.jepria.auto.application.property;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JepRiaAutoProperties {
  public static String JEPRIA_LOGIN_PAGE_TYPE = "jepria";
  public static String DEFAULT_LOGIN_PAGE_TYPE = "default";
  
  private static Map<String, String> properties = new HashMap<String, String>(); 

  public static final String BASE_URL_KEY = "baseUrl";
  public static final String BROWSER_NAME_KEY = "browserName";
  public static final String BROWSER_VERSION_KEY = "browserVersion";
  public static final String BROWSER_PLATFORM_KEY = "browserPlatform";
  public static final String BROWSER_PATH_KEY = "browserPath"; // Используется для указания на конкретное место установки браузера (если их может быть несколько, как у FireFox)
  public static final String DRIVER_PATH_KEY = "driverPath"; // Используется для указания на конкретное место установки драйвера (для некоторых браузеров, таких как firefox, не нужен)
  public static final String JEPRIA_VERSION_KEY = "jepRiaVersion";
  public static final String USERNAME_KEY = "username";
  public static final String PASSWORD_KEY = "password";
  
  public static final String DB_URL_KEY = "dbURL";
  public static final String DB_USER_KEY = "dbUser";
  public static final String DB_PASSWORD_KEY = "dbPassword";
  
  public static void set(String key, String value) {
    properties.put(key, value);
  }
    
  public static String get(String key) {
    return properties.get(key);
  }
  
  public static String asString() {
    StringBuilder result = new StringBuilder();
    
    for(Entry<String, String> entry: properties.entrySet()) {
      if(result.length() > 0) {
        result.append(",\n");
      }
      result.append(entry.getKey());
      result.append(" = ");
      result.append('"');
      result.append(entry.getValue());
      result.append('"');
    }
    return result.toString();
  }
}
