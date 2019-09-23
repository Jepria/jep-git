package org.jepria.server.service.rest;

import org.jepria.server.data.Dao;
import org.jepria.server.data.RecordDefinition;
import org.jepria.server.data.RecordDefinition.IncompletePrimaryKeyException;
import org.jepria.server.service.security.Credential;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ResourceBasicControllerImpl implements ResourceBasicController {

  protected final ResourceDescription description;

  public ResourceBasicControllerImpl(ResourceDescription description) {
    this.description = description;
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

    final Object resource;
    try {
      resource = description.getDao().findByPrimaryKey(primaryKeyMap, credential.getOperatorId());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }

    // check find result is of size 1
    if (resource == null) {
      throw new NoSuchElementException();
    }

    return resource;
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
        final List<String> primaryKey = description.getRecordDefinition().getPrimaryKey();

        if (primaryKey.size() == 1) {
          // simple primary key: "value"

          final String fieldName = primaryKey.get(0);
          final Object fieldValue = getTypedValue(fieldName, resourceId);

          ret.put(fieldName, fieldValue);

        } else if (primaryKey.size() > 1) {
          // composite primary key: "key1=value1,key2=value2"

          Map<String, String> resourceIdFieldMap = new HashMap<>();

          String[] resourceIdParts = resourceId.split("\\s*,\\s*");
          for (String resourceIdPart: resourceIdParts) {
            if (resourceIdPart != null) {
              String[] resourceIdPartKv = resourceIdPart.split("\\s*\\=\\s*");
              if (resourceIdPartKv.length != 2) {
                throw new IllegalArgumentException("Could not split '" + resourceIdPart + "' as 'key=value'");
              }
              resourceIdFieldMap.put(resourceIdPartKv[0], resourceIdPartKv[1]);
            }
          }

          // check or throw
          resourceIdFieldMap = description.getRecordDefinition().buildPrimaryKey(resourceIdFieldMap);

          
          // create typed values
          for (final String fieldName: resourceIdFieldMap.keySet()) {
            final String fieldValueStr = resourceIdFieldMap.get(fieldName);
            final Object fieldValue = getTypedValue(fieldName, fieldValueStr);

            ret.put(fieldName, fieldValue);
          }
        }
      }

      return ret;
    }

    private Object getTypedValue(String fieldName, String strValue) {
      Class<?> type = description.getRecordDefinition().getFieldType(fieldName);
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
    try {
      daoResult = description.getDao().create(record, credential.getOperatorId());
    } catch (Throwable e) {
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
      description.getDao().delete(primaryKeyMap, credential.getOperatorId());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(String resourceId, Object newRecord, Credential credential) throws NoSuchElementException {
    
    final Map<String, ?> primaryKeyMap;
    try {
      primaryKeyMap = getResourceIdParser().parse(resourceId);
    } catch (IncompletePrimaryKeyException e) {
      throw new NoSuchElementException("The resourceId '" + resourceId + "' cannot be parsed against the primary key");
    }
    
    try {
      description.getDao().update(primaryKeyMap, newRecord, credential.getOperatorId());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
    
  }

  /////////////////////////// OPTIONS RESOURCE //////////////////////////

  @Override
  public List<?> listOptions(String optionEntityName, Credential credential) throws NoSuchElementException {

    try {

      final Object dao = description.getDao();

      String optionEntityNameNormalized = normalizeResourceName(optionEntityName);

      final Method getOptionsMethod;

      final String methodName = "get" + optionEntityNameNormalized;
      try {
        Class<?> daoClass = dao.getClass();
        getOptionsMethod = daoClass.getMethod(methodName);
        
      } catch (NoSuchMethodException e) {
        NoSuchElementException rethrown = new NoSuchElementException("The dao class contains no '" + methodName + "' method");
        rethrown.addSuppressed(e);
        throw rethrown;
      }


      final Object daoResult = getOptionsMethod.invoke(dao);


      final List<?> result = (List<?>)daoResult;


      return result;

    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
