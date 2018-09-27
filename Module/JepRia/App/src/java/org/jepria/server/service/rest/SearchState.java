package org.jepria.server.service.rest;

import java.util.List;

import org.jepria.server.load.rest.SearchEntity;

public interface SearchState {
  
  List<?> getSearchResultRecords();
  
  void setSearchResultRecords(List<?> searchResults);
  
  SearchEntity getSearchEntity();
  
  void setSearchEntity(SearchEntity searchEntity);
  
  Boolean getRefreshFlag();
  
  void setRefreshFlag(Boolean refreshFlag);
}
