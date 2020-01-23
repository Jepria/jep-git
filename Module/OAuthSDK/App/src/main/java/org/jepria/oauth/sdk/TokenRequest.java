package org.jepria.oauth.sdk;

import com.google.gson.Gson;
import org.jepria.oauth.sdk.util.URIUtil;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

import static org.jepria.oauth.sdk.OAuthConstants.*;


/**
 * <pre>
 * Token request builder.
 * Return entity for JSON object {@link org.jepria.oauth.sdk.TokenResponse}
 * </pre>
 * <p>Example:
 * <pre>
 *  TokenRequest tokenRequest = new TokenRequest.Builder()
 *       .resourceURI(URI.create("http://server/" + OAUTH_TOKEN_CONTEXT_PATH))
 *       .grantType(GrantType.AUTHORIZATION_CODE)
 *       .clientId("clientID")
 *       .clientSecret("clientSecret")
 *       .redirectionURI(URI.create("http://server/App?callback"))
 *       .authorizationCode("code")
 *       .build();
 *  TokenResponse response = tokenRequest.execute();
 * </pre>
 */
public class TokenRequest {

  private final URI resourceURI;
  private final String grantType;
  private final String clientId;
  private final String clientSecret;
  private final URI redirectionURI;
  private final String authorizationCode;

  public TokenRequest(URI resourceURI, String grantType, String clientId, String clientSecret, URI redirectionURI, String authorizationCode) {
    if (resourceURI == null) {
      throw new IllegalArgumentException("Request URI must be not null");
    }
    this.resourceURI = resourceURI;

    if (GrantType.AUTHORIZATION_CODE.equals(grantType)) {
      if (authorizationCode == null) {
        throw new IllegalArgumentException("Authorization Code must be not null");
      }
      if (redirectionURI == null) {
        throw new IllegalArgumentException("Redirection URI must be not null");
      }
      if (clientId == null) {
        throw new IllegalArgumentException("Client Id must be not null");
      }
    }

    this.grantType = grantType;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectionURI = redirectionURI;
    this.authorizationCode = authorizationCode;
  }

  public URI getResourceURI() {
    return resourceURI;
  }

  public String getGrantType() {
    return grantType;
  }

