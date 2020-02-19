package org.jepria.server.service.rest;

import org.jepria.server.data.Dao;
import org.jepria.server.data.RecordDefinition;
import org.jepria.server.data.RecordDefinition.IncompletePrimaryKeyException;
import org.jepria.server.service.security.Credential;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


public class EntityServiceImpl implements EntityService {

  protected final Dao dao;

  protected final RecordDefinition recordDefinition;

  public EntityServiceImpl(Dao dao, RecordDefinition recordDefinition) {
    this.dao = dao;
    this.recordDefinition = recordDefinition;
  }

  @Override
  public Object getRecordById(String recordId, Credential credential) throws NoSuchElementException {

    final Map<String, ?> primaryKeyMap;
    try {
      primaryKeyMap = recordDefinition.parseRecordId(recordId);
    } catch (IncompletePrimaryKeyException e) {
      throw new IllegalArgumentException("The recordId [" + recordId + "] cannot be parsed against the primary key: incomplete");
    }

    final Object record;
    final List<?> records = dao.findByPrimaryKey(primaryKeyMap, credential == null ? null : credential.getOperatorId());

    // validate records
    if (records == null || records.size() == 0) {
      record = null;
    } else {
      if (records.size() > 1) {
        throw new IllegalStateException("Expected a list of size at most 1, actual size: " + records.size());
      } else {
        record = records.iterator().next();
      }
    }

    // check find result is of size 1
    if (record == null) {
      throw new NoSuchElementException();
    }

    return record;
  }

  @Override
  public String create(Object record, Credential credential) {
    final Object daoResult;

    daoResult = dao.create(record, credential == null ? null : credential.getOperatorId());

    return daoResult.toString();// TODO convert like Parser
  }

  @Override
  public void deleteRecord(String recordId, Credential credential) throws NoSuchElementException {

    final Map<String, ?> primaryKeyMap;
    try {
      primaryKeyMap = recordDefinition.parseRecordId(recordId);
    } catch (IncompletePrimaryKeyException e) {
      throw new NoSuchElementException("The recordId [" + recordId + "] cannot be parsed against the primary key: incomplete");
    }

    dao.delete(primaryKeyMap, credential == null ? null : credential.getOperatorId());
  }

  @Override
  public void update(String recordId, Object newRecord, Credential credential) throws NoSuchElementException {
    
    final Map<String, ?> primaryKeyMap;
    try {
      primaryKeyMap = recordDefinition.parseRecordId(recordId);
    } catch (IncompletePrimaryKeyException e) {
      throw new NoSuchElementException("The recordId [" + recordId + "] cannot be parsed against the primary key: incomplete");
    }
    
    dao.update(primaryKeyMap, newRecord, credential == null ? null : credential.getOperatorId());
  }

}
