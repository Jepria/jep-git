package org.jepria.server.service.rest;

import java.util.List;

import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.security.Credential;

/**
 * The interface has CREATE and READ methods only.
 * UPDATE or DELETE methods (recycling and cleaning strategy) are implementation-dependent
 */
public interface ResourceSearchController {

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

  /**
   * Exception indicates that 
   */
  public static class UnsupportedMethodException extends Exception {
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
  List<?> getResultsetPaged(String searchId, int pageSize, int page, Credential credential) throws NoSuchSearchIdException;

  /**
   * 
   * @param searchId
   * @param credential
   * @return
   * @throws NoSuchSearchIdException if no search request found by the searchId,
   * @throws UnsupportedMethodException if {@link #getResultset} is not supported for the searchId (the resultset is too large).
   * Then use {@link #fetchResultsetPaged} instead
   */
  List<?> getResultset(String searchId, Credential credential) throws NoSuchSearchIdException, UnsupportedMethodException;

  /**
   * @param searchId
   * @return
   * @throws NoSuchSearchIdException if no search request found by the searchId
   */
  int getResultsetSize(String searchId, Credential credential) throws NoSuchSearchIdException;
}
