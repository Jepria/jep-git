package org.jepria.server.service.rest;

import java.util.List;
import java.util.Map;

import org.jepria.server.service.security.Credential;

public interface ResourceController {

  //////////// CRUD ///////////////////

  /**
   * @param recordId
   * @param credential
   * @return instance normally; {@code null} to indicate 'not found' status
   */
  Object getResourceById(String recordId, Credential credential);
  
  /**
   * @param instance
   * @param credential
   * @return created instance ID normally; never {@code null}
   */
  Object create(Map<String, Object> instance, Credential credential);
  
  void deleteResourceById(String recordId, Credential credential);
  
  void update(String recordId, Map<String, Object> fields, Credential credential);
  
  /////////////////////////// OPTIONS RESOURCE //////////////////////////
  
  /**
   * 
   * @param optionEntityName
   * @param credential
   * @return non-empty list normally; {@code null} or empty list to indicate 'not found' status
   */
  List<?> listOptions(String optionEntityName, Credential credential);
  
}
