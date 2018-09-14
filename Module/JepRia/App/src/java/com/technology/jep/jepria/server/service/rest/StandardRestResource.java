package com.technology.jep.jepria.server.service.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.Mutable;

public class StandardRestResource<D extends JepDataStandard> extends BaseRestResource<D> {
  
  protected StandardRestResource(JepRecordDefinition recordDefinition, Supplier<D> daoSupplier, String entityName) {
    super(recordDefinition, daoSupplier, entityName);
  }

  // returns Object to support Map, Dto or Response
  public Object getResourceById(String recordId) {
    
    String[] primaryKey = recordDefinition.getPrimaryKey();
    if (primaryKey != null && primaryKey.length == 1) {
      
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
        // TODO
        throw new UnsupportedOperationException();
      }}
      
      Map<String, Object> primaryKeyMap = new HashMap<>();
      primaryKeyMap.put(primaryKey0, primaryKeyValueTyped);
      
      
      
      final D dao = daoSupplier.get();
      
      try {
        List<JepRecord> result = dao.find(
            Compat.mapToRecord(primaryKeyMap), 
            new Mutable<Boolean>(false), 
            1, 
            1/*TODO operatorId*/);
        
        if (result == null || result.size() == 0) {
          
          // return 404 (empty result)
          return Response.status(Status.NOT_FOUND).build();
          
        } else if (result.size() == 1) {
          
          // return normal result (single-record)
          return result.get(0);
          
        } else {
          
          // TODO 
          throw new IllegalStateException("result size > 1");
          
        }
        
      } catch (ApplicationException e) {
        
        // TODO Auto-generated catch block
        throw new RuntimeException(e);
      }
      
    } else {
      // TODO
      throw new IllegalStateException();
    }
  }
  
  
  // return Object to support Map, Dto or Response
  public Object getAllResources() {
    return Response.status(Status.BAD_REQUEST).entity("Resource ID is mandatory, returning full list of resources unsupported").build();
  }
}