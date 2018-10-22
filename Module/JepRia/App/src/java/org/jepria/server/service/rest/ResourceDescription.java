package org.jepria.server.service.rest;

import org.jepria.shared.RecordDefinition;

import com.technology.jep.jepria.server.dao.JepDataStandard;

/**
 * Implementors provide standard applicational description for the REST resources  
 */
public interface ResourceDescription<D extends JepDataStandard> {

  D getDao();

  String getResourceName();

  RecordDefinition getRecordDefinition();

}
