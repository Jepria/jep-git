package org.jepria.server.service.rest.jaxrs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Standard Jaxrs REST resource for light-weight options on the application level
 */
@Path("/option")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Api
public abstract class StandardApplicationOptionEndpoint extends StandardEndpointBase {
  
  protected StandardApplicationOptionEndpoint() {}
  
  @GET
  @Path("{optionEntityName}")
  @ApiOperation(value = "List options by option-entity name")
  public Response listOptions(@PathParam("optionEntityName") String optionEntityName) {
    List<?> result = resourceController.get().listOptions(optionEntityName, getCredential());
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
}