package org.jepria.server.service.rest;

import java.util.List;

import org.jepria.server.load.rest.SearchParamsDto;

public interface StandardResourceSearchController {

  public static class NoSuchSearchIdException extends Exception {
    private static final long serialVersionUID = 1L;

    private final String searchId;

    public String getSearchId() {
      return searchId;
    }

    public NoSuchSearchIdException(String searchId) {
      this.searchId = searchId;
    }
  }

  public static class GetResultsetDisallowedException extends Exception {
    private static final long serialVersionUID = 1L;
  }

  /**
   * 
   * @param searchParams
   * @return non-null
   */
  String postSearchRequest(SearchParamsDto searchParams, Credential credential);
  /**
   * 
   * @param searchId
   * @return non-null
   * @throws NoSuchSearchIdException if no search request found by the searchId 
   */
  SearchParamsDto getSearchParams(String searchId, Credential credential) throws NoSuchSearchIdException;

  /**
   * 
   * @param searchId
   * @param pageSize
   * @param page
   * @return
   * @throws NoSuchSearchIdException if no search request found by the searchId
   */
  List<?> fetchResultsetPaged(String searchId, int pageSize, int page, Credential credential) throws NoSuchSearchIdException;

  /**
   * 
   * @param searchId
   * @return
   * @throws NoSuchSearchIdException if no search request found by the searchId,
   * GetResultsetDisallowedException if getResultset is not allowed; use fetchResultsetPaged instead
   */
  List<?> getResultset(String searchId, Credential credential) throws NoSuchSearchIdException, GetResultsetDisallowedException;

  /**
   * 
   * @param searchId
   * @return
   * @throws NoSuchSearchIdException if no search request found by the searchId
   */
  int getResultsetSize(String searchId, Credential credential) throws NoSuchSearchIdException;
}
