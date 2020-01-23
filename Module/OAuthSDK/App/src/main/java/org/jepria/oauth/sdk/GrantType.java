package org.jepria.oauth.sdk;

/**
 * Grant Types for {@link org.jepria.oauth.sdk.TokenRequest}
 */
public class GrantType {

  public static final String AUTHORIZATION_CODE = "authorization_code";
  public static final String IMPLICIT = "implicit";
  public static final String CLIENT_CREDENTIALS = "client_credentials";
  public static final String PASSWORD = "password";

  public static boolean implies(String grantType) {
    return AUTHORIZATION_CODE.equals(grantType) || CLIENT_CREDENTIALS.equals(grantType) || PASSWORD.equals(grantType);
  }
}
