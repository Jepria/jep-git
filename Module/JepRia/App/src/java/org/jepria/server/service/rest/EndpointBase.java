package org.jepria.server.service.rest;

import io.swagger.annotations.Api;
import org.jepria.server.service.security.Credential;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Api
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Path("") // important
public class EndpointBase {

  protected EndpointBase() {}

  /**
   * Injectable field
   * @deprecated do not use the field in application endpoints, inject (override) own request explicitly instead
   */
  @Context
  @Deprecated
  protected HttpServletRequest request;

  /**
   * Get credential from the injected request
   * @return
   */
  protected Credential getCredential() {
    return new Credential() {
      @Override
      public int getOperatorId() {
        return 1; // server operatorId
        // TODO return a proper value
//        return (int)request.getAttribute("org.jepria.auth.jwt.OperatorId");
      }
    };
  }
}
