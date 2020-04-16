package org.jepria.oauth.sdk.jaxrs;

import org.jepria.oauth.sdk.TokenInfoRequest;
import org.jepria.oauth.sdk.TokenInfoResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.net.URI;

import static org.jepria.oauth.sdk.OAuthConstants.*;

/**
 * <p>
 *   Basic JWT security filter implementation.
 *   Allows to add custom token handling.
 * </p>
 */
public abstract class OAuthContainerRequestFilter implements ContainerRequestFilter {

  /**
   * Override this method, to create SecurityContext object based on special system requirements. E.G. getting roles, principal.
   *
   * @param tokenInfo Parsed {@link TokenInfoResponse}
   * @return
   */
  protected abstract SecurityContext getSecurityContext(TokenInfoResponse tokenInfo);

  /**
   * Override this method, to create specific manipulations with request object, or custom logic. E.G. session caching.
   *
   * @param request Incoming {@link HttpServletRequest}
   * @param tokenInfo Parsed {@link TokenInfoResponse}
   */
  protected void handleRequest(HttpServletRequest request, TokenInfoResponse tokenInfo){};

  protected String getTokenFromCookie() {
    return null;
  }

  protected abstract HttpServletRequest getRequest();
  /**
   * get Token string from Authorization HTTP Header
   * @return token
   */
  protected final String getTokenFromHeader() {
    String headerText = getRequest().getHeader("Authorization");
    if (headerText != null && headerText.startsWith("Bearer")) {
      return headerText.replaceFirst("Bearer ", "");
    } else {
      return null;
    }
  }

  /**
   * get OAuth client_id
   * @return
   */
  protected abstract String getClientSecret();

  /**
   * get OAuth client_secret
   * @return
   */
  protected abstract String getClientId();

  /**
   * Request token information form OAuth server
   */
  private TokenInfoResponse getTokenInfo(String tokenString) throws IOException {
    TokenInfoRequest tokenInfoRequest = TokenInfoRequest.Builder()
      .resourceURI(URI.create(getRequest().getRequestURL().toString().replaceFirst(getRequest().getRequestURI(), OAUTH_TOKENINFO_CONTEXT_PATH)))
      .clientId(getClientId())
      .clientSecret(getClientSecret())
      .token(tokenString)
      .build();
    TokenInfoResponse response =  tokenInfoRequest.execute();
    return response;
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    /*
     * Get token from Authorization Header
     */
    String tokenString = getTokenFromHeader();
    if (tokenString == null) {
      /*
       * Get token from Cookies
       */
      tokenString = getTokenFromCookie();
    }
    if (tokenString == null) {
        throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
    }
    /*
     * Introspect token with OAuth provider endpoint
     */
    TokenInfoResponse tokenClaims = getTokenInfo(tokenString);
    if (tokenClaims != null && tokenClaims.getActive()) {
      handleRequest(getRequest(), tokenClaims);
      containerRequestContext.setSecurityContext(getSecurityContext(tokenClaims));
    } else {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
    }
  }
}
