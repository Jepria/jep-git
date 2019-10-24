package org.jepria.server.service.security;

import org.jepria.ssoutils.JepPrincipal;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.function.Supplier;

public class JaxrsCredential implements Credential {

  protected final Supplier<SecurityContext> securityContext;

  public JaxrsCredential(Supplier<SecurityContext> securityContext) {
    this.securityContext = securityContext;
  }

  @Override
  public int getOperatorId() {
    final Principal principal = securityContext.get().getUserPrincipal();

    if (principal != null) {
      if (principal instanceof PrincipalImpl) {
        return ((PrincipalImpl) principal).getOperatorId();

      } else if (principal instanceof JepPrincipal) {
        // backward compat with ssoutils
        return ((JepPrincipal) principal).getOperatorId();

      } else if (principal == null) {
        throw new IllegalStateException("Null principal");
      } else {
        throw new IllegalStateException("Unknown principal type: " + principal.getClass().getCanonicalName());
      }
    }

    // TODO is it correct to return Server operiatorId if the user is not logged in?
    return 1;// operatorId = Server
  }

}
