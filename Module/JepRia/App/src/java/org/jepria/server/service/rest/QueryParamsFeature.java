package org.jepria.server.service.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;
import org.jepria.CastMap;
import org.jepria.CastMapBase;
import org.jepria.TypedValueParserImpl;

/**
 * Feature supports annotating resource method parameters with {@link QueryParams} annotation.
 * 
 * @see Doc/App/jersey-custom-method-parameter-injection
 */
public class QueryParamsFeature implements Feature {

  @Override
  public boolean configure(FeatureContext context) {
    context.register(new AbstractBinder() {
      
      @Override
      protected void configure() {
        bind(QueryParamsValueParamProvider.class)
        .to(ValueParamProvider.class)
        .in(Singleton.class);
      }
    });

    return true;
  }
  
  public static class QueryParamsValueParamProvider implements ValueParamProvider {

    @Override
    public Function<ContainerRequest, ?> getValueProvider(Parameter parameter) {
      
      final Class<?> parameterClass = parameter.getRawType();
      final QueryParams annotation = parameter.getAnnotation(QueryParams.class);
      
      if (annotation != null) {
        // annotation is present
        
        if (org.jepria.CastMap.class.isAssignableFrom(parameterClass)) {
          return new CastMapProvider();
          
        } else if (java.util.Map.class.isAssignableFrom(parameterClass)) {
          return new MapProvider();
          
        } else {
          throw new IllegalArgumentException(
              "The parameter type [" + parameterClass.getCanonicalName() + "] is not supported "
                  + "for the [" + QueryParams.class.getCanonicalName() + "] annotation");
        }
      }
      
      return null;
    }
    
    @Override
    public PriorityType getPriority() {
      return Priority.HIGH;
    }
    
    protected class CastMapProvider implements Function<ContainerRequest, CastMap<String, Object>> {

      /**
       * @param creator creates new instances. Not {@code null}, must not return {@code null} 
       */
      protected final Supplier<CastMap<String, Object>> creator;

      /**
       * @param creator creates new instances. Must not be {@code null} or return {@code null} 
       */
      public CastMapProvider(Supplier<CastMap<String, Object>> creator) {
        Objects.requireNonNull(creator);
        this.creator = creator;
      }

      public CastMapProvider() {
        this(() -> new CastMapBase(new TypedValueParserImpl()));
      }

      @Override
      public CastMap<String, Object> apply(ContainerRequest request) {

        final CastMap<String, Object> params = creator.get();

        MultivaluedMap<String, String> m = request.getUriInfo().getQueryParameters();
        if (m != null) {
          m.forEach((key, values) -> {
            if (key != null && values != null && values.size() > 0) {
              // the first declared value wins over the same-named parameters
              params.put(key, values.get(0));
            }
          });
        }
        
        return params; 
      }
    }

    protected class MapProvider implements Function<ContainerRequest, Map<String, Object>> {

      /**
       * @param creator creates new instances. Not {@code null}, must not return {@code null} 
       */
      protected final Supplier<Map<String, Object>> creator;

      /**
       * @param creator creates new instances. Must not be {@code null} or return {@code null} 
       */
      public MapProvider(Supplier<Map<String, Object>> creator) {
        Objects.requireNonNull(creator);
        this.creator = creator;
      }

      public MapProvider() {
        this(() -> new HashMap<>());
      }

      @Override
      public Map<String, Object> apply(ContainerRequest request) {

        final Map<String, Object> params = creator.get();

        MultivaluedMap<String, String> m = request.getUriInfo().getQueryParameters();
        if (m != null) {
          m.forEach((key, values) -> {
            if (key != null && values != null && values.size() > 0) {
              // the first declared value wins over the same-named parameters
              params.put(key, values.get(0));
            }
          });
        }
        
        return params; 
      }
    }
  }
  
}
