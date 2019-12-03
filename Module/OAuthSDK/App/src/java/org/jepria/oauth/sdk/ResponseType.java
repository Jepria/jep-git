package org.jepria.oauth.sdk;

/**
 * Authorization Response Types for {@link org.jepria.oauth.sdk.AuthorizationRequest}
 */
public class ResponseType {
  public static final String AUTHORIZATION_CODE = "code";
  public static final String IMPLICIT = "token";

  public static boolean implies(String responseType) {
    return AUTHORIZATION_CODE.equals(responseType) || IMPLICIT.equals(responseType);
  }
}
