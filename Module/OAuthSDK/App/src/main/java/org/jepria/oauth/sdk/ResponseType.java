package org.jepria.oauth.sdk;

import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.List;

/**
 * Authorization Response Types for {@link org.jepria.oauth.sdk.AuthorizationRequest}
 */
@ThreadSafe
public class ResponseType {

  private ResponseType(){}

  public static final String CODE = "code";
  public static final String TOKEN = "token";

  public static boolean implies(String responseType) {
    return CODE.equals(responseType) || TOKEN.equals(responseType);
  }

  public static List<String> getResponseTypes() {
    return new ArrayList<String>(){{
      add(CODE);
      add(TOKEN);
    }};
  }
}
