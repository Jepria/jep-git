package org.jepria.server.service.security;

import org.jepria.server.service.rest.SecurityContextWrapper;

import javax.ws.rs.core.SecurityContext;

public abstract class JepSecurityContextAbstract extends SecurityContextWrapper implements JepSecurityContext {
  public JepSecurityContextAbstract(SecurityContext securityContext) {
    super(securityContext);
  }
}
