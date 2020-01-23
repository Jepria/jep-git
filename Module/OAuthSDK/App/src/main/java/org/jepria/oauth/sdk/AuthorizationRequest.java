package org.jepria.oauth.sdk;

import org.jepria.oauth.sdk.util.URIUtil;

import java.net.URI;
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
    if (resourceURI == null) {
      throw new IllegalArgumentException("Request URI must be not null");
    }
    this.resourceURI = resourceURI;

    if (responseType == null) {
      throw new IllegalArgumentException("Response type must be not null");
    }
    this.responseType = responseType;

    if (redirectionURI == null) {
      throw new IllegalArgumentException("Redirection URI must be not null");
    }
    this.redirectionURI = redirectionURI;

    if (clientId == null) {
      throw new IllegalArgumentException("Client Id must be not null");
    }
    this.clientId = clientId;

    if (state == null) {
      throw new IllegalArgumentException("State must be not null");
    }
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
      params.put(REDIRECT_URI, Base64.getUrlEncoder().withoutPadding().encodeToString(redirectionURI.toString().getBytes()));
      params.put(STATE, state.toString());
      result = new URI(resourceURI.getScheme(), resourceURI.getAuthority(), resourceURI.getPath(), URIUtil.serializeParameters(params, null), resourceURI.getFragment());
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      return result;
    }
  }


  public static final class Builder {

    private URI resourceURI;
    private String responseType;
    private URI redirectionURI;
    private String clientId;
    private State state;

    public Builder(){}

    public Builder clientId(String clientId) {
      if (clientId == null) {
        throw new IllegalArgumentException("Client Id must be not null");
      }
      this.clientId = clientId;
      return this;
    }

    public Builder responseType(String responseType) {
      if (responseType == null) {
        throw new IllegalArgumentException("Redirection URI must be not null");
      }
      if (!ResponseType.implies(responseType)) {
        throw new IllegalArgumentException("Response Type must have trusted value");
      }
      this.responseType = responseType;
      return this;
    }

    public Builder redirectionURI(URI redirectionURI) {
      if (redirectionURI == null) {
        throw new IllegalArgumentException("Redirection URI must be not null");
      }
      this.redirectionURI = redirectionURI;
      return this;
    }

    public Builder state(State state) {
      if (state == null) {
        throw new IllegalArgumentException("State must be not null");
      }
      this.state = state;
      return this;
    }

    public Builder resourceURI(URI resourceURI) {
      if (resourceURI == null) {
        throw new IllegalArgumentException("Resource URI must be not null");
      }
      this.resourceURI = resourceURI;
      return this;
    }

    public AuthorizationRequest build() {
      return new AuthorizationRequest(resourceURI, responseType, redirectionURI, clientId, state);
    }
  }
}
