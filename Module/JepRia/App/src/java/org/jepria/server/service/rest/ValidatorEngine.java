package org.jepria.server.service.rest;

import java.util.HashMap;
import java.util.Map;

import org.jepria.server.service.rest.Validator.Context;

public class ValidatorEngine<T> {
  
  protected final Validator<T> validator;

  public ValidatorEngine(Validator<T> validator) {
    this.validator = validator;
  }
  
  public ValidatorEngine(Class<Validator<T>> validatorClass) {
    if (validatorClass != null) {
      @SuppressWarnings("unchecked")
      Validator<T> validator = (Validator<T>)newValidatorInstance(validatorClass); 
      this.validator = validator;
    } else {
      this.validator = null;
    }
  }
  
  protected class InvalidParameter {
    public final Object invalidValue;
    public final String message;
    
    public InvalidParameter(Object invalidValue, String message) {
      this.invalidValue = invalidValue;
      this.message = message;
    }
  }
  
  public void validate(T target) throws ValidationException {
    if (validator != null) {
      
      final Map<String, InvalidParameter> invalidParams = new HashMap<>();
      
      final Validator.Context context = new Context() {
        @Override
        public void invalidParameter(String name, Object invalidValue, String message) {
          invalidParams.put(name, new InvalidParameter(invalidValue, message));
        }
      };
      
      boolean valid = validator.validate(target, context);
      
      if (!valid) {
        if (invalidParams.isEmpty()) {
          throw new ValidationException("One or more parameter value is invalid");
        } else {
          throw new ValidationException("Invalid parameter values: " + invalidParams.keySet());
        }
      }
    }
  }
  
  protected Validator<?> newValidatorInstance(Class<? extends Validator<?>> validatorClass) {
    try {
      return validatorClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
  
}
