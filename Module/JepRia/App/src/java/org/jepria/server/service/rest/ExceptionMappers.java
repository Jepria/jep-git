package org.jepria.server.service.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.jepria.CastMap;
import org.jepria.server.service.rest.validation.ConstraintViolationBase;
import org.jepria.server.service.rest.validation.PathBase;

/**
 * A container for {@link ExceptionMapper} classes
 */
public class ExceptionMappers {
  
  private ExceptionMappers() {}
  
  public static class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
      return invalidParams(e.getConstraintViolations());
    }
  }
 
  /**
   * @param constraintViolations null-safe
   * @return
   */
  private static Response invalidParams(Collection<ConstraintViolation<?>> constraintViolations) {
    
    final Map<String, Object> responseMap = new HashMap<>();
    
    responseMap.put("code", "INVALID_PARAMS");
    
    {
      final Map<String, Object> invalidParamsMap = new HashMap<>();
      
      if (constraintViolations != null) {
        
        int unnamedViolationCount = 0;
        
        for (ConstraintViolation<?> constraintViolation: constraintViolations) {
          
          final Map<String, Object> invalidParamMap = new HashMap<>();
          invalidParamMap.put("message", constraintViolation.getMessage());
          
          String path = null;
          if (constraintViolation.getPropertyPath() == null 
              || (path = constraintViolation.getPropertyPath().toString()) == null 
              || "".equals(path)) {
            path = "unnamed#" + ++unnamedViolationCount;
          }
          
          invalidParamsMap.put(path, invalidParamMap);
        }
      }
      responseMap.put("invalidParams", invalidParamsMap);
    }
    
    StringBuilder sb = new StringBuilder();
    new JsonSerializer().serialize(responseMap, sb);
    String entity = sb.toString();
    
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(entity)
        .type("application/json;charset=UTF-8").build();
  }
  
  public static class CastOnGet implements ExceptionMapper<CastMap.CastOnGetException> {
    @Override
    public Response toResponse(CastMap.CastOnGetException e) {
      // treat as invalid parameter

      String message = "Expected value type: " + e.getCastTo().getCanonicalName();
      
      ConstraintViolation<?> violation = new ConstraintViolationBase(
          e.getKey() == null ? null : new PathBase(String.valueOf(e.getKey()))
              , message);
      
      return invalidParams(Arrays.asList(violation));
    }      
  }
  
  public static class JsonParse implements ExceptionMapper<JsonParseException> {
    @Override
    public Response toResponse(JsonParseException e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(e.getClass().getCanonicalName() + ": " + e.getMessage())
          .type("text/plain;charset=UTF-8").build();
    }
  }

  /**
   * Mapper that logs Throwables 
   */
  public static class Logging implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable e) {
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }
}
