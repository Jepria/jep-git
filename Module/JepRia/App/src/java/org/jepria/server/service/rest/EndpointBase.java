package org.jepria.server.service.rest;

import org.jepria.server.service.security.Credential;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public class EndpointBase {

  protected EndpointBase() {}

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
        return 1; // server operatorId
        // TODO return a proper value
//        return (int)request.getAttribute("org.jepria.auth.jwt.OperatorId");
      }
    };
  }
}
