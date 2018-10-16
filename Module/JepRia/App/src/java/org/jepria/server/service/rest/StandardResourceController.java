package org.jepria.server.service.rest;

import java.util.List;
import java.util.Map;

import org.jepria.server.load.rest.SearchEntity;
import org.jepria.server.load.rest.SearchParamsDto;

public interface StandardResourceController {

  //////////// CRUD ///////////////////

  /**
   * @param recordId
   * @param operatorId
   * @return instance normally; {@code null} to indicate 'not found' status
   */
  Object getResourceById(String recordId, Integer operatorId);
  
  /**
   * @param instance
   * @param operatorId
   * @return created instance ID normally; never {@code null}
   */
  Object create(Map<String, Object> instance, Integer operatorId);
  
  void deleteResourceById(String recordId, Integer operatorId);
  
  void update(String recordId, Map<String, Object> fields, Integer operatorId);
  
  /////////////////////////// OPTIONS RESOURCE //////////////////////////
  
  /**
   * 
   * @param optionEntityName
   * @param operatorId
   * @return non-empty list normally; {@code null} or empty list to indicate 'not found' status
   */
  List<?> listOptions(String optionEntityName, Integer operatorId);
  
  
  /////////////////////////// SEARCH RESOURCE ///////////////////////////
  
  /**
   * @param searchParamsDto
   * @param searchState search state corresponding to the particular invocation; not {@code null}
   * @param operatorId
   */
  void postSearch(SearchParamsDto searchParamsDto, SearchState searchState, Integer operatorId);
  
  /**
   * 
   * @param searchState search state corresponding to the particular invocation; not {@code null}
   * @param operatorId
   * @return search entity normally; {@code null} to indicate 'not found' status
   */
  SearchEntity getSearchEntity(SearchState searchState, Integer operatorId);
  
  /**
   * @param searchState search state corresponding to the particular invocation; not {@code null}
   * @param pageSize
   * @param page
   * @param operatorId
   * @return non-empty list normally; {@code null} or empty list to indicate 'not found' status
   */
  List<?> fetchData(SearchState searchState,
      Integer pageSize, 
      Integer page, 
      Integer operatorId);
}
