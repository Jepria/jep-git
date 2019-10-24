package org.jepria.server.service.rest;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class SecurityContextWrapper implements SecurityContext {

  protected final SecurityContext context;

  public SecurityContextWrapper(SecurityContext context) {
    this.context = context;
  }

  @Override
  public Principal getUserPrincipal() {
    return context.getUserPrincipal();
  }

  @Override
  public boolean isUserInRole(String s) {
    return context.isUserInRole(s);
  }

  @Override
  public boolean isSecure() {
    return context.isSecure();
  }

  @Override
  public String getAuthenticationScheme() {
    return context.getAuthenticationScheme();
  }
}
