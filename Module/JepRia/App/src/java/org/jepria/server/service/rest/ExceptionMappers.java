package org.jepria.server.service.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * A container for {@link ExceptionMapper} classes
 */
public class ExceptionMappers {
  
  private ExceptionMappers() {}
  
  public static class Validation implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException e) {
      
      final Map<String, Object> invalidParamMap = new HashMap<>();
      Collection<InvalidParameter> invalidParams = e.getInvalidParams();
      if (invalidParams != null) {
        for (InvalidParameter invalidParam: invalidParams) {
          invalidParamMap.put(invalidParam.name, invalidParam);
        }
      }
      
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(invalidParamMap)
          .type("application/json;charset=UTF-8").build();
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
