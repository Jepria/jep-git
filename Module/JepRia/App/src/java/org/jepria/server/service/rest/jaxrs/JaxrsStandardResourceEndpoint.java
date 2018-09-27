package org.jepria.server.service.rest.jaxrs;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jepria.server.load.rest.SearchEntity;
import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.rest.SearchState;
import org.jepria.server.service.rest.StandardResourceDescription;
import org.jepria.server.service.rest.jersey.ServletContainer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Standard Jaxrs REST resource
 * <br/><br/>
 * Example for copying into application resource to override one or more REST methods.
 * 
 * @final no need to override (annotations are not inherited and all the code will be duplicated in descendant)
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Api
public final class JaxrsStandardResourceEndpoint extends BaseStandardResourceEndpoint {
  
  /**
   * For reflective instantiation
   */
  public JaxrsStandardResourceEndpoint() {}
  
  /**
   * Injectable field
   */
  @Context 
  private HttpServletRequest request;
  
  @Override
  protected StandardResourceDescription getResourceDescription() {
    return (StandardResourceDescription)request.getAttribute(ServletContainer.STANDARD_RESOURCE_DESCRIPTION_REQUEST_ATTR);
  }
  
  @Override
  protected SearchState getSearchState(String searchId) {
    
    return new SearchState() {
      
      private static final String SESSION_KEY_SUFFIX_SEARCH_RESULT_RECORDS = "searchResultRecords";
      private static final String SESSION_KEY_SUFFIX_SEARCH_ENTITY = "searchEntity";
      private static final String SESSION_KEY_SUFFIX_REFRESH_FLAG = "refreshFlag";
      
      private String generateSessionKeyPrefix(String searchId) {
        return JaxrsStandardResourceEndpoint.class.getCanonicalName() + "[Resource=" + getResourceDescription().getResourceName() + ";SearchId=" + searchId + "].";
      }
      
      @Override
      public List<?> getSearchResultRecords() {
        return (List<?>)request.getSession().getAttribute(generateSessionKeyPrefix(searchId) + SESSION_KEY_SUFFIX_SEARCH_RESULT_RECORDS);
      }

      @Override
      public void setSearchResultRecords(List<?> searchResultRecords) {
        request.getSession().setAttribute(generateSessionKeyPrefix(searchId) + SESSION_KEY_SUFFIX_SEARCH_RESULT_RECORDS, searchResultRecords);
      }

      @Override
      public SearchEntity getSearchEntity() {
        return (SearchEntity)request.getSession().getAttribute(generateSessionKeyPrefix(searchId) + SESSION_KEY_SUFFIX_SEARCH_ENTITY);
      }

      @Override
      public void setSearchEntity(SearchEntity searchEntity) {
        request.getSession().setAttribute(generateSessionKeyPrefix(searchId) + SESSION_KEY_SUFFIX_SEARCH_ENTITY, searchEntity);
      }
      
      @Override
      public void setRefreshFlag(Boolean refreshFlag) {
        request.getSession().setAttribute(generateSessionKeyPrefix(searchId) + SESSION_KEY_SUFFIX_REFRESH_FLAG, refreshFlag);
      }
      
      @Override
      public Boolean getRefreshFlag() {
        return (Boolean)request.getSession().getAttribute(generateSessionKeyPrefix(searchId) + SESSION_KEY_SUFFIX_REFRESH_FLAG);
      }
    };
  }
  
  //////// CRUD ////////
  
  @GET
  @ApiOperation(value = "List this resource as options")
  @Override
  public Response listAsOptions() {
    return super.listAsOptions();
  }
  
  @GET
  @Path("{recordId}")
  @ApiOperation(value = "Get resource by ID")
  @Override
  public Response getResourceById(@PathParam("recordId") String recordId) {
    return super.getResourceById(recordId);
  }
  
  @POST
  @ApiOperation(value = "Create a new instance")
  @Override
  public Response create(Map<String, Object> instance) {
    return super.create(instance);
  }
  
  @DELETE
  @Path("{recordId}")
  @ApiOperation(value = "Delete resource by ID")
  @Override
  public Response deleteResourceById(@PathParam("recordId") String recordId) {
    return super.deleteResourceById(recordId);
  }
  
  @PUT
  @Path("{recordId}")
  @ApiOperation(value = "Update resource by ID")
  @Override
  public Response update(@PathParam("recordId") String recordId, Map<String, Object> fields) {
    return super.update(recordId, fields);
  }

  //////// OPTIONS ////////
  
  @GET
  @Path("options/{optionResourceName}")
  @ApiOperation(value = "List options")
  @Override
  public Response listOptions(@PathParam("optionResourceName") String optionResourceName) {
    return super.listOptions(optionResourceName);
  }
  
  //////// SEARCH ////////
  
  @POST
  @Path("search")
  @ApiOperation(value = "Post search params")
  @Override
  public Response postSearch(SearchParamsDto searchParamsDto) {
    return super.postSearch(searchParamsDto);
  }
  
  @GET
  @Path("search/{searchId}")
  @ApiOperation(value = "Get search entity by ID")
  @Override
  public Response getSearchEntity(
      @PathParam("searchId") String searchId) {
    return super.getSearchEntity(searchId);
  }
  
  @GET
  @Path("search/{searchId}/data/paged-by-{pageSize:\\d+}/{page}")
  @ApiOperation(value = "Fetch paged data")
  @Override
  public Response fetchData(
      @PathParam("searchId") String searchId,
      @PathParam("pageSize") Integer pageSize, 
      @PathParam("page") Integer page, 
      @QueryParam("sortField") String sortField,
      @QueryParam("sortOrder") String sortOrder) {
    return super.fetchData(searchId, pageSize, page, sortField, sortOrder);
  }
  
  
}