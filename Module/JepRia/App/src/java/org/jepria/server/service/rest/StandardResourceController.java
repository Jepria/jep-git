package org.jepria.server.service.rest;

import java.util.List;
import java.util.Map;

import org.jepria.server.load.rest.SearchEntity;
import org.jepria.server.load.rest.SearchParamsDto;

public interface StandardResourceController {

  //////////// CRUD ///////////////////

  /**
   * @param recordId
   * @return instance normally; {@code null} to indicate 'not found' status
   */
  Object getResourceById(String recordId);
  
  /**
   * @param instance
   * @return created instance ID normally; never {@code null}
   */
  Object create(Map<String, Object> instance);
  
  void deleteResourceById(String recordId);
  
  void update(String recordId, Map<String, Object> fields);
  
  /////////////////////////// OPTIONS RESOURCE //////////////////////////
  
  /**
   * 
   * @param optionResourceName
   * @return non-empty list normally; {@code null} or empty list to indicate 'not found' status
   */
  List<?> listOptions(String optionResourceName);
  
  
  /////////////////////////// SEARCH RESOURCE ///////////////////////////
  
  /**
   * @param searchParamsDto
   * @param searchState search state corresponding to the particular invocation; not {@code null}
   */
  void postSearch(SearchParamsDto searchParamsDto, SearchState searchState);
  
  /**
   * 
   * @param searchState search state corresponding to the particular invocation; not {@code null}
   * @return search entity normally; {@code null} to indicate 'not found' status
   */
  SearchEntity getSearchEntity(SearchState searchState);
  
  /**
   * @param searchState search state corresponding to the particular invocation; not {@code null}
   * @param pageSize
   * @param page
   * @param sortField
   * @param sortOrder
   * @return non-empty list normally; {@code null} or empty list to indicate 'not found' status
   */
  List<?> fetchData(SearchState searchState,
      Integer pageSize, 
      Integer page, 
      String sortField,
      String sortOrder);
}
