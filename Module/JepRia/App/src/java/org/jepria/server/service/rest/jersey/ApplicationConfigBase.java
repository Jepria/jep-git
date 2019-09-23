package org.jepria.server.service.rest.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.jepria.server.service.rest.gson.JsonBindingProvider;

import javax.json.bind.JsonbException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ApplicationConfigBase extends ResourceConfig {
  
  public ApplicationConfigBase() {
    packages("io.swagger.jaxrs.listing");

    register(JsonBindingProvider.class);


    // register exception mappers
    ExceptionMapper<? extends Throwable> em;
    if ((em = createExceptionMapper(JsonbException.class)) != null) {
      register(em);
    }

    // Note:
    // Некоторые исключения могут быть спрятаны в cause более общего эксепшена javax.ws.rs.ProcessingException,
    // и таким образом не отлавливаться целевыми обработчиками.


    { // Подключение отладочного обработчика исключений

      // Note:
      // Некоторые исключения могут по каким-то причинам никак не логироваться, а просто выдавать ошибку сервиса 500.
      // Для отладки таких исключений можно временно включить отладочный обработчик.
      // Включать его можно только на период отладки, потому что когда он включен,
      // то все исключения обрабатываются этим обработчиком, а должны штатными (например, javax.ws.rs.NotFoundException).

      // register(new ExceptionMapperDefault());
    }


  }

  /**
   * @param exceptionClass
   * @return {@code null} if no need to register such exception class
   */
  @SuppressWarnings("unchecked")
  protected <T extends Throwable> ExceptionMapper<T> createExceptionMapper(Class<T> exceptionClass) {
    if (exceptionClass == JsonbException.class) {
      return (ExceptionMapper<T>) new ExceptionMapperJsonb();
    }
    return null;
  }

  public static class ExceptionMapperJsonb implements ExceptionMapper<JsonbException> {
    @Override
    public Response toResponse(JsonbException e) {
      e.printStackTrace();
      return Response.status(Response.Status.BAD_REQUEST)
              .entity(e.getClass().getCanonicalName() + ": " + e.getMessage())
              .type("text/plain;charset=UTF-8").build();
    }
  }

  /**
   * Lowest-level ExceptionMapper that simply logs exceptions which can potentially be swallowed. Used for debugging purposes
   */
  public static class ExceptionMapperDefault implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable e) {
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }
}
