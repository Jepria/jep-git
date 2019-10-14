package org.jepria.server.service.rest;

import org.jepria.server.service.security.Credential;

import java.util.NoSuchElementException;

/**
 * Сервис сущности, обслуживающий базовые CRUD-операции над объектами сущности (create, get-by-id, update, delete)
 * <br/>
 * <i>В устаревшей терминологии: контроллер сущности, CRUD-контроллер, ResourceBasicController</i>
 * <br/>
 */
public interface ResourceBasicController {

  /**
   * @param resourceId
   * @param credential
   * @return instance, non-null
   * @throws NoSuchElementException if the requested resourceId does not exist
   */
  Object getResourceById(String resourceId, Credential credential) throws NoSuchElementException;
  
  /**
   * @param record
   * @param credential
   * @return created resourceId, non-null
   */
  String create(Object record, Credential credential);
  
  /**
   * @param resourceId
   * @param credential
   * @throws NoSuchElementException if the requested resourceId does not exist
   */
  void deleteResource(String resourceId, Credential credential) throws NoSuchElementException;
  
  /**
   * @param resourceId
   * @param newRecord
   * @param credential
   * @throws NoSuchElementException if the requested resourceId does not exist
   */
  void update(String resourceId, Object newRecord, Credential credential) throws NoSuchElementException;
}
