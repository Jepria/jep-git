package org.jepria.server.service.rest;

import io.swagger.annotations.Api;
import org.jepria.server.service.security.Credential;
import org.jepria.server.service.security.PrincipalImpl;
import org.jepria.ssoutils.JepPrincipal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Api
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Path("") // important
/**
 * jaxrs-адаптер (транспортный слой)
 * <br/>
 * <i>В устаревшей терминологии: endpoint, EndpointBase</i>
 * <br/>
 */
public class JaxrsAdapterBase {

  protected JaxrsAdapterBase() {}

  /**
   * Injectable field
   * @deprecated do not use the field in application adapters, inject (override) own request explicitly instead
   */
  @Context
  @Deprecated
  protected HttpServletRequest request;

  @Context
  @Deprecated
  protected SecurityContext securityContext;

  /**
   * Get credential from the injected request
   * @return
   */
  protected Credential getCredential() {
    return new Credential() {
      @Override
      public int getOperatorId() {
        if (securityContext != null && securityContext.getUserPrincipal() != null) {
          Principal principal = securityContext.getUserPrincipal();

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

        } else {
          // TODO is it correct to return Server operiatorId if the user is not logged in?
          return 1;// operatorId = Server
        }
      }
    };
  }
}
