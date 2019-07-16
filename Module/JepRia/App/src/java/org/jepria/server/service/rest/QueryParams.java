package org.jepria.server.service.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Binds all values of a HTTP query parameters to a resource method parameter.
 * The annotated parameter must be of either {@link java.util.Map} or {@link org.jepria.CastMap} type.
 * The annotated method parameter is not {@code null}, but at least an empty map. 
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParams {
  // exactly that generic declaration necessary
  Class<? extends Validator<? extends Map<String, ?>>> validator() default VoidValidator.class;
  
  public static final class VoidValidator implements Validator<Map<String, ?>> {
    @Override
    public boolean validate(Map<String, ?> value, ValidationContext context) {
      return true;
    }
  }
}
