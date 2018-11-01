package org.jepria.server.service.rest;

import java.util.List;
import java.util.Map;

import org.jepria.server.security.Credential;

public interface ResourceController {

  //////////// CRUD ///////////////////

  /**
   * @param resourceId
   * @param credential
   * @return instance normally; {@code null} to indicate 'not found' status
   */
  Object getResourceById(String resourceId, Credential credential);
  
  /**
   * @param record
   * @param credential
   * @return created instance ID normally; never {@code null}
   */
  Object create(Map<String, Object> record, Credential credential);
  
  void deleteResourceById(String resourceId, Credential credential);
  
  void update(String resourceId, Map<String, Object> fields, Credential credential);
  
  /////////////////////////// OPTIONS RESOURCE //////////////////////////
  
  /**
   * 
   * @param optionEntityName
   * @param credential
   * @return non-empty list normally; {@code null} or empty list to indicate 'not found' status
   */
  List<?> listOptions(String optionEntityName, Credential credential);
  
}
