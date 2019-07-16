package org.jepria.server.service.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;
import org.jepria.CastMap;
import org.jepria.CastMapBase;
import org.jepria.TypedValueParserImpl;

/**
 * A ValueFactoryProvider for the parameters annotated with {@link QueryParams}.
 */
public class QueryParamsParamValueFactoryProvider extends AbstractValueFactoryProvider {

  @Inject
  public QueryParamsParamValueFactoryProvider(MultivaluedParameterExtractorProvider mpep,
      ServiceLocator locator) {
    super(mpep, locator, Parameter.Source.UNKNOWN);
  }

  @Override
  protected Factory<?> createValueFactory(Parameter parameter) {
    
    final Class<?> parameterClass = parameter.getRawType();
    final QueryParams annotation = parameter.getAnnotation(QueryParams.class);
    
    if (annotation != null) {
      // annotation is present
      
      if (org.jepria.CastMap.class.isAssignableFrom(parameterClass)) {
        CastMapFactory factory = new CastMapFactory();
        // inject validator
        @SuppressWarnings("unchecked")
        Class<Validator<CastMap<String, Object>>> validatorClass = (Class<Validator<CastMap<String, Object>>>)annotation.validator();
        if (validatorClass != null && (Class<?>)validatorClass != (Class<?>)QueryParams.VoidValidator.class) {
          factory.injectValidator(validatorClass);
        }
        return factory;
        
      } else if (java.util.Map.class.isAssignableFrom(parameterClass)) {
        MapFactory factory = new MapFactory();
        // inject validator
        @SuppressWarnings("unchecked")
        Class<Validator<Map<String, Object>>> validatorClass = (Class<Validator<Map<String, Object>>>)annotation.validator();
        if (validatorClass != null && (Class<?>)validatorClass != (Class<?>)QueryParams.VoidValidator.class) {
          factory.injectValidator(validatorClass);
        }
      } else {
        throw new IllegalArgumentException(
            "The parameter type [" + parameterClass + "] is not supported "
                + "for the [" + QueryParams.class.getCanonicalName() + "] annotation");
      }
    }
    return null;
  }
  
  public static class QueryParamsParamInjectionResolver extends ParamInjectionResolver<QueryParams> {
    public QueryParamsParamInjectionResolver() {
      super(QueryParamsParamValueFactoryProvider.class);  
    }
  }

  public static class Binder extends AbstractBinder {
    @Override
    protected void configure() {
      bind(QueryParamsParamValueFactoryProvider.class)
      .to(ValueFactoryProvider.class)
      .in(Singleton.class);
      bind(QueryParamsParamInjectionResolver.class)
      .to(new TypeLiteral<InjectionResolver<QueryParams>>(){})
      .in(Singleton.class);
      bindFactory(CastMapFactory.class)
      .to(org.jepria.CastMap.class)
      .in(Singleton.class);
      bindFactory(MapFactory.class)
      .to(java.util.Map.class)
      .in(Singleton.class);
    } 
  }

  protected class CastMapFactory extends ValidatingParamValueFactory<CastMap<String, Object>> {

    /**
     * @param creator creates new instances. Not {@code null}, must not return {@code null} 
     */
    protected final Supplier<CastMap<String, Object>> creator;

    /**
     * @param creator creates new instances. Must not be {@code null} or return {@code null} 
     */
    public CastMapFactory(Supplier<CastMap<String, Object>> creator) {
      Objects.requireNonNull(creator);
      this.creator = creator;
    }

    public CastMapFactory() {
      this(() -> new CastMapBase(new TypedValueParserImpl()));
    }

    @Override
    public CastMap<String, Object> provide() {
      final CastMap<String, Object> params = creator.get();

      MultivaluedMap<String, String> m = getContainerRequest().getUriInfo().getQueryParameters();
      if (m != null) {
        m.forEach((key, values) -> {
          if (key != null && values != null && values.size() > 0) {
            // the first declared value wins over the same-named parameters
            params.put(key, values.get(0));
          }
        });
      }
      
      validate(params);
      
      return params; 
    }

    @Override
    public void dispose(CastMap<String, Object> t) { }
  }

  protected class MapFactory extends ValidatingParamValueFactory<Map<String, Object>> {

    /**
     * @param creator creates new instances. Not {@code null}, must not return {@code null} 
     */
    protected final Supplier<Map<String, Object>> creator;

    /**
     * @param creator creates new instances. Must not be {@code null} or return {@code null} 
     */
    public MapFactory(Supplier<Map<String, Object>> creator) {
      Objects.requireNonNull(creator);
      this.creator = creator;
    }

    public MapFactory() {
      this(() -> new HashMap<>());
    }

    @Override
    public Map<String, Object> provide() {
      final Map<String, Object> params = creator.get();

      MultivaluedMap<String, String> m = getContainerRequest().getUriInfo().getQueryParameters();
      if (m != null) {
        m.forEach((key, values) -> {
          if (key != null && values != null && values.size() > 0) {
            // the first declared value wins over the same-named parameters
            params.put(key, values.get(0));
          }
        });
      }
      
      validate(params);

      return params; 
    }

    @Override
    public void dispose(Map<String, Object> t) { }
  }
}
