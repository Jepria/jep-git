package org.jepria.server.service.rest;

import org.jepria.server.dao.JepDataStandard;
import org.jepria.shared.RecordDefinition;


/**
 * Implementors provide standard applicational description for the REST resources  
 */
public interface ResourceDescription<D extends JepDataStandard> {

  D getDao();

  String getResourceName();

  RecordDefinition getRecordDefinition();

}
