package com.technology.jep.jepria.server.security.servlet;

import com.technology.jep.jepria.server.security.TokenRequestWrapper;
import org.apache.log4j.Logger;
import org.jepria.oauth.sdk.*;
import org.jepria.oauth.sdk.util.URIUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jepria.oauth.sdk.OAuthConstants.*;

public class SecurityFilter implements Filter {

  private static Logger logger = Logger.getLogger(SecurityFilter.class.getName());

  private ServletContext servletContext;
  public static final String SECURITY_CONSTRAINT = "security-constraint";
  private TreeMap<String, String[]> securityConstraints = new TreeMap<>();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    servletContext = filterConfig.getServletContext();
    String securityConstraintsString = filterConfig.getInitParameter(SECURITY_CONSTRAINT);
    if (securityConstraintsString != null && securityConstraintsString.length() > 0) {
      String[] securityConstraints = securityConstraintsString.split("\\s*;\\s*");
      for (String securityConstraint : securityConstraints) {
        if (securityConstraint.chars().filter(ch -> ch == ':').count() > 1) {
          logger.error("Wrong 'security-constraint' value.\n'security-constraint' pattern: \nurl1,...,urlN:role1,...,roleN;\nurl1,...,urlN;");
          throw new IllegalStateException("Wrong 'security-constraint' value.\n'security-constraint' pattern: \nurl1,...,urlN:role1,...,roleN;\nurl1,...,urlN;");
        }
        String[] securityConstraintParts = securityConstraint.split("\\s*:\\s*");
        String[] urlPatterns = securityConstraintParts[0].split("\\s*,\\s*");
        String[] securityRoles;
        if (securityConstraintParts.length == 2) {
          securityRoles = securityConstraintParts[1].length() > 0 ? securityConstraintParts[1].split("\\s*,\\s*") : new String[0];
        } else {
          securityRoles = new String[0];
        }
        for (String urlPattern : urlPatterns) {
          if (!this.securityConstraints.containsKey(urlPattern) ){
            this.securityConstraints.put(urlPattern, securityRoles);
          } else {
            logger.error("URL pattern must bew unique value in 'security-constraint' init parameter");
            throw new IllegalStateException("URL pattern must bew unique value in 'security-constraint' init parameter");
          }
        }
      }
    }
  }

  /**
   * Request token from OAuth server
   */
  private TokenResponse getToken(HttpServletRequest httpServletRequest, String code) throws IOException, URISyntaxException {
    logger.trace("BEGIN getToken()");
    TokenRequest tokenRequest = new TokenRequest.Builder()
      .resourceURI(URI.create(httpServletRequest.getRequestURL().toString().replaceFirst(httpServletRequest.getRequestURI(), OAUTH_TOKEN_CONTEXT_PATH)))
      .grantType(GrantType.AUTHORIZATION_CODE)
      .clientId(httpServletRequest.getServletContext().getInitParameter(CLIENT_ID_PROPERTY))
      .clientSecret(httpServletRequest.getServletContext().getInitParameter(CLIENT_SECRET_PROPERTY))
      .redirectionURI(URI.create(URIUtil.removeQueryParameter(httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString(), CODE, STATE)))
      .authorizationCode(code)
      .build();
    TokenResponse response = tokenRequest.execute();
    logger.trace("END getToken()");
    return response;
  }

  private void buildAuthorizationRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    logger.trace("BEGIN buildAuthorizationRequest()");
    try {
      /**
       * Create state param and save it to Cookie for checking in future to prevent 'Replay attacks'
       */
      State state = new State();
      Cookie stateCookie = new Cookie(RFI_OAUTH_CSRF_TOKEN, state.toString());
      stateCookie.setPath("/");
      stateCookie.setHttpOnly(true);
      httpServletResponse.addCookie(stateCookie);

      String authorizationRequestURI = new AuthorizationRequest.Builder()
        .resourceURI(URI.create(httpServletRequest.getRequestURL().toString().replaceFirst(httpServletRequest.getRequestURI(), OAUTH_AUTHORIZATION_CONTEXT_PATH)))
        .responseType(ResponseType.AUTHORIZATION_CODE)
        .clientId(httpServletRequest.getServletContext().getInitParameter(CLIENT_ID_PROPERTY))
        .redirectionURI(URI.create(URIUtil.removeQueryParameter(httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString(), CODE, STATE)))
        .state(state)
        .build()
        .toString();

      httpServletResponse.sendRedirect(authorizationRequestURI);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    logger.trace("END buildAuthorizationRequest()");
  }

  @Override
  public void doFilter(final ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    String path = request.getServletPath();//((HttpServletRequest) servletRequest).getRequestURL().toString().contains(url))
    String[] securityRoles = new String[0];
    for(String key :securityConstraints.descendingKeySet()) {
      Pattern pattern = Pattern.compile(key);
      Matcher matcher = pattern.matcher(path);
      if (matcher.lookingAt() || matcher.matches()) {
        securityRoles = securityConstraints.get(key);
        break;
      }
    }

    try {
      /**
       * If query contains CODE param, make a request to OAuth token endpoint;
       */
      if (request.getParameter(CODE) != null) {
        logger.trace("Request query has CODE param. Initialising token request");
        /**
         * Check for valid STATE param in query
         */
        boolean hasValidState = false;
        if (request.getCookies() != null) {
          for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(RFI_OAUTH_CSRF_TOKEN)) {
              logger.trace(hasValidState + " request: " + request.getParameter(STATE) + "; cookie: " + cookie.getValue());
              hasValidState = cookie.getValue().equals(request.getParameter(STATE));
              if (hasValidState) break;
            }
          }
        }
        if (!hasValidState) {
          logger.error("State param is not valid");
          throw new IllegalArgumentException("State param is not valid");
        }
        TokenResponse tokenObject = getToken(request, request.getParameter(CODE));
        if (tokenObject != null) {
          String token = tokenObject.getAccess_token();
          Cookie tokenCookie = new Cookie(RFI_OAUTH_TOKEN, token);
          tokenCookie.setPath("/");
          tokenCookie.setHttpOnly(true);
          response.addCookie(tokenCookie);
          response.sendRedirect(URIUtil.removeQueryParameter(request.getRequestURL().toString() + "?" + request.getQueryString(), CODE, STATE));
          return;
        } else {
          logger.error("Token request failed");
          throw new RuntimeException("Token request failed");
        }
      }
    } catch (Throwable th) {
      th.printStackTrace();
      if (securityRoles.length > 0) {
        buildAuthorizationRequest(request, response);
      }
      return;
    }

    request = new TokenRequestWrapper(request);
    if (securityRoles.length == 0) {
      /**
       * For public resource: authorize request, if it has token. (for cases where JepMainServiceServlet is public)
       */
       String token = ((TokenRequestWrapper)request).getTokenFromRequest();
       if (token != null) {
         ((TokenRequestWrapper)request).authorize();
       }
       filterChain.doFilter(request, servletResponse);
       return;
    }
    if (request.authenticate(response)) {
      if (securityRoles.length == 0 || ((TokenRequestWrapper) request).isUserInRoles(securityRoles)) {
        filterChain.doFilter(request, servletResponse);
      } else {
        response.sendError(403, "Access denied");
      }
    }
  }

  @Override
  public void destroy() {
    securityConstraints = null;
  }
}
