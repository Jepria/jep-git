package org.jepria.server.service.rest;

import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfigBase extends ResourceConfig {
  
  public ApplicationConfigBase() {
    register(new QueryParamsParamValueFactoryProvider.Binder());
    register(new BodyParamsParamValueFactoryProvider.Binder());

    // register exception mappers
    ExceptionMapper<? extends Throwable> em;
    if ((em = createExceptionMapper(ValidationException.class)) != null) {
      register(em);
    }
    if ((em = createExceptionMapper(JsonParseException.class)) != null) {
      register(em);
    }
  }
  
  /**
   * @param exceptionClass
   * @return {@code null} if no need to register such exception class
   */
  @SuppressWarnings("unchecked")
  protected <T extends Throwable> ExceptionMapper<T> createExceptionMapper(Class<T> exceptionClass) {
    if (exceptionClass == ValidationException.class) {
      return (ExceptionMapper<T>) new ExceptionMappers.Validation();
    }
    if (exceptionClass == JsonParseException.class) {
      return (ExceptionMapper<T>) new ExceptionMappers.JsonParse();
    }
    return null;
  }
}
