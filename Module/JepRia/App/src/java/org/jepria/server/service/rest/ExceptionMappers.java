package org.jepria.server.service.rest;

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
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(e.getClass().getCanonicalName() + ": " + e.getMessage())
          .type("text/plain").build();
    }
  }
  
  public static class JsonParse implements ExceptionMapper<JsonParseException> {
    @Override
    public Response toResponse(JsonParseException e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(e.getClass().getCanonicalName() + ": " + e.getMessage())
          .type("text/plain").build();
    }
  }

}
