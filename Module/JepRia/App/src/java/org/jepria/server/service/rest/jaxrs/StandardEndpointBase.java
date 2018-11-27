package org.jepria.server.service.rest.jaxrs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.jepria.server.security.Credential;

public abstract class StandardEndpointBase {

  protected StandardEndpointBase() {}
  
  /**
   * Injectable field
   */
  @Context 
  protected HttpServletRequest request;
  
  /**
   * Get credential from the injected request
   * @return
   */
  protected Credential getCredential() {
    return new Credential() {
      @Override
      public int getOperatorId() {
        return 1;
        // TODO
//        return (int)request.getAttribute("org.jepria.auth.jwt.OperatorId");
      }
    };
  }
}
