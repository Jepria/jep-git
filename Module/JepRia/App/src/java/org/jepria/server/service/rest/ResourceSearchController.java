package org.jepria.server.service.rest;

import java.util.List;
import java.util.NoSuchElementException;

import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.security.Credential;

/**
 * The interface has CREATE and READ methods only.
 * UPDATE or DELETE methods (recycling and cleaning strategy) are implementation-dependent
 */
public interface ResourceSearchController {

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
   * @throws NoSuchElementException if the requested searchId does not exist 
   */
  SearchParamsDto getSearchParams(String searchId, Credential credential) throws NoSuchElementException;

  /**
   * 
   * @param searchId
   * @param pageSize
   * @param page
   * @return
   * @throws NoSuchElementException if the requested searchId does not exist
   */
  List<?> getResultsetPaged(String searchId, int pageSize, int page, Credential credential) throws NoSuchElementException;

  /**
   * 
   * @param searchId
   * @param credential
   * @return
   * @throws NoSuchElementException if the requested searchId does not exist
   */
  List<?> getResultset(String searchId, Credential credential) throws NoSuchElementException;

  /**
   * @param searchId
   * @return
   * @throws NoSuchElementException if the requested searchId does not exist
   */
  int getResultsetSize(String searchId, Credential credential) throws NoSuchElementException;
}
