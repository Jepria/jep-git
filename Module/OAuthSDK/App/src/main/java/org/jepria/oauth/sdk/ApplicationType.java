package org.jepria.oauth.sdk;

import net.jcip.annotations.ThreadSafe;

import java.util.*;
import static org.jepria.oauth.sdk.GrantType.*;

/**
 * OAuth Client Application Types
 */
@ThreadSafe
public class ApplicationType {

  private ApplicationType(){}

  public static final String WEB = "web";
  public static final String NATIVE = "native";
  public static final String BROWSER = "browser";
  public static final String SERVICE = "service";

  private static final Map<String, Set<String>> applicationGrantTypes;

  static {
    Map<String, Set<String>> dictionary = new HashMap<>();
    dictionary.put(ApplicationType.BROWSER, Collections.unmodifiableSet(new HashSet<String>(){{
      add(AUTHORIZATION_CODE);
      add(IMPLICIT);
      add(PASSWORD);
    }}));
    dictionary.put(ApplicationType.NATIVE, Collections.unmodifiableSet(new HashSet<String>(){{
      add(AUTHORIZATION_CODE);
      add(IMPLICIT);
      add(PASSWORD);
      add(REFRESH_TOKEN);
    }}));
    dictionary.put(ApplicationType.WEB, Collections.unmodifiableSet(new HashSet<String>(){{
      add(AUTHORIZATION_CODE);
      add(IMPLICIT);
      add(PASSWORD);
      add(CLIENT_CREDENTIALS);
      add(REFRESH_TOKEN);
    }}));
    dictionary.put(ApplicationType.SERVICE, Collections.unmodifiableSet(new HashSet<String>(){{
      add(CLIENT_CREDENTIALS);
      add(REFRESH_TOKEN);
    }}));
    applicationGrantTypes = Collections.unmodifiableMap(dictionary);
  }

  public static boolean implies(String applicationType) {
    return WEB.equals(applicationType) || NATIVE.equals(applicationType) || BROWSER.equals(applicationType) || SERVICE.equals(applicationType);
  }

  public static boolean implies(String applicationType, String grantType) {
    if (implies(applicationType)) {
      return applicationGrantTypes.get(applicationType).contains(grantType);
    } else {
      throw new IllegalArgumentException("Unsupported application type");
    }
  }

  public static boolean implies(String applicationType, Collection<String> grantTypes) {
    if (implies(applicationType)) {
      return applicationGrantTypes.get(applicationType).containsAll(grantTypes);
    } else {
      throw new IllegalArgumentException("Unsupported application type");
    }
  }

  public static List<String> getApplicationTypes() {
    return new ArrayList<>(applicationGrantTypes.keySet());
  }

  public static List<String> getApplicationGrantTypes(String applicationType) {
    if (implies(applicationType)) {
      return new ArrayList<>(applicationGrantTypes.get(applicationType));
    } else {
      throw new IllegalArgumentException("Unsupported application type");
    }
  }

  public static List<String> getApplicationGrantTypes(List<String> applicationTypes) {
    List<String> result = new ArrayList<>();
    applicationTypes.stream().forEach(applicationType -> {
      if (implies(applicationType)) {
        result.addAll(applicationGrantTypes.get(applicationType));
      } else {
        throw new IllegalArgumentException("Unsupported application type");
      }
    });
    return result;
  }
}
