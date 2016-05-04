package com.technology.jep.jepria.auto;
import java.util.HashMap;
import java.util.Map;

public class JepAutoProperties {
    public static String JEPRIA_LOGIN_PAGE_TYPE = "jepria";
    public static String DEFAULT_LOGIN_PAGE_TYPE = "default";
    
    private static Map<String, String> properties = new HashMap<String, String>(); 

//	public static final String BASE_URL_KEY = PropertyLoader.loadProperty("base.url");
//	public static final String BROWSER_NAME_KEY = PropertyLoader.loadProperty("browser.name");
//	public static final String BROWSER_VERSION_KEY = PropertyLoader.loadProperty("browser.version");
//	public static final String BROWSER_PLATFORM_KEY = PropertyLoader.loadProperty("browser.platform");
//    public static final String JEPRIA_VERSION_KEY = PropertyLoader.loadProperty("jepria.version");
//    public static final String USERNAME_KEY = PropertyLoader.loadProperty("user.username");
//    public static final String PASSWORD_KEY = PropertyLoader.loadProperty("user.password");

	public static String BASE_URL_KEY = "BASE_URL_KEY";
	public static String BROWSER_NAME_KEY = "BROWSER_NAME_KEY";
	public static String BROWSER_VERSION_KEY = "BROWSER_VERSION_KEY";
	public static String BROWSER_PLATFORM_KEY = "BROWSER_PLATFORM_KEY";
	public static String BROWSER_PATH_KEY = "BROWSER_PATH_KEY"; // Используется для указания на конкретное место установки
    public static String JEPRIA_VERSION_KEY = "JEPRIA_VERSION_KEY";
    public static String USERNAME_KEY = "USERNAME_KEY";
    public static String PASSWORD_KEY = "PASSWORD_KEY";
    
    public static void set(String key, String value) {
    	properties.put(key, value);
    }
    
    public static String get(String key) {
    	return properties.get(key);
    }
}
