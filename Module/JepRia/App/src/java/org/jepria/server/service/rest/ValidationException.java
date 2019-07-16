package org.jepria.server.service.rest;

/**
 * Exception thrown while validating request parameters
 */
public class ValidationException extends RuntimeException {

  private static final long serialVersionUID = -611295618314876742L;

  
  
  public ValidationException(String message) {
    super(message);
  }
}
