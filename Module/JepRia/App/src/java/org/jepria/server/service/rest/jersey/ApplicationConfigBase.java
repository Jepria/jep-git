package org.jepria.server.service.rest.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jepria.server.service.rest.XCacheControlFilter;
import org.jepria.server.service.rest.gson.JsonBindingProvider;
import org.jepria.server.service.security.HttpBasicDynamicFeature;

import javax.json.bind.JsonbException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;

public class ApplicationConfigBase extends ResourceConfig {

  public ApplicationConfigBase() {
    packages("io.swagger.jaxrs.listing");


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

    registerExceptionMapper(JsonbException.class, new ExceptionMapperJsonb());

    // Note: unchecked-исключения могут быть обёрнуты в java.lang.reflect.UndeclaredThrowableException, и таким образом не отлавливаться целевыми обработчиками.
    registerExceptionMapper(UndeclaredThrowableException.class, new ExceptionMapperUndeclaredThrowable());

    // Подключение отладочного обработчика исключений (только для отладки! Иначе штатные исключения вроде jaxaw.ws.rs.NotFoundException не будут корректно обрабатываться на уровне Jersey)
    // registerExceptionMapper(Throwable.class, new ExceptionMapperDefault());


  }

  /**
   * ExceptionMapper for JsonbException
   */
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
   * ExceptionMapper for UndeclaredThrowableException
   */
  public class ExceptionMapperUndeclaredThrowable implements ExceptionMapper<UndeclaredThrowableException> {
    @Override
    public Response toResponse(UndeclaredThrowableException e) {
      Throwable cause = e.getCause();

      // delegate exception handling to the proper ExceptionMapper
      ExceptionMapper mapper = getRegisteredExceptionMapper(cause.getClass());
      if (mapper != null) {
        // use registered mapper
        return mapper.toResponse(cause);

      } else {
        // Do not rethrow exception from here (it will be swallowed by the container)

        // use default (lowest-level) mapper
        return new ExceptionMapperDefault().toResponse(cause);
      }
    }
  }

  /**
   * Lowest-level ExceptionMapper that simply logs exceptions which can potentially be swallowed. Used for debugging purposes
   */
  public static class ExceptionMapperDefault implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable e) {

      // TODO this printing results INFO log level in Tomcat logs. Make the Level ERROR
      System.err.println(new Date() + ": Exception handled by " + ExceptionMapperDefault.class.getCanonicalName() + ":");
      e.printStackTrace();

      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }



  //------------ ExceptionMapper registration ------------//

  // Механизм локальной регистрации ExceptionMapperов нужен для того, чтобы иметь возможность по возникшему в программе исключению (точнее, по его классу),
  // получить ExceptionMapper, который был зарегистрирован для него (или для предка этого исключения) при инициализации программы.
  // TODO похоже, что в Jersey нет стандартного механизма getRegisteredExceptionMapper(Class<E> exceptionClass). В случае, если он будет обнаружен, удалить данную реализацию
  // Механизм требует регистрации ExceptionMapperов не стандартным методом register, а специальным методом registerExceptionMapper, который помимо собственно вызова register,
  // сохраняет зарегистрированный ExceptionMapper в связке с типом исключения, для которого он регистрируется.

  /**
   * Internal storage of the registered ExceptionMappers
   */
  // TODO this Map must use type Class<? extends Throwable> as key (not String), but it seems to cause memory leaks (see link below). So use canonical classnames of the exception classes instead.
  // see https://stackoverflow.com/questions/2625546/is-using-the-class-instance-as-a-map-key-a-best-practice
  protected final Map<String, ExceptionMapper<?>> exceptionMappersRegistered = new HashMap<>();

  protected <E extends Throwable> void registerExceptionMapper(Class<E> exceptionClass, ExceptionMapper<E> mapper) {
    register(mapper);
    exceptionMappersRegistered.put(exceptionClass.getCanonicalName(), mapper);
  }

  /**
   * Looks up the closest superclass of the exception and returns the ExceptionMapper registered for it (if any)
   * @param exceptionClass
   * @param <E>
   * @return
   */
  protected <E extends Throwable> ExceptionMapper<? super E> getRegisteredExceptionMapper(Class<E> exceptionClass) {

    List<String> hierarchy = new ArrayList<>();
    Class<?> hierarchyElement = exceptionClass;
    while (hierarchyElement != null) {
      hierarchy.add(hierarchyElement.getCanonicalName());
      hierarchyElement = hierarchyElement.getSuperclass();
    }

    int minDegree = -1;
    ExceptionMapper<?> closestMapper = null;
    for (String exceptionClassName: exceptionMappersRegistered.keySet()) {
      int degree = hierarchy.indexOf((String)exceptionClassName);
      if (degree != -1 && (minDegree == -1 || degree < minDegree)) {
        minDegree = degree;
        closestMapper = exceptionMappersRegistered.get((String)exceptionClassName);
      }
    }

    if (minDegree != -1 && closestMapper != null) {
      return (ExceptionMapper<? super E>)closestMapper;
    } else {
      return null;
    }
  }

}
