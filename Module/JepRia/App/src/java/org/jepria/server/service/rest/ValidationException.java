package org.jepria.server.service.rest;

import java.util.Collection;

/**
 * Exception thrown while validating request parameters
 */
public class ValidationException extends RuntimeException {

  private static final long serialVersionUID = -611295618314876742L;

  private final Collection<InvalidParameter> invalidParams;
  
  public ValidationException() {
    this(null);
  }
  
  public ValidationException(Collection<InvalidParameter> invalidParams) {
    this.invalidParams = invalidParams;
  }

  public Collection<InvalidParameter> getInvalidParams() {
    return invalidParams;
  }
}
