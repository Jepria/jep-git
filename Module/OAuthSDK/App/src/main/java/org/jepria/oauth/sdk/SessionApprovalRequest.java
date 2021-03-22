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
 *  SessionApprovalRequest tokenRequest = new SessionApprovalRequest.Builder()
 *       .resourceURI(URI.create("http://server/" + OAUTH_TOKEN_CONTEXT_PATH))
 *       .responseType(ResponseType.CODE)
 *       .clientId("clientID")
 *       .clientSecret("clientSecret")
 *       .operatorId(1234)
 *       .username("username")
 *       .state("state")
 *       .build();
 *  TokenResponse response = tokenRequest.execute();
 * </pre>
 */
public class SessionApprovalRequest {
  
  private final URI resourceURI;
  private final String responseType;
  private final String clientId;
  private final String loginModuleClientId;
  private final String loginModuleClientSecret;
  private final URI redirectionURI;
  private final Integer operatorId;
  private final String username;
  private final String state;
  
  public SessionApprovalRequest(URI resourceURI,
                                String responseType,
                                String clientId,
                                String loginModuleClientId,
                                String loginModuleClientSecret,
                                URI redirectionURI,
                                Integer operatorId,
                                String username,
                                String state) {
    Objects.requireNonNull(resourceURI, "Request URI must be not null");
    Objects.requireNonNull(clientId, "Client id type must be not null");
    Objects.requireNonNull(loginModuleClientId, "Login module client id type must be not null");
    Objects.requireNonNull(loginModuleClientSecret, "Login module client secret must be not null");
    Objects.requireNonNull(operatorId, "Operator id must be not null");
    Objects.requireNonNull(username, "Username must be not null");
    Objects.requireNonNull(responseType, "Response type must be not null");
    if (!ResponseType.implies(responseType)) {
      throw new IllegalArgumentException("Incorrect response type");
    }
    
    this.responseType = responseType;
    this.resourceURI = resourceURI;
    this.clientId = clientId;
    this.loginModuleClientId = loginModuleClientId;
    this.loginModuleClientSecret = loginModuleClientSecret;
    this.redirectionURI = redirectionURI;
    this.operatorId = operatorId;
    this.username = username;
    this.state = state;
  }
  
  public static Builder Builder() {
    return new BuilderImpl();
  }
  
  public URI getResourceURI() {
    return resourceURI;
  }
  
  public String getResponseType() {
    return responseType;
  }
  
  public String getClientId() {
    return clientId;
  }
  
  public URI getRedirectionURI() {
    return redirectionURI;
  }
  
  public Integer getOperatorId() {
    return operatorId;
  }
  
  public String getUsername() {
    return username;
  }
  
  public String getState() {
    return state;
  }
  
