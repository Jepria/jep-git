package org.jepria.server.data;

public final class NamingUtil {
  private NamingUtil() {}

  public static String camelCase2snake_case(String s) {
    if (s == null) {
      return null;
    }

    StringBuilder sb = new StringBuilder();
    boolean begin = true;
    char prev = 0;

    for (int i = 0; i < s.length(); i++) {
      final char c = s.charAt(i);

      if (c >= 'a' && c <= 'z') {
        sb.append(c);
      } else if (c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
        if (!begin && !(c >= '0' && c <= '9' && prev >= '0' && prev <= '9')) {
          sb.append('_');
        }
        if (c >= 'A' && c <= 'Z') {
          sb.append(Character.toLowerCase(c));
        } else {
          sb.append(c);
        }
      } else {
        // TODO append illegal character as-is or throw exception?
        sb.append(c);
      }
      begin = false;
      prev = c;
    }

    return sb.toString();
  }

  public static String snake_case2camelCase(String s) {
    if (s == null) {
      return null;
    }

    StringBuilder sb = new StringBuilder();
    boolean nextCap = false;

    for (int i = 0; i < s.length(); i++) {
      final char c = s.charAt(i);
      if (c == '_') {
        nextCap = true;
      } else {
        if (nextCap) {
          sb.append(Character.toUpperCase(c));
          nextCap = false;
        } else {
          sb.append(Character.toLowerCase(c));
        }
      }
    }

    return sb.toString();
  }

  public static String snake_case2CapCamelCase(String s) {
    if (s == null) {
      return null;
    }

    return snake_case2camelCase("_" + s);
  }
}
