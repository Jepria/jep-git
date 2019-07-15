package org.jepria.server.service.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Binds all values of a HTTP query parameters to a resource method parameter.
 * The annotated parameter must be of either {@link java.util.Map} or {@link org.jepria.CastMap} type. 
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParams {
  // exactly that generic declaration necessary
  Class<? extends Validator<? extends Map<String, ?>>> validator() default Validator.Void.class;
}
