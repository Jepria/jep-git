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
import java.nio.charset.StandardCharsets;
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
  private final String username;
  private final String password;
  private final String refreshToken;
  
  public TokenRequest(URI resourceURI,
                      String grantType,
                      String clientId,
                      String clientSecret,
                      URI redirectionURI,
                      String authorizationCode,
                      String username,
                      String password,
                      String refreshToken) {
    Objects.requireNonNull(resourceURI, "Request URI must be not null");
    this.resourceURI = resourceURI;
    
    Objects.requireNonNull(grantType, "Grant Type must be not null");
    this.grantType = grantType;
    
    if (!GrantType.implies(grantType)) {
      throw new IllegalArgumentException("Incorrect grant_type");
    }
    
    if (GrantType.AUTHORIZATION_CODE.equals(grantType)) {
      Objects.requireNonNull(authorizationCode, "Authorization Code must be not null");
      Objects.requireNonNull(redirectionURI, "Redirection URI must be not null");
      Objects.requireNonNull(clientId, "Client Id must be not null");
      Objects.requireNonNull(clientSecret, "Client Id must be not null");
    }
    
    if (GrantType.PASSWORD.equals(grantType)) {
      Objects.requireNonNull(username, "Username must be not null");
      Objects.requireNonNull(password, "Password must be not null");
    }
    
    if (GrantType.CLIENT_CREDENTIALS.equals(grantType)) {
      Objects.requireNonNull(clientId, "Client ID must be not null");
      Objects.requireNonNull(clientSecret, "Client Secret must be not null");
    }
  
    if (GrantType.REFRESH_TOKEN.equals(grantType)) {
      Objects.requireNonNull(refreshToken, "Refresh token must be not null");
    }
    
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectionURI = redirectionURI;
    this.authorizationCode = authorizationCode;
    this.username = username;
    this.password = password;
    this.refreshToken = refreshToken;
  }
  
  public static Builder Builder() {
    return new BuilderImpl();
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
  
  private Map<String, String> authorizationCode() {
    Map<String, String> params = new HashMap<>();
    params.put(GRANT_TYPE, grantType);
    params.put(CLIENT_ID, clientId);
    params.put(CLIENT_SECRET, clientSecret);
    try {
      params.put(REDIRECT_URI, URLEncoder.encode(redirectionURI.toString(), StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    params.put(CODE, authorizationCode);
    return params;
  }
  
  private Map<String, String> password() {
    Map<String, String> params = new HashMap<>();
    params.put(GRANT_TYPE, grantType);
    params.put(CLIENT_ID, clientId);
    params.put(CLIENT_SECRET, clientSecret);
    params.put(USERNAME, username);
    params.put(PASSWORD, password);
    return params;
  }
  
  private Map<String, String> clientCredentials() {
    Map<String, String> params = new HashMap<>();
    params.put(GRANT_TYPE, grantType);
    params.put(CLIENT_ID, clientId);
    params.put(CLIENT_SECRET, clientSecret);
    return params;
  }
  
  private Map<String, String> refreshToken() {
    Map<String, String> params = new HashMap<>();
    params.put(GRANT_TYPE, grantType);
    params.put(CLIENT_ID, clientId);
    params.put(CLIENT_SECRET, clientSecret);
    params.put(REFRESH_TOKEN, refreshToken);
    return params;
  }
  
  private HttpURLConnection toHttpURLConnection(final HostnameVerifier hostnameVerifier, final SSLSocketFactory sslSocketFactory) throws IOException {
    Map<String, String> params = null;
    if (GrantType.AUTHORIZATION_CODE.equals(grantType)) {
      params = authorizationCode();
    } else if (GrantType.PASSWORD.equals(grantType)) {
      params = password();
    } else if (GrantType.CLIENT_CREDENTIALS.equals(grantType)) {
      params = clientCredentials();
    } else if (GrantType.REFRESH_TOKEN.equals(grantType)) {
      params = refreshToken();
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
      HttpsURLConnection sslConn = (HttpsURLConnection) conn;
      sslConn.setHostnameVerifier(hostnameVerifier != null ? hostnameVerifier : new DefaultHostnameVerifier());
      sslConn.setSSLSocketFactory(sslSocketFactory != null ? sslSocketFactory : (SSLSocketFactory) SSLSocketFactory.getDefault());
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
  
  public interface Builder {
    Builder grantType(String grantType);
    
    Builder clientId(String clientId);
    
    Builder clientSecret(String clientSecret);
    
    Builder redirectionURI(URI redirectionURI);
    
    Builder userName(String username);
    
    Builder password(String password);
    
    Builder authorizationCode(String authorizationCode);
  
    Builder refreshToken(String refreshToken);
  
    Builder resourceURI(URI resourceURI);
    
    TokenRequest build();
  }
  
  private static final class BuilderImpl implements Builder {
    
    private String grantType;
    private String clientId;
    private String clientSecret;
    private URI redirectionURI;
    private String authorizationCode;
    private URI resourceURI;
    private String username;
    private String password;
    private String refreshToken;
    private boolean isBuilt = false;
    
    @Override
    public Builder grantType(String grantType) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.grantType = grantType;
      return this;
    }
    
    @Override
    public Builder clientId(String clientId) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.clientId = clientId;
      return this;
    }
    
    @Override
    public Builder clientSecret(String clientSecret) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.clientSecret = clientSecret;
      return this;
    }
    
    @Override
    public Builder redirectionURI(URI redirectionURI) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.redirectionURI = redirectionURI;
      return this;
    }
    
    @Override
    public Builder userName(String username) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.username = username;
      return this;
    }
    
    @Override
    public Builder password(String password) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.password = password;
      return this;
    }
  
    @Override
    public Builder authorizationCode(String authorizationCode) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.authorizationCode = authorizationCode;
      return this;
    }
  
    @Override
    public Builder refreshToken(String refreshToken) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.refreshToken = refreshToken;
      return this;
    }
    
    @Override
    public Builder resourceURI(URI resourceURI) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.resourceURI = resourceURI;
      return this;
    }
    
    @Override
    public TokenRequest build() {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      isBuilt = true;
      return new TokenRequest(resourceURI, grantType, clientId, clientSecret, redirectionURI, authorizationCode, username, password, refreshToken);
    }
  }
}
