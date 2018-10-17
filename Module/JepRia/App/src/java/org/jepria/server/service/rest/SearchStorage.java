package org.jepria.server.service.rest;

import java.util.Set;

import org.jepria.server.load.rest.SearchEntity;

/**
 * Minimal map interface
 */
public interface SearchStorage {
  SearchEntity get(String searchId);
  
  String put(SearchEntity searchEntity);
  
  SearchEntity update(String searchId, SearchEntity newSearchEntity);
  
  default SearchEntity remove(String searchId) {
    return update(searchId, null);
  }
  
  Set<String> keySet();
  
}
