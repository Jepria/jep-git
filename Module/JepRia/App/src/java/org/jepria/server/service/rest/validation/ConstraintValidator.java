package org.jepria.server.service.rest.validation;

import java.lang.annotation.Annotation;

public interface ConstraintValidator<T> extends javax.validation.ConstraintValidator<Annotation, T> {
  @Override
  default void initialize(Annotation arg0) { }
}
