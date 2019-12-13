package org.jepria.server.service.rest.jersey.validate;


import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public interface DirectValidator {
  /**
   *
   * @param object object to validate
   * @param annotations
   * @param propertyPath path depending on the object context
   * @param <T>
   * @return
   */
  <T> Set<ConstraintViolation<T>> validate(T object, List<Annotation> annotations, Path propertyPath);
}