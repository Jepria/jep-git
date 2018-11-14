package org.jepria.server.service.rest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jepria.server.dao.Dao;
import org.jepria.server.security.Credential;
import org.jepria.shared.RecordDefinition;
import org.jepria.shared.RecordDefinition.IncompletePrimaryKeyException;

import com.technology.jep.jepria.shared.field.JepTypeEnum;

public class ResourceControllerBase implements ResourceController {

  // нет необходимости параметризовать, так как механизм CRUD не специфицируется на прикладном уровне 
  protected final Supplier<Dao> daoSupplier;
  protected final RecordDefinition recordDefinition;

  public ResourceControllerBase(Supplier<Dao> daoSupplier, RecordDefinition recordDefinition) {
    this.daoSupplier = daoSupplier;
    this.recordDefinition = recordDefinition;
  }

  /**
   * Converts url-styled resource name to dao-styled resource name:
   * <br/>
   * {@code feature-status} -> {@code FeatureStatus}
   * @param resourceName url-styled resource name
   * @return dao-styled resource name; {@code null} if the input is {@code null} 
   */
  protected String normalizeResourceName(String resourceName) {
    if (resourceName == null) {
      return null;
    }

    String ret = resourceName;

    while (true) {
      Matcher m = Pattern.compile("(^|[-_]+)([a-z])").matcher(ret);
      if (!m.find()) {
        break;
      } else {
        ret = ret.substring(0, m.start()) + Character.toUpperCase(m.group(2).charAt(0)) + ret.substring(m.end(), ret.length());
      }
    }

    return ret;
  }

  //////////// CRUD ///////////////////

  @Override
  public Object getResourceById(String resourceId, Credential credential) throws NoSuchElementException {

    final Map<String, ?> primaryKeyMap;
    try {
      primaryKeyMap = getResourceIdParser().parse(resourceId);
    } catch (IncompletePrimaryKeyException e) {
      throw new NoSuchElementException("The resourceId '" + resourceId + "' cannot be parsed against the primary key");
    }

    final List<?> daoResultList;
    try {
      Dao dao = daoSupplier.get();
      daoResultList = dao.find(primaryKeyMap, 1, credential.getOperatorId());
    } catch (Throwable e) {
      // TODO or log?
      throw new RuntimeException(e);
    }

    // check find result is of size 1
    if (daoResultList == null || daoResultList.size() == 0) {
      throw new NoSuchElementException();
      
    } else if (daoResultList.size() != 1) {
      // TODO 
      throw new IllegalStateException("Expected find result of size 1 (actual size: " + daoResultList.size() + ")");
    }


    // daoResult is a single record
    final Object result = daoResultList.get(0);

    return result;
  }

  private interface ResourceIdParser {
    /**
     * Parse resourceId (simple or composite) into a primary key map with typed values, based on RecordDefinition
     * @param resourceId
     * @return
     * @throws IncompletePrimaryKeyException 
     */
    Map<String, ?> parse(String resourceId) throws IncompletePrimaryKeyException;
  }

  protected ResourceIdParser getResourceIdParser() {
    return new ResourceIdParserImpl();
  }

  private class ResourceIdParserImpl implements ResourceIdParser {
    @Override
    public Map<String, Object> parse(String resourceId) throws IncompletePrimaryKeyException {
      Map<String, Object> ret = new HashMap<>();

      if (resourceId != null) {
        final List<String> primaryKey = recordDefinition.getPrimaryKey();

        if (primaryKey.size() == 1) {
          // simple primary key

          final String fieldName = primaryKey.get(0);
          final String fieldvalueStr = resourceId;
          final Object fieldValue = getTypedValue(fieldName, fieldvalueStr);

          ret.put(fieldName, fieldValue);

        } else if (primaryKey.size() > 1) {
          // composite primary key, parse resourceId as "key1=value1,key2=value2"

          Map<String, String> resourceIdFieldMap = new HashMap<>();

          String[] resourceIdParts = resourceId.split("\\s*,\\s*");
          if (resourceIdParts != null) {
            for (String resourceIdPart: resourceIdParts) {
              if (resourceIdPart != null) {
                String[] resourceIdPartKv = resourceIdPart.split("\\s*\\=\\s*");
                if (resourceIdPartKv == null || resourceIdPartKv.length != 2) {
                  throw new IllegalArgumentException("Could not split '" + resourceIdPart + "' as 'key=value'");
                }
                resourceIdFieldMap.put(resourceIdPartKv[0], resourceIdPartKv[1]);
              }
            }
          }

          // check or throw
          resourceIdFieldMap = recordDefinition.buildPrimaryKey(resourceIdFieldMap);

          
          // create typed values
          for (final String fieldName: resourceIdFieldMap.keySet()) {
            final String fieldvalueStr = resourceIdFieldMap.get(fieldName);
            final Object fieldValue = getTypedValue(fieldName, fieldvalueStr);

            ret.put(fieldName, fieldValue);
          }
        }
      }

      return ret;
    }

