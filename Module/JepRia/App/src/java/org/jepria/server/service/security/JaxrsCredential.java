package org.jepria.server.service.security;

import org.jepria.ssoutils.JepPrincipal;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;


public class JaxrsCredential implements Credential {

  protected final SecurityContext securityContext;

  public JaxrsCredential(SecurityContext securityContext) {
    this.securityContext = securityContext;
  }

  @Override
  public int getOperatorId() {
    final Principal principal = securityContext.getUserPrincipal();

    if (principal != null) {
      if (principal instanceof PrincipalImpl) {
        return ((PrincipalImpl) principal).getOperatorId();
    
      } else if (principal instanceof JepPrincipal) {
        // backward compat with ssoutils
        return ((JepPrincipal) principal).getOperatorId();
    
      } else {
        throw new IllegalStateException("Unknown principal type: " + principal.getClass().getCanonicalName());
      }
    } else {
      throw new IllegalStateException("Principal expected no be not null");
    }
  }

  @Override
  public String getUsername() {
    final Principal principal = securityContext.getUserPrincipal();
    return principal == null ? null : principal.getName();
  }

  @Override
  public boolean isUserInRole(String roleShortName) {
    return securityContext.isUserInRole(roleShortName);
  }
}
