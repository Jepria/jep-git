package org.jepria.oauth.sdk.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * URI manipulation utility methods
 */
public final class URIUtil {

  /**
   * The default UTF-8 character set.
   */
  public static final String CHARSET = "utf-8";


  private URIUtil(){}

  /**
   * Remove params with provided names from incoming URI in String representation.
   *
   * @param uri String representation of URI
   * @param parameterNames parameter names
   * @return String representation of provided URI with deleted parameters.
   * @throws URISyntaxException if provided URI is invalid
   */
  public static String removeQueryParameter(String uri, String... parameterNames) throws URISyntaxException {
    if (uri == null || uri.equals("")) {
      return "";
    }
    return removeQueryParameter(URI.create(uri), parameterNames).toString();
  }

  /**
   * Remove params with provided names from incoming URI.
   *
   * @param uri URI
   * @param parameterNames parameter names
   * @return provided URI with deleted parameters
   * @throws URISyntaxException if provided URI is invalid
   */
  public static URI removeQueryParameter(URI uri, String... parameterNames) throws URISyntaxException {
    if (uri == null) {
      return null;
    }
    String query = uri.getQuery();
    if (query == null || query.equals("") || parameterNames.length == 0) return uri;
    Map<String, String> parameters = null;
    try {
      parameters = parseParameters(query, null);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    for (String paramName : parameterNames) {
      parameters.remove(paramName);
    }
    try {
      return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), serializeParameters(parameters, null), uri.getFragment());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return uri;
    }
  }

  /**
   * Parses the specified URL query string into a parameter map. If a
   * parameter has multiple values only the first one will be saved. The
   * parameter keys and values are
   * {@code application/x-www-form-urlencoded} decoded.
   *
   * <p>Example query string:
   *
   * <pre>
   * param1=param1
   * &amp;param2=param2
   * </pre>
   *
   * <p>The opposite method {@link #serializeParameters}.
   *
   * @param query The URL query string to parse. May be {@code null}.
   * @param charset Charset of query string. If {@code null} => UTF-8 by default.
   *
   * @return A map of the URL query parameters, empty if none are found.
   */
  public static Map<String, String> parseParameters(String query, String charset) throws UnsupportedEncodingException {
    if (charset == null) charset = CHARSET;
    Map<String, String> params = new HashMap<>();
    if (query == null || query.equals("")) {
      return params; // empty map
    }
    StringTokenizer st = new StringTokenizer(query.trim(), "&");
    while(st.hasMoreTokens()) {
      String param = st.nextToken();
      String pair[] = param.split("=", 2); // Split around the first '='
      String key = URLDecoder.decode(pair[0], charset);
      String value = pair.length > 1 ? URLDecoder.decode(pair[1], charset) : "";
      params.putIfAbsent(key, value);
    }
    return params;
  }


  /**
   * Serialises the specified map of parameters into a URL query string.
   * The parameter keys and values are
   * {@code application/x-www-form-urlencoded} encoded.
   *
   * <p>Example result query string:
   *
   * <pre>
   * param1=param1
   * &amp;param2=param2
   * </pre>
   *
   * <p>The opposite method is {@link #parseParameters}.
   *
   * @param params A map of the URL query parameters. May be empty or
   *               {@code null}.
   * @param charset Charset of query string. If {@code null} => UTF-8 by default.
   *
   * @return The serialised URL query string, empty if no parameters.
   */
  public static String serializeParameters(Map<String, String> params, String charset) throws UnsupportedEncodingException {
    if (charset == null) charset = CHARSET;
    if (params == null || params.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String,String> entry: params.entrySet()) {
      String value = entry.getValue();
      if (value == null) {
        value = "";
      }
      String encodedKey = URLEncoder.encode(entry.getKey(), charset);
      String encodedValue = URLEncoder.encode(value, charset);
      if (sb.length() > 0) {
        sb.append('&');
      }
      sb.append(encodedKey);
      sb.append('=');
      sb.append(encodedValue);
    }
    return sb.toString();
  }
}