    private Object getTypedValue(String fieldName, String strValue) {
      JepTypeEnum type = recordDefinition.getFieldType(fieldName);
      if (type == null) {
        throw new IllegalArgumentException("Could not determine type for the field '" + fieldName + "'");
      }
      switch (type) {
      case STRING: {
        return Integer.parseInt(strValue);
      }
      case INTEGER: {
        return strValue;
      }
      default: {
        // TODO add support?
        throw new UnsupportedOperationException("The type '" + type + "' is unsupported for getting typed values");
      }
      }
    }
  }

  @Override
  public String create(Map<String, ?> record, Credential credential) {
    final Object daoResult;
    try {
      Dao dao = daoSupplier.get();
      daoResult = dao.create(record, credential.getOperatorId());
    } catch (Throwable e) {
      // TODO or log?
      throw new RuntimeException(e);
    }
    return daoResult.toString();// TODO convert like Parser
  }

  @Override
  public void deleteResource(String resourceId, Credential credential) throws NoSuchElementException {

    final Map<String, ?> primaryKeyMap;
    try {
      primaryKeyMap = getResourceIdParser().parse(resourceId);
    } catch (IncompletePrimaryKeyException e) {
      throw new NoSuchElementException("The resourceId '" + resourceId + "' cannot be parsed against the primary key");
    }

    try {
      Dao dao = daoSupplier.get();
      dao.delete(primaryKeyMap, credential.getOperatorId());
    } catch (Throwable e) {
      // TODO or log?
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(String resourceId, Map<String, ?> newRecord, Credential credential) throws NoSuchElementException {
    
    final Map<String, ?> primaryKeyMap;
    try {
      primaryKeyMap = getResourceIdParser().parse(resourceId);
    } catch (IncompletePrimaryKeyException e) {
      throw new NoSuchElementException("The resourceId '" + resourceId + "' cannot be parsed against the primary key");
    }
    
    // overwrite primary key fields with values from resourceId, if any
    Map<String, Object> updateModel = new HashMap<>();
    updateModel.putAll(primaryKeyMap);// TODO put primaryKeyMap into newRecord
    
    try {
      Dao dao = daoSupplier.get();
      dao.update(newRecord, credential.getOperatorId());
    } catch (Throwable e) {
      // TODO or log?
      throw new RuntimeException(e);
    }
    
  }

  /////////////////////////// OPTIONS RESOURCE //////////////////////////

  @Override
  public List<?> listOptions(String optionEntityName, Credential credential) throws NoSuchElementException {

    try {
      String optionEntityNameNormalized = normalizeResourceName(optionEntityName);

      final Method getOptionsMethod;

      final String methodName = "get" + optionEntityNameNormalized;
      try {
        Class<?> daoClass = daoSupplier.get().getClass();
        getOptionsMethod = daoClass.getMethod(methodName);
        
      } catch (NoSuchMethodException e) {
        NoSuchElementException rethrown = new NoSuchElementException("The dao class contains no '" + methodName + "' method");
        rethrown.addSuppressed(e);
        throw rethrown;
      }


      final Object daoResult = getOptionsMethod.invoke(daoSupplier.get());


      // TODO convert the DAO result into a target response object here (using DTO or RecordDefinition, whatever)
      final List<?> result = (List<?>)daoResult;


      return result;

    } catch (Throwable e) {
      // TODO
      throw new RuntimeException(e);
    }
  }
}
