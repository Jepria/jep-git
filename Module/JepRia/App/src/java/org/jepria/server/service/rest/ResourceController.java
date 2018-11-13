package org.jepria.server.service.rest;

import java.util.List;
import java.util.NoSuchElementException;

import org.jepria.server.security.Credential;

public interface ResourceController<T> {

  //////////// CRUD ///////////////////

  /**
   * @param resourceId
   * @param credential
   * @return instance, non-null
   * @throws NoSuchElementException if the requested resourceId does not exist
   */
  T getResourceById(String resourceId, Credential credential) throws NoSuchElementException;
  
  /**
   * @param record
   * @param credential
   * @return created resourceId, non-null
   */
  String create(T record, Credential credential);
  
  /**
   * 
   * @param resourceId
   * @param credential
   * @throws NoSuchElementException if the requested resourceId does not exist
   */
  void deleteResource(String resourceId, Credential credential) throws NoSuchElementException;
  
  /**
   * @param resourceId
   * @param fields
   * @param credential
   * @throws NoSuchElementException if the requested resourceId does not exist
   */
  void update(String resourceId, T newRecord, Credential credential) throws NoSuchElementException;
  
  /////////////////////////// OPTIONS RESOURCE //////////////////////////
  
  /**
   * 
   * @param optionEntityName
   * @param credential
   * @return non-empty, non-null list
   * @throws NoSuchElementException if the requested optionEntityName does not exist
   */
  List<?> listOptions(String optionEntityName, Credential credential) throws NoSuchElementException;
  
}
