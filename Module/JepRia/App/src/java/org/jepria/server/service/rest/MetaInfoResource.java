package org.jepria.server.service.rest;

import org.jepria.server.service.rest.gson.JsonConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/meta")
public class MetaInfoResource extends JaxrsAdapterBase {

  @GET
  @Path("/current-user")
  @JsonConfig(serializeNulls = true)
  public Response getCurrentUser() {
    Map<String, Object> responseBodyMap = new HashMap<>();
    responseBodyMap.put("username", securityContext.getCredential().getUsername());
    responseBodyMap.put("operatorId", securityContext.getCredential().getOperatorId());
    return Response.ok(responseBodyMap).build();
  }

  @GET
  @Path("/current-user/test-roles")
  public Response testCurrentUserRoles(@QueryParam("roles") String roles) {
    Map<String, Object> responseBodyMap = new LinkedHashMap<>();

    if (roles != null) {
      String[] tokens = roles.split("[~,;]"); // support '~' because it is a delimiter for a composite primary key. TODO support also both ,; delimiters?
      for (String role: tokens) {
        if (!"".equals(role)) {
          responseBodyMap.put(role, securityContext.isUserInRole(role) ? 1 : 0);
        }
      }
    }

    return Response.ok(responseBodyMap).build();
  }

  @GET
  @Path("/current-user/test-role")
  public Response testCurrentUserRole(@QueryParam("role") String role) {
    Map<String, Object> responseBodyMap = new LinkedHashMap<>();

    if (role != null && !"".equals(role)) {
      responseBodyMap.put(role, securityContext.isUserInRole(role) ? 1 : 0);
    }

    return Response.ok(responseBodyMap).build();
  }
}
