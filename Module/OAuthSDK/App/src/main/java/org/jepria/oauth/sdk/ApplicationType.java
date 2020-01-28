package org.jepria.oauth.sdk;

public class ApplicationType {
  public static final String WEB = "web";
  public static final String NATIVE = "native";
  public static final String BROWSER = "browser";
  public static final String SERVICE = "service";

  public static boolean implies(String responseType) {
    return WEB.equals(responseType) || NATIVE.equals(responseType) || BROWSER.equals(responseType) || SERVICE.equals(responseType);
  }
}
