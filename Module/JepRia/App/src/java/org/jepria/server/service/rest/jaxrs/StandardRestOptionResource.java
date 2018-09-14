package org.jepria.server.service.rest.jaxrs;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response.Status;

import com.technology.jep.jepria.shared.record.JepRecordDefinition;

/**
 * Реализация стандартного REST-ресурса для получения опций
 *
 * @param <D> интерфейс Dao
 */
public class StandardRestOptionResource<D> extends BaseRestResource<D> {
  
  protected StandardRestOptionResource(JepRecordDefinition recordDefinition, Supplier<D> daoSupplier, String entityName) {
    super(recordDefinition, daoSupplier, entityName);
  }

  public List<Map<String, Object>> getOptions() {
    
    final D dao = daoSupplier.get();
    
    
    final Method getOptionsMethod;
    
    try {
      getOptionsMethod = getMethod(dao.getClass());
      
    } catch (Throwable e) {
      // any error results 404 status
      e.printStackTrace();
      throw new NotFoundException();
    }
    
    
    final Object result;
    
    try {
      
      Object[] invocationParams = getInvocationParams();
      if (invocationParams == null || invocationParams.length == 0) {
        result = getOptionsMethod.invoke(dao);
      } else {
        result = getOptionsMethod.invoke(dao, invocationParams);
      }
      
    } catch (Throwable e) {
      
      e.printStackTrace();
      throw new ServerErrorException(Status.INTERNAL_SERVER_ERROR);
    }
    
    return (List<Map<String, Object>>)result;
  }
  
  /**
   * Поиск вызываемого метода в Dao-классе
   * @param daoClass
   * @return метод получения опций, либо null если подходящего метода не найдено (в этом случае будет 404 статус) 
   */
  protected Method getMethod(Class<?> daoClass) {
    try {
      
      String methodName = "get" + entityName;
      return daoClass.getMethod(methodName);
      
    } catch (NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  /**
   * Определение параметров для вызова метода получения опций 
   * @return объекты-параметры или null 
   */
  protected Object[] getInvocationParams() {
    return null;
  }
}