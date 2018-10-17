package org.jepria.server.service.rest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jepria.server.load.rest.Compat;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.Mutable;

public class StandardResourceControllerImpl implements StandardResourceController {

  protected final JepRecordDefinition recordDefinition;
  
  protected final JepDataStandard dao;
  
  public StandardResourceControllerImpl(JepRecordDefinition recordDefinition, 
      JepDataStandard dao) {
    this.recordDefinition = recordDefinition;
    this.dao = dao;
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
  public Object getResourceById(String recordId, Credential credential) {

    String[] primaryKey = recordDefinition.getPrimaryKey();
    
    // check primary key is of length 1
    if (primaryKey == null) {
      throw new NullPointerException("Primary key must not be null");
    } else if (primaryKey.length != 1) {
      // TODO add support
      throw new UnsupportedOperationException("Only primary key of length 1 is supported (actual length: " + primaryKey.length + ")");
    }
    

    String primaryKey0 = primaryKey[0];

    JepTypeEnum primaryKeyType = recordDefinition.getTypeMap().get(primaryKey0);
    final Object primaryKeyValueTyped;

    switch (primaryKeyType) {
    case INTEGER: {
      primaryKeyValueTyped = Integer.parseInt(recordId); 
      break;
    }
    case STRING: {
      primaryKeyValueTyped = recordId;
      break;
    }
    default: {
      // TODO add support
      throw new UnsupportedOperationException("Only primary key of type INTEGER or STRING is supported (actual type: " + primaryKeyType + ")");
    }}

    Map<String, Object> primaryKeyMap = new HashMap<>();
    primaryKeyMap.put(primaryKey0, primaryKeyValueTyped);



    try {
      List<JepRecord> result = dao.find(
          Compat.mapToRecord(primaryKeyMap), 
          new Mutable<Boolean>(false), 
          1, 
          credential.getOperatorId());

      
      // check find result is of size 1
      if (result == null || result.size() == 0) {
        // return 404 (empty result)
        return null;
      } else if (result.size() != 1) {
        // TODO 
        throw new IllegalStateException("Expected find result of size 1 (actual size: " + result.size() + ")");
      }
      
      
      // return a single-record result
      return result.get(0);

    } catch (ApplicationException e) {

      // TODO Auto-generated catch block
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public Object create(Map<String, Object> instance, Credential credential) {
    // TODO
    throw new NotImplementedYetException();
  }
  
  @Override
  public void deleteResourceById(String recordId, Credential credential) {
    // TODO
    throw new NotImplementedYetException();
  }
  
  @Override
  public void update(String recordId, Map<String, Object> fields, Credential credential) {
    // TODO
    throw new NotImplementedYetException();
  }
  
  /////////////////////////// OPTIONS RESOURCE //////////////////////////
  
  @Override
  public List<?> listOptions(String optionEntityName, Credential credential) {

    try {
      String optionEntityNameNormalized = normalizeResourceName(optionEntityName);
      
      final Method getOptionsMethod;
  
      try {
  
        String methodName = "get" + optionEntityNameNormalized;
        Class<?> daoClass = dao.getClass();
        getOptionsMethod = daoClass.getMethod(methodName);
  
      } catch (NoSuchMethodException e) {
        // Only NoSuchMethodException equals 'not found' status
        e.printStackTrace();
        return null;
      }
  
  
      final Object result = getOptionsMethod.invoke(dao);
  
      return (List<?>)result;
      
    } catch (Throwable e) {
      // TODO
      throw new RuntimeException(e);
    }
  }
}
