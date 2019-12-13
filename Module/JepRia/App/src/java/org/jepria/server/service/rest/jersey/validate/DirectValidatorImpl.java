package org.jepria.server.service.rest.jersey.validate;


import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DirectValidatorImpl implements DirectValidator {

  @Override
  public <T> Set<ConstraintViolation<T>> validate(T object, List<Annotation> annotations, Path propertyPath) {

    Set<ConstraintViolation<T>> constraintViolations = new LinkedHashSet<>(); // maintain insertion order

    if (annotations == null || annotations.isEmpty()) {
      return constraintViolations;
    }


    // @NotNull
    for (Annotation annotation: annotations) {
      if (NotNull.class.equals(annotation.annotationType())) {
        NotNull notNull = (NotNull)annotation;
        if (object == null) {
          ConstraintViolationImpl<T> cv = new ConstraintViolationImpl<>();
          cv.setMessage(notNull.message());
          cv.setPropertyPath(propertyPath);
          constraintViolations.add(cv);

          // @NotNull failure aborts all further validation
          return constraintViolations;
        }
      }
    }

    return constraintViolations;
  }
}