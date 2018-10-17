package org.jepria.server.service.rest;

import java.util.List;

import org.jepria.server.load.rest.SearchEntity;
import org.jepria.server.load.rest.SearchParamsDto;

public interface StandardSearchResourceController {

  /**
   * @param searchParamsDto
   * @param searchState search state corresponding to the particular invocation; not {@code null}
   * @param credential
   */
  void postSearch(SearchParamsDto searchParamsDto, SearchState searchState, Credential credential);
  
  /**
   * 
   * @param searchState search state corresponding to the particular invocation; not {@code null}
   * @param credential
   * @return search entity normally; {@code null} to indicate 'not found' status
   */
  SearchEntity getSearchEntity(SearchState searchState, Credential credential);
  
  /**
   * @param searchState search state corresponding to the particular invocation; not {@code null}
   * @param pageSize
   * @param page
   * @param credential
   * @return non-empty list normally; {@code null} or empty list to indicate 'not found' status
   */
  List<?> fetchData(SearchState searchState,
      Integer pageSize, 
      Integer page, 
      Credential credential);
}
