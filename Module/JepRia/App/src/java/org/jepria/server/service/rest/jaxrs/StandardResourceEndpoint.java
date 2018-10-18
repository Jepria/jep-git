package org.jepria.server.service.rest.jaxrs;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
import javax.ws.rs.core.UriInfo;

import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.rest.ResourceSearchController;
import org.jepria.server.service.rest.ResourceSearchController.NoSuchSearchIdException;
import org.jepria.server.service.rest.ResourceSearchController.UnsupportedMethodException;
import org.jepria.server.service.rest.ResourceSearchControllerSession;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Standard Jaxrs REST resource
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Api
public abstract class StandardResourceEndpoint extends StandardEndpointBase {
  
  protected StandardResourceEndpoint() {}
  
  /**
   * Supplier protects the internal field from direct access from within the class members,
   * and initializes the field lazily (due to the DI: the injectable fields are being injected after the object construction)
   */
  protected final Supplier<ResourceSearchController> searchController = new Supplier<ResourceSearchController>() {
    private ResourceSearchController instance = null;
    @Override
    public ResourceSearchController get() {
      if (instance == null) {
        instance = createSearchController();
      }
      return instance;
    }
  };
  
  protected ResourceSearchController createSearchController() {
    return new ResourceSearchControllerSession(
        getResourceDescription(),
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
    Object record = resourceController.get().getResourceById(recordId, getCredential());
    if (record == null) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(record).build();
    }
  }
  
  @POST
  @ApiOperation(value = "Create a new instance")
  public Response create(Map<String, Object> instance) {
    Object createdId = resourceController.get().create(instance, getCredential());
    return Response.status(Status.CREATED).header("Location", createdId).build();
  }
  
  @DELETE
  @Path("{recordId}")
  @ApiOperation(value = "Delete resource by ID")
  public Response deleteResourceById(@PathParam("recordId") String recordId) {
    resourceController.get().deleteResourceById(recordId, getCredential());
    return Response.ok().build();
  }
  
  @PUT
  @Path("{recordId}")
  @ApiOperation(value = "Update resource by ID")
  public Response update(@PathParam("recordId") String recordId, Map<String, Object> fields) {
    resourceController.get().update(recordId, fields, getCredential());
    return Response.ok().build();
  }

  //////// OPTIONS ////////
  
  @GET
  @Path("option/{optionEntityName}")
  @ApiOperation(value = "List options by option-entity name")
  public Response listOptions(@PathParam("optionEntityName") String optionEntityName) {
    List<?> result = resourceController.get().listOptions(optionEntityName, getCredential());
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
  /**
   * @param searchParams
   * @param uriInfo injectable parameter for building correct "Location" response header
   * @return
   */
  public Response postSearch(SearchParamsDto searchParams, @Context UriInfo uriInfo) {
    final String searchId = searchController.get().postSearchRequest(searchParams, getCredential());
    // ссылка на созданный ресурс
    final String location = uriInfo.getPath() + "/" + searchId;
    return Response.status(Status.CREATED).header("Location", location).build();
  }
  
  @GET
  @Path("search/{searchId}")
  @ApiOperation(value = "Get search request by ID")
  public Response getSearchRequest(
      @PathParam("searchId") String searchId) {
    final SearchParamsDto result;
    
    try {
      result = searchController.get().getSearchParams(searchId, getCredential());
    } catch (NoSuchSearchIdException e) {
      // TODO log?
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(result).build();
  }
  
  @GET
  @Path("search/{searchId}/resultset-size")
  @ApiOperation(value = "Get search resultset size")
  public Response getSearchResultsetSize(
      @PathParam("searchId") String searchId) {
    final int result;
    
    try {
      result = searchController.get().getResultsetSize(searchId, getCredential());
    } catch (NoSuchSearchIdException e) {
      // TODO log?
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(result).build();
  }
  
  @GET
  @Path("search/{searchId}/resultset")
  @ApiOperation(value = "Get entire resultset")
  public Response getResultset(
      @PathParam("searchId") String searchId) {
    final List<?> result;
    
    try {
      result = searchController.get().getEntireResultset(searchId, getCredential());
    } catch (NoSuchSearchIdException e) {
      // TODO log?
      return Response.status(Status.NOT_FOUND).build();
    } catch (UnsupportedMethodException e) {
      // TODO correct to use 405 here?
      return Response.status(Status.METHOD_NOT_ALLOWED).build();
    }
    
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
  
  
  private static final int DEFAULT_PAGE_SIZE = 25;
  
  @GET
  @Path("search/{searchId}/resultset/paged-by-{pageSize:\\d+}/{page}")
  @ApiOperation(value = "Fetch resultset paged")
  public Response fetchResultsetPaged(
      @PathParam("searchId") String searchId,
      @PathParam("pageSize") Integer pageSize, 
      @PathParam("page") Integer page) {
    
    // normalize paging parameters
    pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
    page = page == null ? 1 : page;
    
    final List<?> result;
    
    try {
      result = searchController.get().fetchResultsetPaged(searchId, pageSize, page, getCredential());
    } catch (NoSuchSearchIdException e) {
      // TODO log?
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
  
  
}