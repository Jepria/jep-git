package org.jepria.server.service.rest.jersey;

import org.jepria.server.service.security.Credential;
import org.jepria.server.service.security.JaxrsCredential;
import org.jepria.server.service.security.JepSecurityContext;
import org.jepria.server.service.security.JepSecurityContextAbstract;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.function.Supplier;

public class JepSecurityContextFactory implements Supplier<JepSecurityContext> {

  @Context
  private SecurityContext securityContext;

  @Override
  public JepSecurityContext get() {

    return new JepSecurityContextAbstract(securityContext) {
      @Override
      public Credential getCredential() {
        if (securityContext.getUserPrincipal() == null) {
          // the user has not been authenticated, thus no credential
          return null;
        } else {
          // the user has been authenticated
          return new JaxrsCredential(securityContext);
        }
      }
    };

  }

}
