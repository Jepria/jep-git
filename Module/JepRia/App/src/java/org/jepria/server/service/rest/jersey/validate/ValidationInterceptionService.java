package org.jepria.server.service.rest.jersey.validate;

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class ValidationInterceptionService implements InterceptionService {

  private final static MethodInterceptor METHOD_INTERCEPTOR = new ValidationMethodInterceptor();
  private final static List<MethodInterceptor> METHOD_INTERCEPTORS = Collections.singletonList(METHOD_INTERCEPTOR);

  @Override
  public Filter getDescriptorFilter() {
    return new Filter() {
      @Override
      public boolean matches(Descriptor descriptor) {
        // TODO filter resource classes (jaxrs-endpoints) only!
        return true;
      }
    };
  }

  @Override
  public List<MethodInterceptor> getMethodInterceptors(Method method) {
    Predicate<Method> mf = getMethodFilter();
    if (mf != null && !mf.test(method)) {
      return null;
    } else {
      return METHOD_INTERCEPTORS;
    }
  }

  @Override
  public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> constructor) {
    // no constructor interception
    return null;
  }

  /**
   * Equivalent for the {@link #getDescriptorFilter()}: tests whether or not the method should be intercepted by the interceptor
   * @return
   */
  protected Predicate<Method> getMethodFilter() {
    return new ResourceMethodFilter();
  }

  public static class ResourceMethodFilter implements Predicate<Method> {

    @Override
    public boolean test(Method method) {
      // filter Jaxrs-annotated methods only


      Annotation[] annotations = method.getDeclaredAnnotations();
      for (Annotation annotation : annotations) {
        if (isJaxrsAnnotation(annotation)) {
          return true;
        }
      }

      return false;
    }

    private static boolean isJaxrsAnnotation(Annotation annotation) {
      Class<? extends Annotation> annotationType;

      if (annotation instanceof Proxy) {
        // in Jersey the resource method's annotations are (always?) behind proxies
        annotationType = getProxiedAnnotationType((Proxy) annotation);
      } else {
        annotationType = annotation.annotationType();
      }

      return isJaxrsAnnotation(annotationType);
    }

    private static boolean isJaxrsAnnotation(Class<? extends Annotation> annotationType) {
      // TODO refine criteria!
      String annotationTypeName = annotationType.getCanonicalName();
      return "javax.ws.rs.DELETE".equals(annotationTypeName)
              || "javax.ws.rs.GET".equals(annotationTypeName)
              || "javax.ws.rs.HEAD".equals(annotationTypeName)
              || "javax.ws.rs.OPTIONS".equals(annotationTypeName)
              || "javax.ws.rs.POST".equals(annotationTypeName)
              || "javax.ws.rs.PUT".equals(annotationTypeName);
    }

    /**
     * @param proxiedAnnotation
     * @return Class of the proxied {@link Annotation}. If the object behind the proxy is not an {@link Annotation}, {@link IllegalArgumentException} is thrown.
     */
    private static Class<? extends Annotation> getProxiedAnnotationType(Proxy proxiedAnnotation) {
      InvocationHandler ih = Proxy.getInvocationHandler(proxiedAnnotation);

      Method annotationTypeMethod;
      try {
        annotationTypeMethod = Annotation.class.getMethod("annotationType");
      } catch (NoSuchMethodException e) {
        throw new IllegalArgumentException("The object behind the proxy is not a java.lang.annotation.Annotation", e);
      }

      Object invocationResult;
      try {
        invocationResult = ih.invoke(proxiedAnnotation, annotationTypeMethod, null);
      } catch (Throwable e) {
        // impossible
        throw new RuntimeException(e);
      }

      Class<? extends Annotation> annotationType = (Class<? extends Annotation>) invocationResult;

      return annotationType;
    }
  }
}
