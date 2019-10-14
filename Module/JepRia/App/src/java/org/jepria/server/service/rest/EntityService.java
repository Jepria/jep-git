package org.jepria.server.service.rest;

import org.jepria.server.service.security.Credential;

import java.util.NoSuchElementException;

/**
 * Сервис сущности, обслуживающий базовые CRUD-операции над объектами сущности (create, get-by-id, update, delete)
 * <br/>
 * <i>В устаревшей терминологии: контроллер сущности, CRUD-контроллер, ResourceBasicController</i>
 * <br/>
 */
public interface EntityService {

  /**
   * @param recordId
   * @param credential
   * @return instance, non-null
   * @throws NoSuchElementException if the requested recordId does not exist
   */
  Object getRecordById(String recordId, Credential credential) throws NoSuchElementException;
  
  /**
   * @param record
   * @param credential
   * @return created recordId, non-null
   */
  String create(Object record, Credential credential);
  
  /**
   * @param recordId
   * @param credential
   * @throws NoSuchElementException if the requested recordId does not exist
   */
  void deleteRecord(String recordId, Credential credential) throws NoSuchElementException;
  
  /**
   * @param recordId
   * @param newRecord
   * @param credential
   * @throws NoSuchElementException if the requested recordId does not exist
   */
  void update(String recordId, Object newRecord, Credential credential) throws NoSuchElementException;
}