  public String getClientId() {
    return clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public URI getRedirectionURI() {
    return redirectionURI;
  }

  public String getAuthorizationCode() {
    return authorizationCode;
  }

  private HttpURLConnection toHttpURLConnection(final HostnameVerifier hostnameVerifier, final SSLSocketFactory sslSocketFactory) throws IOException {
    Map<String, String> params = new HashMap<>();
    if (GrantType.AUTHORIZATION_CODE.equals(grantType)) {
      params.put(GRANT_TYPE, grantType);
      params.put(CLIENT_ID, clientId);
      if (clientSecret != null) {
        params.put(CLIENT_SECRET, clientSecret);
      }
      params.put(REDIRECT_URI, Base64.getUrlEncoder().withoutPadding().encodeToString(redirectionURI.toString().getBytes()));
      params.put(CODE, authorizationCode);
    }
    String query = "";
    try {
      query = URIUtil.serializeParameters(params, null);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    URL finalURL = null;
    try {
      finalURL = new URI(resourceURI.getScheme(), resourceURI.getAuthority(), resourceURI.getPath(), "", resourceURI.getFragment()).toURL();
    } catch (URISyntaxException | MalformedURLException e) {
      e.printStackTrace();
    }

    HttpURLConnection conn = (HttpURLConnection) finalURL.openConnection();

    if (conn instanceof HttpsURLConnection) {
      HttpsURLConnection sslConn = (HttpsURLConnection)conn;
      sslConn.setHostnameVerifier(hostnameVerifier != null ? hostnameVerifier : new DefaultHostnameVerifier());
      sslConn.setSSLSocketFactory(sslSocketFactory != null ? sslSocketFactory :  (SSLSocketFactory) SSLSocketFactory.getDefault());
    }

    conn.setRequestMethod("POST");
    conn.setConnectTimeout(10000);
    conn.setReadTimeout(0);
    conn.setInstanceFollowRedirects(true);
    if (clientSecret != null) {
      conn.setRequestProperty("Authorization", "Basic " +
        Base64.getUrlEncoder().withoutPadding().encodeToString((clientId + ":" + clientSecret).getBytes()));
    }

    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setRequestProperty("charset", "utf-8");
    conn.setRequestProperty("Content-Length", Integer.toString(query.length()));
    conn.setUseCaches(false);

    conn.setDoOutput(true);
    if (query != null) {
      try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
        writer.write(query);
      }
    }
    return conn;
  }

  /**
   * Executes Token Request.
   *
   * @return {@link org.jepria.oauth.sdk.TokenResponse}
   * @throws IOException
   */
  public TokenResponse execute() throws IOException {
    return execute(null, null);
  }

  /**
   * Executes Token Request.
   * Provide HostnameVerifier and SSLSocketFactory if your origin and destination aren't at the same Host to establish HTTPS connection.
   *
   * @param hostnameVerifier
   * @param sslSocketFactory
   * @return {@link org.jepria.oauth.sdk.TokenResponse}
   * @throws IOException
   */
  public TokenResponse execute(final HostnameVerifier hostnameVerifier, final SSLSocketFactory sslSocketFactory) throws IOException {
    HttpURLConnection conn = toHttpURLConnection(hostnameVerifier, sslSocketFactory);
    int statusCode;
    BufferedReader reader;

    try {
      reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
      statusCode = conn.getResponseCode();
    } catch (IOException e) {
      // HttpUrlConnection will throw an IOException if any
      // 4XX response is sent. If we request the status
      // again, this time the internal status will be
      // properly set, and we'll be able to retrieve it.
      statusCode = conn.getResponseCode();
      if (statusCode == -1) {
        throw e; // Rethrow IO exception
      } else {
        throw new IOException("HTTP Response Status Code: " + statusCode + '\n' + e.getLocalizedMessage());
      }
    }
    if (statusCode == 200) {
      try {
        Gson gson = new Gson();
        return gson.fromJson(reader, TokenResponse.class);
      } catch (Throwable th) {
        th.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }

  private final class DefaultHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
      return hostname.equals(session.getPeerHost());
    }
  }

  public static final class Builder {

    private String grantType;
    private String clientId;
    private String clientSecret;
    private URI redirectionURI;
    private String authorizationCode;
    private URI resourceURI;

    public Builder grantType(String grantType) {
      if (grantType == null) {
        throw new IllegalArgumentException("Grant type must be not null");
      }
      this.grantType = grantType;
      return this;
    }

    public Builder clientId(String clientId) {
      if (clientId == null) {
        throw new IllegalArgumentException("Client Id must be not null");
      }
      this.clientId = clientId;
      return this;
    }

    public Builder clientSecret(String clientSecret) {
      if (clientSecret == null) {
        throw new IllegalArgumentException("Client Secret must be not null");
      }
      this.clientSecret = clientSecret;
      return this;
    }

    public Builder redirectionURI(URI redirectionURI) {
      if (redirectionURI == null) {
        throw new IllegalArgumentException("Redirection URI must be not null");
      }
      this.redirectionURI = redirectionURI;
      return this;
    }

    public Builder authorizationCode(String authorizationCode) {
      if (authorizationCode == null) {
        throw new IllegalArgumentException("Authorization code must be not null");
      }
      this.authorizationCode = authorizationCode;
      return this;
    }

    public Builder resourceURI(URI resourceURI) {
      if (resourceURI == null) {
        throw new IllegalArgumentException("Resource URI must be not null");
      }
      this.resourceURI = resourceURI;
      return this;
    }

    public TokenRequest build() {
      return new TokenRequest(resourceURI, grantType, clientId, clientSecret, redirectionURI, authorizationCode);
    }
  }
}
