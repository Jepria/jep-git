package org.jepria.server.service.rest.jersey.validate;


import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.constraints.*;
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

    for (Annotation annotation: annotations) {
      processAnnotation(object, annotation, propertyPath, constraintViolations);
    }

    return constraintViolations;
  }

  /**
   * Process general annotation
   * @param object
   * @param annotation
   * @param propertyPath
   * @param constraintViolations
   * @param <T>
   */
  protected <T> void processAnnotation(Object object, Annotation annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {

    if (AssertFalse.class.equals(annotation.annotationType())) {
      AssertFalse a = (AssertFalse) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (AssertTrue.class.equals(annotation.annotationType())) {
      AssertTrue a = (AssertTrue) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (DecimalMax.class.equals(annotation.annotationType())) {
      DecimalMax a = (DecimalMax) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (DecimalMin.class.equals(annotation.annotationType())) {
      DecimalMin a = (DecimalMin) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Digits.class.equals(annotation.annotationType())) {
      Digits a = (Digits) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Email.class.equals(annotation.annotationType())) {
      Email a = (Email) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Future.class.equals(annotation.annotationType())) {
      Future a = (Future) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (FutureOrPresent.class.equals(annotation.annotationType())) {
      FutureOrPresent a = (FutureOrPresent) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Max.class.equals(annotation.annotationType())) {
      Max a = (Max) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Min.class.equals(annotation.annotationType())) {
      Min a = (Min) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Negative.class.equals(annotation.annotationType())) {
      Negative a = (Negative) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (NegativeOrZero.class.equals(annotation.annotationType())) {
      NegativeOrZero a = (NegativeOrZero) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (NotBlank.class.equals(annotation.annotationType())) {
      NotBlank a = (NotBlank) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (NotEmpty.class.equals(annotation.annotationType())) {
      NotEmpty a = (NotEmpty) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (NotNull.class.equals(annotation.annotationType())) {
      NotNull a = (NotNull) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Null.class.equals(annotation.annotationType())) {
      Null a = (Null) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Past.class.equals(annotation.annotationType())) {
      Past a = (Past) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (PastOrPresent.class.equals(annotation.annotationType())) {
      PastOrPresent a = (PastOrPresent) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Pattern.class.equals(annotation.annotationType())) {
      Pattern a = (Pattern) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Positive.class.equals(annotation.annotationType())) {
      Positive a = (Positive) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (PositiveOrZero.class.equals(annotation.annotationType())) {
      PositiveOrZero a = (PositiveOrZero) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }

    if (Size.class.equals(annotation.annotationType())) {
      Size a = (Size) annotation;
      processAnnotation(object, a, propertyPath, constraintViolations);
    }
  }

  protected <T> void processAnnotation(Object object, AssertFalse annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, AssertTrue annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, DecimalMax annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, DecimalMin annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, Digits annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, Email annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, Future annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, FutureOrPresent annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, Max annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, Min annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, Negative annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, NegativeOrZero annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, NotBlank annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, NotEmpty annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, NotNull annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    if (object == null) {
      ConstraintViolationImpl<T> cv = new ConstraintViolationImpl<>();
      cv.setMessage(annotation.message()); // TODO evaluate message of form {string.resource.reference}
      cv.setPropertyPath(propertyPath);
      constraintViolations.add(cv);
    }
  }

  protected <T> void processAnnotation(Object object, Null annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, Past annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, PastOrPresent annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, Pattern annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    if (object instanceof CharSequence) {
      CharSequence cs = (CharSequence)object;
      final String regexp = annotation.regexp();
      if (!cs.toString().matches(regexp)) {
        ConstraintViolationImpl<T> cv = new ConstraintViolationImpl<>();
        cv.setMessage(annotation.message()); // TODO evaluate message of form {string.resource.reference}
        cv.setPropertyPath(propertyPath);
        constraintViolations.add(cv);
      }
    }
  }

  protected <T> void processAnnotation(Object object, Positive annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, PositiveOrZero annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }

  protected <T> void processAnnotation(Object object, Size annotation, Path propertyPath, Set<ConstraintViolation<T>> constraintViolations) {
    // TODO not implemented yet
  }
}