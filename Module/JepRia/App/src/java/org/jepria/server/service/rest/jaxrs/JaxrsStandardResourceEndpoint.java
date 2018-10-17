package org.jepria.server.service.rest.jaxrs;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jepria.server.load.rest.SearchEntity;
import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.rest.Credential;
import org.jepria.server.service.rest.SearchStorage;
import org.jepria.server.service.rest.SessionSearchStorage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Standard Jaxrs REST resource
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Api
public abstract class JaxrsStandardResourceEndpoint extends JaxrsStandardEndpointBase {
  
  protected JaxrsStandardResourceEndpoint() {}
  
  /**
   * Injectable field
   */
  @Context 
  private HttpServletRequest request;
  
  /**
   * Get credential from the request
   * @return
   */
  @Override
  protected Credential getCredential() {
    Integer operatorId = 1;
    // TODO = (Integer)request.getHeader("operatorId");
    return new Credential() {
      @Override
      public Integer getOperatorId() {
        return operatorId;
      }
    };
  }

  @Override
  protected SearchStorage getSearchStorage() {
    return new SessionSearchStorage(
        getResourceDescription().getResourceName(),
        new Supplier<HttpSession>() {
          @Override
          public HttpSession get() {
            return request.getSession();
          }
        });
  }
  
  //////// CRUD ////////
  
  @GET
  @ApiOperation(value = "List this resource as options")
  public Response listAsOptions() {
    return listOptions(getResourceDescription().getResourceName());
  }
  
  @GET
  @Path("{recordId}")
  @ApiOperation(value = "Get resource by ID")
  public Response getResourceById(@PathParam("recordId") String recordId) {
    Object record = controller.get().getResourceById(recordId, getCredential());
    if (record == null) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(record).build();
    }
  }
  
  @POST
  @ApiOperation(value = "Create a new instance")
  public Response create(Map<String, Object> instance) {
    Object createdId = controller.get().create(instance, getCredential());
    return Response.status(Status.CREATED).header("Location", createdId).build();
  }
  
  @DELETE
  @Path("{recordId}")
  @ApiOperation(value = "Delete resource by ID")
  public Response deleteResourceById(@PathParam("recordId") String recordId) {
    controller.get().deleteResourceById(recordId, getCredential());
    return Response.ok().build();
  }
  
  @PUT
  @Path("{recordId}")
  @ApiOperation(value = "Update resource by ID")
  public Response update(@PathParam("recordId") String recordId, Map<String, Object> fields) {
    controller.get().update(recordId, fields, getCredential());
    return Response.ok().build();
  }

  //////// OPTIONS ////////
  
  @GET
  @Path("option/{optionEntityName}")
  @ApiOperation(value = "List options by option-entity name")
  public Response listOptions(@PathParam("optionEntityName") String optionEntityName) {
    List<?> result = controller.get().listOptions(optionEntityName, getCredential());
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
  
  //////// SEARCH ////////
  
  @POST
  @Path("search")
  @ApiOperation(value = "Post search params")
  public Response postSearch(SearchParamsDto searchParamsDto) {
    
    final String searchId = controller.get().postSearch(searchParamsDto, getCredential());
    
    if (searchId == null) {
      // TODO
      throw new IllegalStateException("searchId must not be null");
    }
    
    return Response.status(Status.CREATED).header("Location", "search/" + searchId).build();
  }
  
  @GET
  @Path("search/{searchId}")
  @ApiOperation(value = "Get search entity by ID")
  public Response getSearchEntity(
      @PathParam("searchId") String searchId) {

    SearchEntity result = controller.get().getSearchEntity(searchId, getCredential());
    if (result == null) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
  
  @GET
  @Path("search/{searchId}/data/paged-by-{pageSize:\\d+}/{page}")
  @ApiOperation(value = "Fetch paged data")
  public Response fetchData(
      @PathParam("searchId") String searchId,
      @PathParam("pageSize") Integer pageSize, 
      @PathParam("page") Integer page) {

    List<?> result = controller.get().fetchData(searchId, pageSize, page, getCredential());
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
  
  
}