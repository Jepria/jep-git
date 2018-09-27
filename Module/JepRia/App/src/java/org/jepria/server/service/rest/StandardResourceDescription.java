package org.jepria.server.service.rest;

import org.jepria.server.service.rest.jaxrs.JaxrsStandardResourceEndpoint;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;

/**
 * Implementors provide application description for the REST resources backed by {@link JaxrsStandardResourceEndpoint}  
 */
public interface StandardResourceDescription {

  JepDataStandard getDao();

  String getResourceName();

  JepRecordDefinition getRecordDefinition();

}
