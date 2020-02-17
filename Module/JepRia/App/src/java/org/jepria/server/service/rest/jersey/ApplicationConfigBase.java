package org.jepria.server.service.rest.jersey;

import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.spi.ExceptionMappers;
import org.jepria.server.service.rest.ErrorDto;
import org.jepria.server.service.rest.MetaInfoResource;
import org.jepria.server.service.rest.XCacheControlFilter;
import org.jepria.server.service.rest.gson.JsonBindingProvider;
import org.jepria.server.service.rest.jersey.validate.ExceptionMapperValidation;
import org.jepria.server.service.rest.jersey.validate.ValidationInterceptionService;
import org.jepria.server.service.security.CorsResponseFilter;
import org.jepria.server.service.security.HttpBasicDynamicFeature;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
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

    register(JsonBindingProvider.class);
    register(HttpBasicDynamicFeature.class);
    register(RolesAllowedDynamicFeature.class);


    register(new JepSecurityContextBinder());

    /*
    Note: Cache-Control header replacement for React Native/Expo mobile apps
    TODO remove when okHttp Cache-Control header issue will be fixed https://github.com/expo/expo/issues/1639
     */
    register(new XCacheControlFilter());


    // register exception mappers

    register(new ExceptionMapperJsonb());

    // Note: unchecked-исключения могут быть обёрнуты в java.lang.reflect.UndeclaredThrowableException, и таким образом не отлавливаться целевыми обработчиками.
    register(new ExceptionMapperUndeclaredThrowable());

    registerExceptionMapperDefault();

    registerMetaInfoResource();

    registerValidation();

    registerCorsHandler();
  }

  /**
   * ExceptionMapper for JsonbException
   */
  public static class ExceptionMapperJsonb implements ExceptionMapper<JsonbException> {
    @Override
    public Response toResponse(JsonbException e) {
      e.printStackTrace();
      
      // The exception is triggered by the client data, so its stacktrace contains no private
      
      // Collect messages from every exception in the stack
      // The top-level exception is just a wrapper (see org.jepria.server.service.rest.gson.GsonJsonb)
      String clientErrorMessage;
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
        clientErrorMessage = sb.toString();
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

  protected void registerMetaInfoResource() {
    register(MetaInfoResource.class);
  }

  protected void registerValidation() {
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(ValidationInterceptionService.class)
                .to(InterceptionService.class)
                .in(Singleton.class);
      }
    });

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
    register(CorsResponseFilter.class);
  }
}
