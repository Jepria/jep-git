package org.jepria.server.service.rest;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import com.google.gson.JsonSyntaxException;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;
import org.jepria.CastMap;
import org.jepria.CastMapBase;
import org.jepria.TypedValueParserImpl;
import org.jepria.server.service.rest.gson.DefaultGsonBuilder;

/**
 * Feature supports annotating resource method parameters with {@link BodyParams} annotation.
 * 
 * @see Doc/App/jersey-custom-method-parameter-injection
 */
public class BodyParamsFeature implements Feature {

  @Override
  public boolean configure(FeatureContext context) {
    context.register(new AbstractBinder() {
      
      @Override
      protected void configure() {
        bind(BodyParamsValueParamProvider.class)
        .to(ValueParamProvider.class)
        .in(Singleton.class);
      }
    });

    return true;
  }
  
  public static class BodyParamsValueParamProvider implements ValueParamProvider {

    @Override
    public Function<ContainerRequest, ?> getValueProvider(Parameter parameter) {
      
      final Class<?> parameterClass = parameter.getRawType();
      final BodyParams annotation = parameter.getAnnotation(BodyParams.class);
      
      if (annotation != null) {
        // annotation is present
        
        if (org.jepria.CastMap.class.isAssignableFrom(parameterClass)) {
          return new CastMapProvider();
          
        } else if (java.util.Map.class.isAssignableFrom(parameterClass)) {
          return new MapProvider();
          
        } else {
          throw new IllegalArgumentException(
              "The parameter type [" + parameterClass.getCanonicalName() + "] is not supported "
                  + "for the [" + BodyParams.class.getCanonicalName() + "] annotation");
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

        final Map<String, ?> m = entityToMap(request);

        if (m != null) {
          for (String key: m.keySet()) {
            Object value = m.get(key);
            if (key != null && value != null) {
              // the first declared value wins over the same-named parameters
              params.putIfAbsent(key, value);
            }
          }
        }
        
        return params; 
      }
    }

    protected String inputStreamToString(InputStream inputStream, String charset)
            throws IOException {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }
      return result.toString(charset);
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

        final Map<String, ?> m = entityToMap(request);

        if (m != null) {
          for (String key: m.keySet()) {
            Object value = m.get(key);
            if (key != null && value != null) {
              // the first declared value wins over the same-named parameters
              params.putIfAbsent(key, value);
            }
          }
        }
        
        return params; 
      }
    }

    protected Map<String, ?> entityToMap(ContainerRequest request) {
      final Map<String, ?> m;
      final String entity;

      try (InputStream in = request.getEntityStream()) {
        // TODO determine the charset from the request header
        entity = inputStreamToString(in, "UTF-8");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      try {
        m = new DefaultGsonBuilder().build().fromJson(entity, (Type) new HashMap<String, Object>().getClass());
      } catch (JsonSyntaxException e) {
        // TODO throw extended exception with attached source that caused the exception?
        throw e;
      }


      return m;
    }
  }
  
}
