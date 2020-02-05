package org.jepria.oauth.sdk.jersey;

import org.jepria.oauth.sdk.TokenInfoRequest;
import org.jepria.oauth.sdk.TokenInfoResponse;

import javax.servlet.http.Cookie;
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
import static org.jepria.oauth.sdk.OAuthConstants.CLIENT_SECRET_PROPERTY;

@JWTSecured
/**
 * <p>
 *   Basic JWT security filter implementation.
 *   Allows to add custom token handling.
 * </p>
 */
public abstract class JWTSecurityFilter implements ContainerRequestFilter {

  @Context
  HttpServletRequest request;

  /**
   * Override this method, to create SecurityContext object based on special system requirements. E.G. getting roles, principal.
   *
   * @param tokenInfo Parsed {@link TokenInfoResponse}
   * @return
   */
  public abstract SecurityContext getSecurityContext(TokenInfoResponse tokenInfo);

  /**
   * Override this method, to create specific manipulations with request object, or custom logic. E.G. session caching.
   *
   * @param request Incoming {@link HttpServletRequest}
   * @param tokenInfo Parsed {@link TokenInfoResponse}
   */
  public void handleRequest(HttpServletRequest request, TokenInfoResponse tokenInfo){};

  protected final String getTokenFromHeader() {
    String headerText = request.getHeader("Authorization");
    if (headerText != null && headerText.startsWith("Bearer")) {
      return headerText.replaceFirst("Bearer ", "");
    } else {
      return null;
    }
  }

  /**
   * Request token information form OAuth server
   */
  private TokenInfoResponse getTokenInfo(String tokenString) throws IOException {
    TokenInfoRequest tokenInfoRequest = new TokenInfoRequest.Builder()
      .resourceURI(URI.create(request.getRequestURL().toString().replaceFirst(request.getRequestURI(), OAUTH_TOKENINFO_CONTEXT_PATH)))
      .clientId(request.getServletContext().getInitParameter(CLIENT_ID_PROPERTY))
      .clientSecret(request.getServletContext().getInitParameter(CLIENT_SECRET_PROPERTY))
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
        throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
    }
    /*
     * Introspect token with OAuth provider endpoint
     */
    TokenInfoResponse tokenClaims = getTokenInfo(tokenString);
    if (tokenClaims != null && tokenClaims.getActive()) {
      handleRequest(request, tokenClaims);
      containerRequestContext.setSecurityContext(getSecurityContext(tokenClaims));
    } else {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
    }
  }
}
