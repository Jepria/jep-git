package org.jepria.server.service.rest.jersey.validate;

import javax.validation.ConstraintViolation;
import java.util.Collection;

public class ValidationException extends Exception {

  public ValidationException() {
    this((Collection<ConstraintViolation<?>>)null);
  }

  public ValidationException(String message) {
    this(message, null);
  }

  protected final Collection<ConstraintViolation<?>> violations;

  public ValidationException(Collection<ConstraintViolation<?>> violations) {
    super();
    this.violations = violations;
  }

  public ValidationException(String message, Collection<ConstraintViolation<?>> violations) {
    super(message);
    this.violations = violations;
  }

  public Collection<ConstraintViolation<?>> getViolations() {
    return violations;
  }
}
