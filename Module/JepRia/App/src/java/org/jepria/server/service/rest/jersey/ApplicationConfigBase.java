package org.jepria.server.service.rest.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.spi.ExceptionMappers;
import org.jepria.server.service.rest.ErrorDto;
import org.jepria.server.service.rest.MetaInfoResource;
import org.jepria.server.service.rest.XCacheControlFilter;
import org.jepria.server.service.rest.gson.JsonBindingProvider;
import org.jepria.server.service.rest.jersey.validate.ExceptionMapperValidation;
import org.jepria.server.service.security.CorsFilter;
import org.jepria.server.service.security.HttpBasicDynamicFeature;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.json.bind.JsonbException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.lang.reflect.UndeclaredThrowableException;

public class ApplicationConfigBase extends ResourceConfig {
  
  @Inject
  // for finding proper ExceptionMappers at runtime
  private Provider<ExceptionMappers> mappers;
  
  public ApplicationConfigBase() {
    
    registerJsonBindingProvider();
    registerHttpBasicDynamicFeature();
    registerRolesAllowedDynamicFeature();
    
    
    registerJepSecurityContextBinder();

    /*
    Note: Cache-Control header replacement for React Native/Expo mobile apps
    TODO remove when okHttp Cache-Control header issue will be fixed https://github.com/expo/expo/issues/1639
     */
    registerXCacheControlFilter();
    
    
    // register exception mappers
    
    registerExceptionMapperJsonb();
    
    // Note: unchecked-исключения могут быть обёрнуты в java.lang.reflect.UndeclaredThrowableException, и таким образом не отлавливаться целевыми обработчиками.
    registerExceptionMapperUndeclaredThrowable();
    
    registerExceptionMapperDefault();
    
    registerMetaInfoResource();
    
    registerValidation();
    
    registerCorsHandler();
  }
  
  /**
   * ExceptionMapper for json parse exceptions
   * @see org.jepria.server.service.rest.gson.GsonJsonb
   */
  public static class ExceptionMapperJsonb implements ExceptionMapper<JsonbException> {
    @Override
    public Response toResponse(JsonbException e) {
      e.printStackTrace();
      
      // The exception is triggered by the client data, so its stacktrace contains no private
      
      // Collect messages from every exception in the stack
      final String clientErrorMessage;
      {
        StringBuilder sb = new StringBuilder();
        Throwable th = e;
        while (th != null) {
          if (sb.length() > 0) {
            sb.append("; ");
          }
          sb.append(th.getClass().getSimpleName()).append(": ").append(th.getMessage());
          th = th.getCause();
        }
        if (sb.length() > 0) {
          clientErrorMessage = sb.toString();
        } else {
          clientErrorMessage = "Json parse exception (no details attached)"; // default client message
        }
      }
      
      ErrorDto errorDto = ExceptionManager.newInstance().registerExceptionAndPrepareErrorDto(e);
      errorDto.setErrorMessage(clientErrorMessage);
      return Response.status(Response.Status.BAD_REQUEST).entity(errorDto).build();
    }
  }
  
  /**
   * ExceptionMapper for UndeclaredThrowableException
   */
  public class ExceptionMapperUndeclaredThrowable implements ExceptionMapper<UndeclaredThrowableException> {
    @Override
    public Response toResponse(UndeclaredThrowableException e) {
      Throwable cause = e.getCause();
      // delegate exception handling to the proper ExceptionMapper
      return mappers.get().findMapping(cause).toResponse(cause);
    }
  }
  
  /**
   * Lowest-level ExceptionMapper that handles all other exceptions
   */
  public static class ExceptionMapperDefault implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable e) {
  
      if (e instanceof WebApplicationException) {
        WebApplicationException wae = (WebApplicationException) e;
        return wae.getResponse();
    
      } else {
        ErrorDto errorDto = ExceptionManager.newInstance().registerExceptionAndPrepareErrorDto(e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorDto).build();
    
      }
    }
  }
  
  protected void registerJsonBindingProvider() {
    register(JsonBindingProvider.class);
  }
  
  protected void registerHttpBasicDynamicFeature() {
    register(HttpBasicDynamicFeature.class);
  }
  
  protected void registerRolesAllowedDynamicFeature() {
    register(RolesAllowedDynamicFeature.class);
  }
  
  protected void registerJepSecurityContextBinder() {
    register(new JepSecurityContextBinder());
  }
  
  protected void registerXCacheControlFilter() {
    register(new XCacheControlFilter());
  }
  
  /**
   * Registers exception mapper for json parse exceptions
   * @see org.jepria.server.service.rest.gson.GsonJsonb
   */
  protected void registerExceptionMapperJsonb() {
    register(new ExceptionMapperJsonb());
  }
  
  protected void registerExceptionMapperUndeclaredThrowable() {
    register(new ExceptionMapperUndeclaredThrowable());
  }
  
  protected void registerMetaInfoResource() {
    register(MetaInfoResource.class);
  }

  protected void registerValidation() {
    register(new ExceptionMapperValidation());
  }

  /**
   * Регистрация общего обработчика исключений (обработчик Throwable)
   */
  protected void registerExceptionMapperDefault() {
    register(new ExceptionMapperDefault());
  }

  /**
   * Регистрация обработчика CORS
   */
  protected void registerCorsHandler() {
    register(CorsFilter.class);
  }
}
