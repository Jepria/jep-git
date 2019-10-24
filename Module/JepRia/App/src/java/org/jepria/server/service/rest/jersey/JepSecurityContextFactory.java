package org.jepria.server.service.rest.jersey;

import org.jepria.server.service.security.JepSecurityContextAbstract;
import org.jepria.server.service.security.Credential;
import org.jepria.server.service.security.JaxrsCredential;
import org.jepria.server.service.security.JepSecurityContext;

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
        return new JaxrsCredential(() -> securityContext);
      }
    };

  }

}
