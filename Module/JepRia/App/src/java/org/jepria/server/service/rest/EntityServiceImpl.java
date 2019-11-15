package org.jepria.server.service.rest;

import org.jepria.server.data.Dao;
import org.jepria.server.data.RecordDefinition;
import org.jepria.server.data.RecordDefinition.IncompletePrimaryKeyException;
import org.jepria.server.service.security.Credential;

import java.util.HashMap;
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
      primaryKeyMap = getRecordIdParser().parse(recordId);
    } catch (IncompletePrimaryKeyException e) {
      throw new IllegalArgumentException("The recordId '" + recordId + "' cannot be parsed against the primary key");
    }

    final Object record;
    final List<?> records = dao.findByPrimaryKey(primaryKeyMap, credential.getOperatorId());

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

  private interface RecordIdParser {
    /**
     * Parse recordId (simple or composite) into a primary key map with typed values, based on RecordDefinition
     * @param recordId
     * @return
     * @throws IncompletePrimaryKeyException 
     */
    Map<String, ?> parse(String recordId) throws IncompletePrimaryKeyException;
  }

  protected RecordIdParser getRecordIdParser() {
    return new RecordIdParserImpl();
  }

  private class RecordIdParserImpl implements RecordIdParser {
    @Override
    public Map<String, Object> parse(String recordId) throws IncompletePrimaryKeyException {
      Map<String, Object> ret = new HashMap<>();

      if (recordId != null) {
        final List<String> primaryKey = recordDefinition.getPrimaryKey();

        if (primaryKey.size() == 1) {
          // simple primary key: "value"

          final String fieldName = primaryKey.get(0);
          final Object fieldValue = getTypedValue(fieldName, recordId);

          ret.put(fieldName, fieldValue);

        } else if (primaryKey.size() > 1) {
          // composite primary key: "key1=value1,key2=value2"

          Map<String, String> recordIdFieldMap = new HashMap<>();

          String[] recordIdParts = recordId.split("\\s*[,;]\\s*");// TODO split both by , and ; or by one of them only?
          for (String recordIdPart: recordIdParts) {
            if (recordIdPart != null) {
              String[] recordIdPartKv = recordIdPart.split("\\s*=\\s*");
              if (recordIdPartKv.length != 2) {
                throw new IllegalArgumentException("Could not split '" + recordIdPart + "' as 'key=value'");
              }
              recordIdFieldMap.put(recordIdPartKv[0], recordIdPartKv[1]);
            }
          }

          // check or throw
          recordIdFieldMap = recordDefinition.buildPrimaryKey(recordIdFieldMap);


          // create typed values
          for (final String fieldName: recordIdFieldMap.keySet()) {
            final String fieldValueStr = recordIdFieldMap.get(fieldName);
            final Object fieldValue = getTypedValue(fieldName, fieldValueStr);

            ret.put(fieldName, fieldValue);
          }
        }
      }

      return ret;
    }

    private Object getTypedValue(String fieldName, String strValue) {
      Class<?> type = recordDefinition.getFieldType(fieldName);
      if (type == null) {
        throw new IllegalArgumentException("Could not determine type for the field '" + fieldName + "'");
      } else if (type == Integer.class) {
        return Integer.parseInt(strValue);
      } else if (type == String.class) {
        return strValue;
      } else {
        // TODO add support?
        throw new UnsupportedOperationException("The type '" + type + "' is unsupported for getting typed values");
      }
    }
  }

  @Override
  public String create(Object record, Credential credential) {
    final Object daoResult;

    daoResult = dao.create(record, credential.getOperatorId());

    return daoResult.toString();// TODO convert like Parser
  }

  @Override
  public void deleteRecord(String recordId, Credential credential) throws NoSuchElementException {

    final Map<String, ?> primaryKeyMap;
    try {
      primaryKeyMap = getRecordIdParser().parse(recordId);
    } catch (IncompletePrimaryKeyException e) {
      throw new NoSuchElementException("The recordId '" + recordId + "' cannot be parsed against the primary key");
    }

    dao.delete(primaryKeyMap, credential.getOperatorId());
  }

  @Override
  public void update(String recordId, Object newRecord, Credential credential) throws NoSuchElementException {
    
    final Map<String, ?> primaryKeyMap;
    try {
      primaryKeyMap = getRecordIdParser().parse(recordId);
    } catch (IncompletePrimaryKeyException e) {
      throw new NoSuchElementException("The recordId '" + recordId + "' cannot be parsed against the primary key");
    }
    
    dao.update(primaryKeyMap, newRecord, credential.getOperatorId());
  }

}
