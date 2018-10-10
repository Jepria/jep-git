package org.jepria.server.service.rest;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;

/**
 * Implementors provide application description for the REST resources backed by {@link org.jepria.server.service.rest.jaxrs.BaseStandardResourceEndpoint}  
 */
public interface ResourceDescription {

  JepDataStandard getDao();

  String getResourceName();

  JepRecordDefinition getRecordDefinition();

}
