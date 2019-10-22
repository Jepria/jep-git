package com.technology.jep.jepria.server.security.servlet;

import com.technology.jep.jepria.server.security.TokenRequestWrapper;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

public class SecurityFilter implements Filter {

  private ServletContext servletContext;
  public static final String SECURITY_ROLE = "security-roles";
  public static final String PUBLIC = "public-resources";
  private String rolesString = "";
  private Set<String> roles = new HashSet<>();
  private Set<String> publicResources = new HashSet<>();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    servletContext = filterConfig.getServletContext();
    String securityRoles = filterConfig.getInitParameter(SECURITY_ROLE);
    if (securityRoles != null && securityRoles.length() > 0) {
      String[] roles = securityRoles.split("\\s*,\\s*");
      for (String role : roles) {
        this.roles.add(role.trim());
        rolesString += "'" + role + "',";
      }
      if (rolesString.length() > 0) {
        rolesString = rolesString.trim().substring(0, rolesString.length() - 1);
      }
    }
    String publicResources = filterConfig.getInitParameter(PUBLIC);
    if (publicResources != null && publicResources.length() > 0) {
      String[] urls = publicResources.split("\\s*,\\s*");
      for (String url : urls) {
        this.publicResources.add(url.trim());
      }
    }
  }

  @Override
  public void doFilter(final ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    if (publicResources.stream().anyMatch(url -> ((HttpServletRequest) servletRequest).getRequestURL().toString().contains(url))) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }
    request = new TokenRequestWrapper(request);
    if (request.authenticate(response)) {
      if (request.isUserInRole(rolesString)) {
        filterChain.doFilter(request, servletResponse);
      } else {
        response.sendError(403, "Access denied");
      }
    }
  }

  @Override
  public void destroy() {
    roles = null;
    publicResources = null;
  }
}
