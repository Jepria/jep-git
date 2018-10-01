package org.jepria.server.service.rest.jaxrs;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jepria.server.load.rest.SearchEntity;
import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.rest.SearchState;
import org.jepria.server.service.rest.StandardResourceController;
import org.jepria.server.service.rest.StandardResourceControllerImpl;
import org.jepria.server.service.rest.StandardResourceDescription;

/**
 * Base Jaxrs REST resource.
 * Bridge between {@link JaxrsStandardResourceEndpoint} and {@link StandardResourceController}
 * <br/><br/>
 * package visibility: only for extension by {@link JaxrsStandardResourceEndpoint}
 */
/*package*/abstract class BaseStandardResourceEndpoint {
  
  /**
   * Provides application resource description
   * @return
   */
  protected abstract StandardResourceDescription getResourceDescription();
  
  /**
   * Provides search state by the searchId (for stateful search)
   * @param searchId
   * @return
   */
  protected abstract SearchState getSearchState(String searchId);
  
  /**
   * Supplier protects the internal field from direct access from within the class members,
   * and initializes the field lazily (due to the DI: the injectable fields are being injected after the object construction)
   */
  private final Supplier<StandardResourceController> controller = new Supplier<StandardResourceController>() {
    private StandardResourceController instance = null;
    @Override
    public StandardResourceController get() {
      if (instance == null) {
        instance = createStandardResourceController();
      }
      return instance;
    }
  };
  
  protected StandardResourceController createStandardResourceController() {
    return new StandardResourceControllerImpl(getResourceDescription().getRecordDefinition(), getResourceDescription().getDao());
  }
  
  //////// CRUD ////////
  
  public Response listAsOptions() {
    return listOptions(getResourceDescription().getResourceName());
  }
  
  public Response getResourceById(String recordId) {
    Object record = controller.get().getResourceById(recordId);
    if (record == null) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(record).build();
    }
  }
  
  public Response create(Map<String, Object> instance) {
    Object createdId = controller.get().create(instance);
    return Response.status(Status.CREATED).header("Location", createdId).build(); 
  }
  
  public Response deleteResourceById(String recordId) {
    controller.get().deleteResourceById(recordId);
    return Response.ok().build();
  }
  
  public Response update(String recordId, Map<String, Object> fields) {
    controller.get().update(recordId, fields);
    return Response.ok().build();
  }

  //////// OPTIONS ////////
  
  public Response listOptions(String optionResourceName) {
    List<?> result = controller.get().listOptions(optionResourceName);
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
  
  //////// SEARCH ////////
  
  protected String generateSearchID(SearchParamsDto searchParamsDto) {
    return UUID.randomUUID().toString();
  }
  
  public Response postSearch(SearchParamsDto searchParamsDto) {
    
    final String searchId = generateSearchID(searchParamsDto);
    
    final SearchState searchState = getSearchState(searchId);
    
    controller.get().postSearch(searchParamsDto, searchState);
    if (searchId == null) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.status(Status.CREATED).header("Location", "search/" + searchId).build();
    }
  }
  
  public Response getSearchEntity(String searchId) {
    
    final SearchState searchState = getSearchState(searchId);
    
    SearchEntity result = controller.get().getSearchEntity(searchState);
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
    
    List<?> result = controller.get().fetchData(searchState, pageSize, page, sortField, sortOrder);
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
}