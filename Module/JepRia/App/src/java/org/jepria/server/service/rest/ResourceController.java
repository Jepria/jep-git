package org.jepria.server.service.rest;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.jepria.server.security.Credential;

/**
 * Контроллер базовых операций над объектами сущности (CRUD, options)  
 */
public interface ResourceController {

  /**
   * @param resourceId
   * @param credential
   * @return instance, non-null
   * @throws NoSuchElementException if the requested resourceId does not exist
   */
  Map<String, ?> getResourceById(String resourceId, Credential credential) throws NoSuchElementException;
  
  /**
   * @param record
   * @param credential
   * @return created resourceId, non-null
   */
  String create(Map<String, ?> record, Credential credential);
  
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
  void update(String resourceId, Map<String, ?> newRecord, Credential credential) throws NoSuchElementException;
  
  /**
   * 
   * @param optionEntityName
   * @param credential
   * @return non-empty, non-null list
   * @throws NoSuchElementException if the requested optionEntityName does not exist
   */
  List<?> listOptions(String optionEntityName, Credential credential) throws NoSuchElementException;
  
}
