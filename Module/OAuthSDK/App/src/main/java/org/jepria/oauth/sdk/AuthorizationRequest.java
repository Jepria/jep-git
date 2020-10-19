package org.jepria.oauth.sdk;

import org.jepria.oauth.sdk.util.URIUtil;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.jepria.oauth.sdk.OAuthConstants.*;

/**
 * <p><b>AuthorizationRequest.</b></p>
 * <pre>
 *  Used to start OAuth 2.0 authentication flow for end-use.
 *  Supports Authorization Code and Implicit flows.
 *  Supports custom parameters.
 * </pre>
 * <pre>
 *  Constructs the request URI by adding the following
 *  parameters to the query component of the authorization endpoint URI
 *  using the "application/x-www-form-urlencoded" format.
 * </pre>
 * <p>Example:
 * <pre>
 *   String authorizationRequestURI = new AuthorizationRequest.Builder()
 *         .resourceURI(URI.create("http://server/" + OAUTH_AUTHORIZATION_CONTEXT_PATH))
 *         .responseType(ResponseType.AUTHORIZATION_CODE)
 *         .clientId("clientID")
 *         .redirectionURI(URI.create("http://server/App?callback")
 *         .state(new State())
 *         .build()
 *         .toString();
 *   httpServletResponse.sendRedirect(authorizationRequestURI);
 * </pre>
 */
public final class AuthorizationRequest {

  private final URI resourceURI;
  private final String responseType;
  private final URI redirectionURI;
  private final String clientId;
  private final State state;

  public AuthorizationRequest(URI resourceURI, String responseType, URI redirectionURI, String clientId, State state){
    Objects.requireNonNull(resourceURI, "Request URI must be not null");
    this.resourceURI = resourceURI;
    Objects.requireNonNull(responseType, "Response type must be not null");
    if (!ResponseType.implies(responseType)) {
      throw new IllegalArgumentException("Response Type must have trusted value");
    }
    this.responseType = responseType;
    Objects.requireNonNull(redirectionURI, "Redirection URI must be not null");
    this.redirectionURI = redirectionURI;
    Objects.requireNonNull(clientId, "Client Id must be not null");
    this.clientId = clientId;
    Objects.requireNonNull(state, "State must be not null");
    this.state = state;
  }

  public URI getResourceURI() {
    return resourceURI;
  }

  public String getResponseType() {
    return responseType;
  }

  public URI getRedirectionURI() {
    return redirectionURI;
  }

  public String getClientId() {
    return clientId;
  }

  public State getState() {
    return state;
  }

  @Override
  public String toString() {
    String result = null;
    try {
      result = toURI().toString();
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      return result;
    }
  }

  public URI toURI() {
    URI result = null;
    try {
      Map<String, String> params = new HashMap<>();
      params.put(RESPONSE_TYPE, responseType);
      params.put(CLIENT_ID, clientId);
      params.put(REDIRECT_URI, redirectionURI.toString());
      params.put(STATE, state.toString());
      result = new URI(resourceURI.getScheme(), resourceURI.getAuthority(), resourceURI.getPath(), URIUtil.serializeParameters(params, null), resourceURI.getFragment());
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      return result;
    }
  }

  public static Builder Builder() {
    return new BuilderImpl();
  }

  public interface Builder {
    Builder clientId(String clientId);
    Builder responseType(String responseType);
    Builder redirectionURI(URI redirectionURI);
    Builder state(State state);
    Builder resourceURI(URI resourceURI);
    AuthorizationRequest build();
  }

  private static final class BuilderImpl implements Builder {

    private URI resourceURI;
    private String responseType;
    private URI redirectionURI;
    private String clientId;
    private State state;
    private boolean isBuilt = false;

    @Override
    public Builder clientId(String clientId) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.clientId = clientId;
      return this;
    }

    @Override
    public Builder responseType(String responseType) {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      this.responseType = responseType;
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
    public Builder state(State state) {
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
    public AuthorizationRequest build() {
      if (isBuilt) {
        new IllegalStateException("Builder is finished");
      }
      return new AuthorizationRequest(resourceURI, responseType, redirectionURI, clientId, state);
    }
  }
}
