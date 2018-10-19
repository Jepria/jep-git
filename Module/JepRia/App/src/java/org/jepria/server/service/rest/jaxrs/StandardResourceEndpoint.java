package org.jepria.server.service.rest.jaxrs;

import java.net.URI;
import java.util.HashMap;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.uri.UriTemplate;
import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.rest.ResourceSearchController;
import org.jepria.server.service.rest.ResourceSearchController.MaxResultsetSizeExceedException;
import org.jepria.server.service.rest.ResourceSearchController.NoSuchSearchIdException;
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

  // public static for Jersey's introspection only
  public static class PostSearchReturnSubresponseDto {
    public Integer status;
    public String reasonPhrase;
    public Object entity;
  }
  
  @POST
  @Path("search")
  @ApiOperation(value = "Post search params")
  /**
   * @param searchParams
   * @param uriInfo injectable parameter for building correct "Location" response header
   * @return
   */
  public Response postSearch(SearchParamsDto searchParams/*, @Context UriInfo uriInfo, @HeaderParam("jepria-postSearch-return") Map<String, String> postSearchReturnHeaderValue*/) {
    final String searchId = searchController.get().postSearchRequest(searchParams, getCredential());
    
    // ссылка на созданный ресурс
    final URI location = URI.create(request.getRequestURL() + "/" + searchId);
    final ResponseBuilder response = Response.created(location);
    
    
    // клиент может запросить результаты поиска непосредственно в ответе данного запроса
    final String postSearchReturnHeaderValue = request.getHeader("jepria-postSearch-return");
    if (postSearchReturnHeaderValue != null) {
      try {
        Map<String, Object> subrequest = new HashMap<>();
        
        final String subrequestUrl = 
            URI.create(request.getRequestURL() + "/" + searchId + "/" + postSearchReturnHeaderValue).toString();
        subrequest.put("jepria-postSearch-return__subrequest-url", subrequestUrl);
        
        PostSearchReturnSubresponseDto subresponse = getPostSearchReturnSubresponse(postSearchReturnHeaderValue, searchId);
        subrequest.put("jepria-postSearch-return__subresponse", subresponse);
        
        response.entity(subrequest);
        
      } catch (IllegalPostSearchReturnHeaderValue e) {
        response.status(Status.BAD_REQUEST.getStatusCode(), "Illegal 'jepria-postSearch-return' header value: " + e.getIllegalValue());
      }
    }
    
    return response.build();
  }
  
  private class IllegalPostSearchReturnHeaderValue extends Exception {
    private static final long serialVersionUID = 1L;
    
    private final String illegalValue;
    
    public IllegalPostSearchReturnHeaderValue(String illegalValue) {
      this.illegalValue = illegalValue;
    }
    
    public String getIllegalValue() {
      return illegalValue;
    }
  }
  
  protected PostSearchReturnSubresponseDto getPostSearchReturnSubresponse(String postSearchReturnHeaderValue, String searchId) throws IllegalPostSearchReturnHeaderValue {
    if (postSearchReturnHeaderValue == null) {
      return null;
    }
    
    final PostSearchReturnSubresponseDto postSearchReturnData = new PostSearchReturnSubresponseDto();

    
    UriTemplate uriTemplate;
    Map<String, String> uriTemplateMap;

    // check getResultset
    uriTemplate = new UriTemplate(URI_TEMPLATE__GET_RESULTSET);
    uriTemplateMap = new HashMap<>();
    if (uriTemplate.match(postSearchReturnHeaderValue, uriTemplateMap)) {
      Response subresponse = getResultset(searchId);
      postSearchReturnData.status = subresponse.getStatus();
      postSearchReturnData.reasonPhrase = subresponse.getStatusInfo().getReasonPhrase();
      postSearchReturnData.entity = subresponse.getEntity();
      return postSearchReturnData;
    }
    
    
    // check getResultsetPaged
    uriTemplate = new UriTemplate(URI_TEMPLATE__GET_RESULTSET_PAGED);
    uriTemplateMap = new HashMap<>();
    if (uriTemplate.match(postSearchReturnHeaderValue, uriTemplateMap)) {
      Integer pageSize = Integer.parseInt(uriTemplateMap.get("pageSize"));
      Integer page = Integer.parseInt(uriTemplateMap.get("page"));
      
      Response subresponse = getResultsetPaged(searchId, pageSize, page);
      postSearchReturnData.status = subresponse.getStatus();
      postSearchReturnData.reasonPhrase = subresponse.getStatusInfo().getReasonPhrase();
      postSearchReturnData.entity = subresponse.getEntity();
      return postSearchReturnData;
    }
    
    
    throw new IllegalPostSearchReturnHeaderValue(postSearchReturnHeaderValue);
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
  
  private static final String URI_TEMPLATE__GET_RESULTSET = "resultset";
  private static final String URI_TEMPLATE__GET_RESULTSET_PAGED = "resultset/paged-by-{pageSize:\\d+}/{page}";
  
  @GET
  @Path("search/{searchId}/" + URI_TEMPLATE__GET_RESULTSET)
  @ApiOperation(value = "Get whole resultset")
  public Response getResultset(
      @PathParam("searchId") String searchId) {
    
    final List<?> result;
    
    try {
      result = searchController.get().getResultset(searchId, getCredential());
    } catch (NoSuchSearchIdException e) {
      // TODO log?
      return Response.status(Status.NOT_FOUND).build();
    } catch (MaxResultsetSizeExceedException e) {
      // TODO log?
      return Response.status(Status.BAD_REQUEST.getStatusCode(), e.getMessage()).build();
    }
    
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
  
  private static final int DEFAULT_PAGE_SIZE = 25;
  
  @GET
  @Path("search/{searchId}/" + URI_TEMPLATE__GET_RESULTSET_PAGED)
  @ApiOperation(value = "Get resultset paged")
  public Response getResultsetPaged(
      @PathParam("searchId") String searchId,
      @PathParam("pageSize") Integer pageSize, 
      @PathParam("page") Integer page) {
    
    // normalize paging parameters
    pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
    page = page == null ? 1 : page;
    
    final List<?> result;
    
    try {
      result = searchController.get().getResultsetPaged(searchId, pageSize, page, getCredential());
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