package org.jepria.server.service.rest;

import org.jepria.server.dao.Dao;
import org.jepria.shared.RecordDefinition;


/**
 * Implementors provide standard applicational description for the REST resources  
 */
public interface ResourceDescription<D extends Dao> {

  D getDao();

  String getResourceName();

  RecordDefinition getRecordDefinition();

}
