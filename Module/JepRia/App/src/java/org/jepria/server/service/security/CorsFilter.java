package org.jepria.server.service.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

  protected String accessControlAllowOrigin;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    accessControlAllowOrigin = filterConfig.getInitParameter("org.jepria.server.service.security.CorsFilter.AccessControlAllowOrigin");
  }

  /**
   * A preflight request is an OPTIONS request
   * with an Origin header.
   */
  private boolean isPreflightRequest(HttpServletRequest request) {
    return request.getHeader("Origin") != null
            && request.getMethod().equalsIgnoreCase("OPTIONS");
  }

  /**
   * Method for ContainerResponseFilter.
   */
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    // Cross origin requests can be either simple requests
    // or preflight request. We need to add this header
    // to both type of requests. Only preflight requests
    // need the previously added headers.

    response.addHeader("Access-Control-Allow-Credentials", "true");
    response.addHeader(
            "Access-Control-Expose-Headers",
            "Origin, Content-Type, Accept, Authorization, Extended-Response, X-Cache-Control, Cache-Control, Location");

    final String accessControlAllowOriginRespHeaderValue;
    {
      if (accessControlAllowOrigin == null) {
        accessControlAllowOriginRespHeaderValue = request.getHeader("Origin");
      } else {
        accessControlAllowOriginRespHeaderValue = accessControlAllowOrigin;
      }
    }

    response.addHeader("Access-Control-Allow-Origin", accessControlAllowOriginRespHeaderValue);

    // If it is a preflight request, then we add all
    // the CORS headers here.
    if (isPreflightRequest(request)) {
      response.addHeader("Access-Control-Allow-Methods",
              "GET, POST, PUT, DELETE, OPTIONS, HEAD");
      response.addHeader("Access-Control-Allow-Headers",
              // Whatever other non-standard/safe headers (see list above)
              // you want the client to be able to send to the server,
              // put it in this list. And remove the ones you don't want.
              "X-Requested-With, Authorization, Accept-Version, Content-MD5, CSRF-Token, Cache-Control, X-Cache-Control, Content-Type");

      return;
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }


  @Override
  public void destroy() {

  }
}
