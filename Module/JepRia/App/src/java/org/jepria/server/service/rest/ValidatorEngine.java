package org.jepria.server.service.rest;

import java.util.ArrayList;
import java.util.List;

public class ValidatorEngine<T> {
  
  protected final Validator<? super T> validator; 

  public ValidatorEngine(Validator<? super T> validator) { 
    this.validator = validator;
  }
  
  public ValidatorEngine(Class<? extends Validator<? super T>> validatorClass) { 
    if (validatorClass != null) {
      Validator<? super T> validator = newValidatorInstance(validatorClass); 
      this.validator = validator;
    } else {
      this.validator = null;
    }
  }
  
  public void validate(T target) throws ValidationException {
    if (validator != null) {
      
      final List<InvalidParameter> invalidParams = new ArrayList<>();
      
      final ValidationContext context = new ValidationContext() {
        @Override
        public void invalidParameter(String name, Object invalidValue, String message) {
          invalidParams.add(new InvalidParameter(null, name, invalidValue, message));
        }
      };
      
      boolean valid = validator.validate(target, context);
      
      if (!valid) {
        if (invalidParams.isEmpty()) {
          throw new ValidationException();
        } else {
          throw new ValidationException(invalidParams);
        }
      }
    }
  }
  
  protected Validator<? super T> newValidatorInstance(Class<? extends Validator<? super T>> validatorClass) { 
    try {
      return validatorClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
  
}
