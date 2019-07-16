package org.jepria.server.service.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.jepria.CastMap;

/**
 * A container for {@link ExceptionMapper} classes
 */
public class ExceptionMappers {
  
  private ExceptionMappers() {}
  
  public static class Validation implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException e) {
      return invalidParams(e.getInvalidParams());
    }
  }
 
  /**
   * @param invalidParams null-safe
   * @return
   */
  private static Response invalidParams(Collection<InvalidParameter> invalidParams) {
    
    final Map<String, Object> responseMap = new HashMap<>();
    
    responseMap.put("code", "INVALID_PARAMS");
    
    {
      final Map<String, Object> invalidParamsMap = new HashMap<>();
      
      if (invalidParams != null) {
        for (InvalidParameter invalidParam: invalidParams) {
          {
            final Map<String, Object> invalidParamMap = new HashMap<>();
            invalidParamMap.put("message", invalidParam.message);
            
            invalidParamsMap.put((invalidParam.type == null ? "" : (invalidParam.type + "/")) + invalidParam.name, invalidParamMap);
          }
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
      
      String name = String.valueOf(e.getKey());
      String message = "Expected value type: " + e.getCastTo().getCanonicalName();
      InvalidParameter invalidParam = new InvalidParameter(
          null, name, e.getValue(), message);
      
      return invalidParams(Arrays.asList(invalidParam));
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

}
