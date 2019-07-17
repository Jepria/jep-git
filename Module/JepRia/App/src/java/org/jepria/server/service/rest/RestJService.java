package org.jepria.server.service.rest;

import java.util.Arrays;

import javax.validation.ConstraintValidator;

import org.jepria.server.service.rest.validation.ValidationEngine;

public class RestJService {
  
  protected <T> void validate(T target, Class<? extends ConstraintValidator<?, ? super T>> validatorClass) {
    ValidationEngine.validate(ValidationEngine.createValidators(Arrays.asList(validatorClass)), target);
  }
  
  protected <T> void validate(T target, ConstraintValidator<?, ? super T> validator) {
    ValidationEngine.validate(Arrays.asList(validator), target);
  }
}
