package org.jepria.server.service.rest;

public interface Validator<T> {
  boolean validate(T value, ValidationContext context);
}
