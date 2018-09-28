package org.jepria.server.service.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jepria.server.load.rest.SearchEntity;

/**
 * Default HttpSession-based search state 
 */
public class SearchStateImpl implements SearchState {
  
  
  private static final String SESSION_KEY_SUFFIX_SEARCH_RESULT_RECORDS = "searchResultRecords";
  private static final String SESSION_KEY_SUFFIX_SEARCH_ENTITY = "searchEntity";
  private static final String SESSION_KEY_SUFFIX_REFRESH_FLAG = "refreshFlag";
  
  
  private final HttpServletRequest request;
  private final String searchId;
  
  /**
   * Name of the resource which the search state related to. Used for debugging/logging only  
   */
  private final String resourceName;
  
  public SearchStateImpl(HttpServletRequest request, String searchId, String resourceName) {
    this.request = request;
    this.searchId = searchId;
    this.resourceName = resourceName == null ? "?" : resourceName;
  }

  private String generateSessionKeyPrefix(String searchId) {
    return "Endpoint[Resource=" + resourceName + ";SearchId=" + searchId + "].";
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

}
