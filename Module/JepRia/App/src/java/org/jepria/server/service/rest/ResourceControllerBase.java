package org.jepria.server.service.rest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jepria.compat.CoreCompat;
import org.jepria.server.dao.JepDataStandard;
import org.jepria.server.security.Credential;

import com.technology.jep.jepria.shared.field.JepTypeEnum;

public class ResourceControllerBase implements ResourceController {

  // нет необходимости параметризовать, так как механизм CRUD не специфицируется на прикладном уровне 
  protected final ResourceDescription<?> resourceDescription;

  public ResourceControllerBase(ResourceDescription<?> resourceDescription) {
    this.resourceDescription = resourceDescription;
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
  public Object getResourceById(String resourceId, Credential credential) {

    Map<String, Object> primaryKeyMap = getResourceIdParser().parse(resourceId);

    final List<?> daoResultList;
    try {
      // TODO remove backward compatibility: resourceDescription.getDao() must return org.jepria.server.dao.JepDataStandard
      JepDataStandard dao = CoreCompat.convertDao(resourceDescription.getDao());
      daoResultList = dao.find(primaryKeyMap, 1, credential.getOperatorId());
    } catch (Throwable e) {
      // TODO or log?
      throw new RuntimeException(e);
    }

    // check find result is of size 1
    if (daoResultList == null || daoResultList.size() == 0) {
      // return 404 (empty result)
      return null;
    } else if (daoResultList.size() != 1) {
      // TODO 
      throw new IllegalStateException("Expected find result of size 1 (actual size: " + daoResultList.size() + ")");
    }


    // daoResult is a single record
    final Object daoResult = daoResultList.get(0);


    // TODO convert the DAO result into a target response object here (using DTO or RecordDefinition, whatever)
    final Object result = daoResult;


    return result;
  }

  private interface ResourceIdParser {
    /**
     * Parse resourceId (simple or composite) into a primary key map with typed values, based on RecordDefinition
     * @param resourceId
     * @return
     */
    Map<String, Object> parse(String resourceId);
  }

  protected ResourceIdParser getResourceIdParser() {
    return new ResourceIdParserImpl();
  }

  private class ResourceIdParserImpl implements ResourceIdParser {
    @Override
    public Map<String, Object> parse(String resourceId) {
      Map<String, Object> ret = new HashMap<>();

      if (resourceId != null) {
        List<String> primaryKey = resourceDescription.getRecordDefinition().getPrimaryKey();

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

          // check that the parsed resourceId key set matches the primary key set from RecordDefinition
          Set<String> primaryKeySet = new HashSet<>(resourceDescription.getRecordDefinition().getPrimaryKey());
          if (!primaryKeySet.equals(resourceIdFieldMap.keySet())) {
            throw new IllegalArgumentException("The parsed resourceId key set " + resourceIdFieldMap.keySet() + " does not match the primary key set " + primaryKeySet);
          }

          for (final String fieldName: resourceIdFieldMap.keySet()) {
            final String fieldvalueStr = resourceIdFieldMap.get(fieldName);
            final Object fieldValue = getTypedValue(fieldName, fieldvalueStr);

            ret.put(fieldName, fieldValue);
          }

        } else {
          throw new IllegalArgumentException("primary key size is not >= 1: " + primaryKey.size());
        }
      }

      return ret;
    }

    private Object getTypedValue(String fieldName, String strValue) {
      JepTypeEnum type = resourceDescription.getRecordDefinition().getFieldType(fieldName);
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
  public Object create(Map<String, Object> record, Credential credential) {
    final Object daoResult;
    try {
      // TODO remove backward compatibility: resourceDescription.getDao() must return org.jepria.server.dao.JepDataStandard
      JepDataStandard dao = CoreCompat.convertDao(resourceDescription.getDao());
      daoResult = dao.create(record, credential.getOperatorId());
    } catch (Throwable e) {
      // TODO or log?
      throw new RuntimeException(e);
    }
    return daoResult;
  }

  @Override
  public void deleteResourceById(String resourceId, Credential credential) {

    Map<String, Object> primaryKeyMap = getResourceIdParser().parse(resourceId);

    try {
      // TODO remove backward compatibility: resourceDescription.getDao() must return org.jepria.server.dao.JepDataStandard
      JepDataStandard dao = CoreCompat.convertDao(resourceDescription.getDao());
      dao.delete(primaryKeyMap, credential.getOperatorId());
    } catch (Throwable e) {
      // TODO or log?
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(String resourceId, Map<String, Object> fields, Credential credential) {
    
    Map<String, Object> primaryKeyMap = getResourceIdParser().parse(resourceId);

    // overwrite primary key fields with values from resourceId, if any
    Map<String, Object> updateModel = new HashMap<>(fields);
    updateModel.putAll(primaryKeyMap);
    
    try {
      // TODO remove backward compatibility: resourceDescription.getDao() must return org.jepria.server.dao.JepDataStandard
      JepDataStandard dao = CoreCompat.convertDao(resourceDescription.getDao());
      dao.update(updateModel, credential.getOperatorId());
    } catch (Throwable e) {
      // TODO or log?
      throw new RuntimeException(e);
    }
    
  }

  /////////////////////////// OPTIONS RESOURCE //////////////////////////

  @Override
  public List<?> listOptions(String optionEntityName, Credential credential) {

    try {
      String optionEntityNameNormalized = normalizeResourceName(optionEntityName);

      final Method getOptionsMethod;

      try {

        String methodName = "get" + optionEntityNameNormalized;
        Class<?> daoClass = resourceDescription.getDao().getClass();
        getOptionsMethod = daoClass.getMethod(methodName);

      } catch (NoSuchMethodException e) {
        // Only NoSuchMethodException equals 'not found' status
        e.printStackTrace();
        return null;
      }


      final Object daoResult = getOptionsMethod.invoke(resourceDescription.getDao());


      // TODO convert the DAO result into a target response object here (using DTO or RecordDefinition, whatever)
      final List<?> result = (List<?>)daoResult;


      return result;

    } catch (Throwable e) {
      // TODO
      throw new RuntimeException(e);
    }
  }
}
