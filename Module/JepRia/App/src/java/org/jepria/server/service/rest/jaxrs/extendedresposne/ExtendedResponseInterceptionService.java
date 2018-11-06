package org.jepria.server.service.rest.jaxrs.extendedresposne;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.jvnet.hk2.annotations.Service;

/**
 * @author Michal Gajdos
 */
@Service
public class ExtendedResponseInterceptionService implements InterceptionService {

  private static final ProducesExtendedResponseMethodInterceptor RESOURCE_INTERCEPTOR = new ProducesExtendedResponseMethodInterceptor();

  private static final List<MethodInterceptor> RESOURCE_METHOD_INTERCEPTORS =
      Collections.<MethodInterceptor>singletonList(RESOURCE_INTERCEPTOR);

  @Override
  public Filter getDescriptorFilter() {
    // We're only interested in classes (resources, providers) from this applications packages.
    return new Filter() {
      @Override
      public boolean matches(final Descriptor d) {
        // TODO
        return true;
//        final String clazz = d.getImplementation();
//        return clazz.startsWith("sk.dejavu.blog.examples.intercepting.resources")
//            || clazz.startsWith("sk.dejavu.blog.examples.intercepting.providers");
      }
    };
  }

  @Override
  public List<MethodInterceptor> getMethodInterceptors(final Method method) {
    // Apply interceptors only to methods annotated with @Intercept.
    if (method.isAnnotationPresent(ProducesExtendedResponse.class)) {
      // TODO other checkings
      return RESOURCE_METHOD_INTERCEPTORS;
    }
    return null;
  }
  
  @Override
  public List<ConstructorInterceptor> getConstructorInterceptors(
      Constructor<?> arg0) {
    return null;
  }

}
