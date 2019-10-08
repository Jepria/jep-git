package org.jepria.server.service.rest;

import org.jepria.server.data.Dao;
import org.jepria.server.data.RecordDefinition;

/**
 * Прикладное описание стандартного REST-ресурса
 */
public interface ResourceDescription {
  Dao getDao();
  RecordDefinition getRecordDefinition();
}
