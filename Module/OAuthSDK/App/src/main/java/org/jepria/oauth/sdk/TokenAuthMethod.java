package org.jepria.oauth.sdk;

public class TokenAuthMethod {
  public static final String NONE = "none";
  public static final String CLIENT_SECRET_POST = "client_secret_post";
  public static final String CLIENT_SECRET_BASIC = "client_secret_basic";

  public static boolean implies(String responseType) {
    return NONE.equals(responseType) || CLIENT_SECRET_POST.equals(responseType) || CLIENT_SECRET_BASIC.equals(responseType);
  }
}