  private HttpURLConnection toHttpURLConnection(final HostnameVerifier hostnameVerifier, final SSLSocketFactory sslSocketFactory) throws IOException {
    LoginConfirmDto dto = new LoginConfirmDto();
    dto.setClientId(clientId);
    dto.setOperatorId(operatorId);
    dto.setRedirectUri(redirectionURI.toString());
    dto.setResponseType(responseType);
    dto.setState(state);
    dto.setUsername(username);
    
    Gson gson = new Gson();
    String json = gson.toJson(dto);
    
    HttpURLConnection conn = (HttpURLConnection) resourceURI.toURL().openConnection();
    
    if (conn instanceof HttpsURLConnection) {
      HttpsURLConnection sslConn = (HttpsURLConnection) conn;
      sslConn.setHostnameVerifier(hostnameVerifier != null ? hostnameVerifier : new DefaultHostnameVerifier());
      sslConn.setSSLSocketFactory(sslSocketFactory != null ? sslSocketFactory : (SSLSocketFactory) SSLSocketFactory.getDefault());
    }
    
    conn.setRequestMethod("PUT");
    conn.setConnectTimeout(10000);
    conn.setReadTimeout(0);
    conn.setInstanceFollowRedirects(false);
    conn.setRequestProperty("Authorization", "Basic " +
      Base64.getUrlEncoder().withoutPadding().encodeToString((loginModuleClientId + ":" + loginModuleClientSecret).getBytes()));
    
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("charset", "utf-8");
    conn.setRequestProperty("Content-Length", Integer.toString(json.length()));
    conn.setUseCaches(false);
    
    conn.setDoOutput(true);
    if (json != null) {
      try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
        writer.write(json);
      }
    }
    return conn;
  }
  
  /**
   * Executes Token Request.
   *
   * @return redirect URI
   * @throws IOException
   */
  public URI execute() throws IOException {
    return execute(null, null);
  }
  
  /**
   * Executes Token Request.
   * Provide HostnameVerifier and SSLSocketFactory if your origin and destination aren't at the same Host to establish HTTPS connection.
   *
   * @param hostnameVerifier
   * @param sslSocketFactory
   * @return redirect URI
   * @throws IOException
   */
  public URI execute(final HostnameVerifier hostnameVerifier, final SSLSocketFactory sslSocketFactory) throws IOException {
    HttpURLConnection conn = toHttpURLConnection(hostnameVerifier, sslSocketFactory);
    int statusCode;
    
    try {
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
    if (statusCode == 302) {
      return URI.create(conn.getHeaderField("Location"));
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
    Builder responseType(String responseType);
  
    Builder clientId(String clientId);
    
    Builder loginModuleClientId(String clientId);
    
    Builder loginModuleClientSecret(String clientSecret);
    
    Builder redirectionURI(URI redirectionURI);
    
    Builder userName(String username);
    
    Builder operatorId(Integer password);
    
    Builder state(String state);
    
    Builder resourceURI(URI resourceURI);
    
    SessionApprovalRequest build();
  }
  
  private static final class BuilderImpl implements Builder {
    
    private String responseType;
    private String clientId;
    private String loginModuleClientId;
    private String loginModuleClientSecret;
    private URI redirectionURI;
    private String state;
    private URI resourceURI;
    private String username;
    private Integer operatorId;
    private boolean isBuilt = false;
    
    @Override
    public Builder responseType(String responseType) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.responseType = responseType;
      return this;
    }
    
    @Override
    public Builder loginModuleClientId(String clientId) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.loginModuleClientId = clientId;
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
    public Builder loginModuleClientSecret(String clientSecret) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.loginModuleClientSecret = clientSecret;
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
    public Builder operatorId(Integer operatorId) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.operatorId = operatorId;
      return this;
    }
    
    @Override
    public Builder state(String state) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.state = state;
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
    public SessionApprovalRequest build() {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      isBuilt = true;
      return new SessionApprovalRequest(resourceURI,
        responseType,
        clientId,
        loginModuleClientId,
        loginModuleClientSecret,
        redirectionURI,
        operatorId,
        username,
        state);
    }
  }
  
  private class LoginConfirmDto {
    private String username;
    private Integer operatorId;
    private String responseType;
    private String redirectUri;
    private String clientId;
    private String state;
    
    public String getUsername() {
      return username;
    }
    
    public void setUsername(String username) {
      this.username = username;
    }
    
    public Integer getOperatorId() {
      return operatorId;
    }
    
    public void setOperatorId(Integer operatorId) {
      this.operatorId = operatorId;
    }
    
    public String getResponseType() {
      return responseType;
    }
    
    public void setResponseType(String responseType) {
      this.responseType = responseType;
    }
    
    public String getRedirectUri() {
      return redirectUri;
    }
    
    public void setRedirectUri(String redirectUri) {
      this.redirectUri = redirectUri;
    }
    
    public String getClientId() {
      return clientId;
    }
    
    public void setClientId(String clientId) {
      this.clientId = clientId;
    }
    
    public String getState() {
      return state;
    }
    
    public void setState(String state) {
      this.state = state;
    }
  }
}
