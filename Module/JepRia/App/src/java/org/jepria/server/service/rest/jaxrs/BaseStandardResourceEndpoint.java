package org.jepria.server.service.rest.jaxrs;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jepria.server.load.rest.SearchEntity;
import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.rest.SearchState;
import org.jepria.server.service.rest.StandardResourceController;
import org.jepria.server.service.rest.StandardResourceDescription;

/**
 * Base Jaxrs REST resource.
 * Bridge between Jaxrs and {@link StandardResourceController}
 */
/*package*/abstract class BaseStandardResourceEndpoint {
  
  private final StandardResourceController controller = new StandardResourceController(
      () -> getResourceDescription().getRecordDefinition(), 
      () -> getResourceDescription().getDao());
  
  protected abstract StandardResourceDescription getResourceDescription();
  
  protected abstract SearchState getSearchState(String searchId);
  
  //////// CRUD ////////
  
  public Response listAsOptions() {
    return listOptions(getResourceDescription().getResourceName());
  }
  
  public Response getResourceById(String recordId) {
    Object record = controller.getResourceById(recordId);
    if (record == null) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(record).build();
    }
  }
  
  public Response create(Map<String, Object> instance) {
    Object createdId = controller.create(instance);
    return Response.status(Status.CREATED).header("Location", createdId).build(); 
  }
  
  public Response deleteResourceById(String recordId) {
    controller.deleteResourceById(recordId);
    return Response.ok().build();
  }
  
  public Response update(String recordId, Map<String, Object> fields) {
    controller.update(recordId, fields);
    return Response.ok().build();
  }

  //////// OPTIONS ////////
  
  public Response listOptions(String optionResourceName) {
    List<?> result = controller.listOptions(optionResourceName);
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
  
  //////// SEARCH ////////
  
  private static String generateSearchID(SearchParamsDto searchParamsDto) {
    return UUID.randomUUID().toString();
  }
  
  public Response postSearch(SearchParamsDto searchParamsDto) {
    
    final String searchId = generateSearchID(searchParamsDto);
    
    final SearchState searchState = getSearchState(searchId);
    
    controller.postSearch(searchParamsDto, searchState);
    if (searchId == null) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.status(Status.CREATED).header("Location", "search/" + searchId).build();
    }
  }
  
  public Response getSearchEntity(String searchId) {
    
    final SearchState searchState = getSearchState(searchId);
    
    SearchEntity result = controller.getSearchEntity(searchState);
    if (result == null) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
  
  public Response fetchData(String searchId,
      Integer pageSize, 
      Integer page, 
      String sortField,
      String sortOrder) {
    
    final SearchState searchState = getSearchState(searchId);
    
    List<?> result = controller.fetchData(searchState, pageSize, page, sortField, sortOrder);
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
}