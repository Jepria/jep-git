package com.technology.jep.jepria.server.service.rest;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;

public class StandardRestOptionResource<S extends JepDataStandard> {
  
  protected final JepRecordDefinition recordDefinition;
  protected final S dao;// TODO or DaoProvider<S>?
  protected final String entityName;
  
  protected StandardRestOptionResource(JepRecordDefinition recordDefinition, S dao, String entityName) {
    this.recordDefinition = recordDefinition;
    this.dao = dao;
    this.entityName = entityName;
  }

  public List<Map<String, Object>> getOptions() {
    try {
      final Method getMethod;
      try {
        getMethod = dao.getClass().getMethod("get" + entityName);
      } catch (NoSuchMethodException e) {
        // TODO
        throw new NotFoundException();
      }
      
      Object result = getMethod.invoke(dao);
      
      return (List<Map<String, Object>>)result;
      
    } catch (Throwable e) {
      // TODO
      throw new RuntimeException(e);
    }
  }
}