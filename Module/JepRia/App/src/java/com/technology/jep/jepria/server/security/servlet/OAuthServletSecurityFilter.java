package com.technology.jep.jepria.server.security.servlet;

import com.technology.jep.jepria.server.security.OAuthRequestWrapper;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;


/**
 * <pre>
 * Фильтр для back-end сервлетов/сервисов.
 * @see <a href="http://google.com">https://github.com/Jepria/jepria-showcase</a>
 * </pre>
 */
public class OAuthServletSecurityFilter extends MultiInstanceSecurityFilter {

  private static Logger logger = Logger.getLogger(OAuthServletSecurityFilter.class.getName());

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    if (isSubPath(request.getServletPath())) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }

    OAuthRequestWrapper oauthRequest = request instanceof OAuthRequestWrapper ? (OAuthRequestWrapper) request : new OAuthRequestWrapper(request);

    if (securityRoles.size() == 0) {
      /**
       * For public resource: authorize request, if it has token. (for cases where JepMainServiceServlet is public)
       */
      String token = oauthRequest.getTokenFromRequest();
      if (token != null) {
        oauthRequest.authenticate(response);
      }
      filterChain.doFilter(oauthRequest, servletResponse);
      return;
    }

    if (oauthRequest.authenticate(response)) {
      if (securityRoles.stream().anyMatch(oauthRequest::isUserInRole)) {
        filterChain.doFilter(oauthRequest, servletResponse);
      } else {
        response.sendError(SC_FORBIDDEN, "Access denied");
      }
    } else {
      response.sendError(SC_UNAUTHORIZED, "access token is invalid or has expired");
      return;
    }
  }

  @Override
  public void destroy() {

  }
}
