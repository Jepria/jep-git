package org.jepria.oauth.sdk;

import net.jcip.annotations.ThreadSafe;

import java.util.*;

/**
 * Grant Types for {@link org.jepria.oauth.sdk.TokenRequest}
 */
@ThreadSafe
public class GrantType {

  private GrantType(){}

  public static final String AUTHORIZATION_CODE = "authorization_code";
  public static final String IMPLICIT = "implicit";
  public static final String CLIENT_CREDENTIALS = "client_credentials";
  public static final String PASSWORD = "password";
  public static final String REFRESH_TOKEN = "refresh_token";

  private static final Map<String, Set<String>> grantResponseTypes;

  static {
    Map<String, Set<String>> dictionary = new HashMap<>();
    dictionary.put(AUTHORIZATION_CODE, Collections.unmodifiableSet(new HashSet<String>(){{
      add(ResponseType.CODE);
    }}));
    dictionary.put(IMPLICIT, Collections.unmodifiableSet(new HashSet<String>(){{
      add(ResponseType.TOKEN);
    }}));
    dictionary.put(CLIENT_CREDENTIALS, Collections.EMPTY_SET);
    dictionary.put(PASSWORD, Collections.EMPTY_SET);
    dictionary.put(REFRESH_TOKEN, Collections.EMPTY_SET);
    grantResponseTypes = Collections.unmodifiableMap(dictionary);
  }

  public static boolean implies(String grantType, String responseType) {
    if (implies(grantType)) {
      return grantResponseTypes.get(grantType).contains(responseType);
    } else {
      throw new IllegalArgumentException("Unsupported grant type");
    }
  }

  public static boolean implies(String grantType, Collection<String> responseTypes) {
    if (implies(grantType)) {
      return grantResponseTypes.get(grantType).containsAll(responseTypes);
    } else {
      throw new IllegalArgumentException("Unsupported grant type");
    }
  }

  public static List<String> getGrantTypes() {
    return new ArrayList<>(grantResponseTypes.keySet());
  }

  public static List<String> getGrantResponseTypes(String grantType) {
    if (implies(grantType)) {
      return new ArrayList<>(grantResponseTypes.get(grantType));
    } else {
      throw new IllegalArgumentException("Unsupported grant type");
    }
  }

  public static List<String> getGrantResponseTypes(Collection<String> grantTypes) {
    List<String> result = new ArrayList<>();
    grantTypes.stream().forEach(grantType -> {
      if (implies(grantType)) {
        result.addAll(grantResponseTypes.get(grantType));
      } else {
        throw new IllegalArgumentException("Unsupported grant type");
      }
    });
    return result;
  }

  public static boolean implies(String grantType) {
    return AUTHORIZATION_CODE.equals(grantType) || IMPLICIT.equals(grantType) || CLIENT_CREDENTIALS.equals(grantType) || PASSWORD.equals(grantType) || REFRESH_TOKEN.equals(grantType);
  }
}
