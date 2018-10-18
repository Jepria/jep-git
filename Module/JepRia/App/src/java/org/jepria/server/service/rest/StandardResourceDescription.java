package org.jepria.server.service.rest;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;

/**
 * Implementors provide standard applicational description for the REST resources  
 */
public interface StandardResourceDescription {

  JepDataStandard getDao();

  String getResourceName();

  JepRecordDefinition getRecordDefinition();

}
