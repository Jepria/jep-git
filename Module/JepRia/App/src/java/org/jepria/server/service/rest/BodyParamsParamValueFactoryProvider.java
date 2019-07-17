package org.jepria.server.service.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;
import org.jepria.CastMap;
import org.jepria.CastMapBase;
import org.jepria.TypedValueParserImpl;

/**
 * A ValueFactoryProvider for the parameters annotated with {@link BodyParams}.
 * 
 * @see Doc/App/jersey-custom-method-parameter-injection
 */
public class BodyParamsParamValueFactoryProvider extends AbstractValueFactoryProvider {

  @Inject
  public BodyParamsParamValueFactoryProvider(MultivaluedParameterExtractorProvider mpep,
      ServiceLocator locator) {
    super(mpep, locator, Parameter.Source.UNKNOWN);
  }

  @Override
  protected Factory<?> createValueFactory(Parameter parameter) {
    
    final Class<?> parameterClass = parameter.getRawType();
    final BodyParams annotation = parameter.getAnnotation(BodyParams.class);
    
    if (annotation != null) {
      // annotation is present
      
      if (org.jepria.CastMap.class.isAssignableFrom(parameterClass)) {
        return new CastMapFactory();
        
      } else if (java.util.Map.class.isAssignableFrom(parameterClass)) {
        return new MapFactory();
        
      } else {
        throw new IllegalArgumentException(
            "The parameter type [" + parameterClass.getCanonicalName() + "] is not supported "
                + "for the [" + BodyParams.class.getCanonicalName() + "] annotation");
      }
    }
    
    return null;
  }
  
  public static class BodyParamsParamInjectionResolver extends ParamInjectionResolver<BodyParams> {
    public BodyParamsParamInjectionResolver() {
      super(BodyParamsParamValueFactoryProvider.class);  
    }
  }

  public static class Binder extends AbstractBinder {
    @Override
    protected void configure() {
      bind(BodyParamsParamValueFactoryProvider.class)
      .to(ValueFactoryProvider.class)
      .in(Singleton.class);
      bind(BodyParamsParamInjectionResolver.class)
      .to(new TypeLiteral<InjectionResolver<BodyParams>>(){})
      .in(Singleton.class);
      bindFactory(CastMapFactory.class)
      .to(org.jepria.CastMap.class)
      .in(Singleton.class);
      bindFactory(MapFactory.class)
      .to(java.util.Map.class)
      .in(Singleton.class);
    } 
  }

  protected class CastMapFactory extends AbstractContainerRequestValueFactory<CastMap<String, Object>> {

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

      final Map<String, ?> m;
      // TODO determine the charset from the request header
      try (Reader reader = new InputStreamReader(getContainerRequest().getEntityStream(), Charset.forName("UTF-8"))) {
        m = new JsonSerializer().deserialize(reader);
        
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      
      if (m != null) {
        m.forEach((key, value) -> {
          if (key != null && value != null) {
            // the first declared value wins over the same-named parameters
            params.put(key, value);
          }
        });
      }
      
      return params; 
    }

    @Override
    public void dispose(CastMap<String, Object> t) { }
  }

  protected class MapFactory extends AbstractContainerRequestValueFactory<Map<String, Object>> {

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

      final Map<String, ?> m;
      // TODO determine the charset from the request header
      try (Reader reader = new InputStreamReader(getContainerRequest().getEntityStream(), Charset.forName("UTF-8"))) {
        m = new JsonSerializer().deserialize(reader);
        
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      
      if (m != null) {
        m.forEach((key, value) -> {
          if (key != null && value != null) {
            params.put(key, value);
          }
        });
      }
      
      return params; 
    }

    @Override
    public void dispose(Map<String, Object> t) { }
  }
}
