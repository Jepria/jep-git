package org.jepria.server.service.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.ConfiguredValidator;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.model.Parameter;
import org.jepria.server.service.rest.validation.ValidationEngine;
import org.jepria.server.service.rest.validation.ValidatorImpl;


public class ConfiguredValidatorImpl extends ValidatorImpl implements ConfiguredValidator {

  public static class Binder extends AbstractBinder {
    @Override
    protected void configure() {
      bindAsContract(ConfiguredValidatorImpl.class).to(ConfiguredValidator.class).in(Singleton.class);
    }
  }
  
  @Override
  public void validateResourceAndInputParams(Object arg0, Invocable method, Object[] params)
      throws ConstraintViolationException {
    
    List<Parameter> paramDefs = method.getParameters();
    if (paramDefs != null) {
      
      final Set<ConstraintViolation<?>> queryParamsViolations = new HashSet<>();
      final Set<ConstraintViolation<?>> bodyParamsViolations = new HashSet<>();
      
      for (int i = 0; i < paramDefs.size(); i++) {
        final Parameter paramDef = paramDefs.get(i);
        final Object paramVal = params[i];
        
        { // process @BodyParams
          final BodyParams annotation = paramDef.getAnnotation(BodyParams.class);
          
          if (annotation != null) {
            // annotation is present
            
            Class<? extends ConstraintValidator<?, ? super Map<String, ?>>> validatorClass = annotation.validator();
            if (validatorClass != null && validatorClass != BodyParams.VoidValidator.class) {
              try {
                @SuppressWarnings("unchecked")
                Map<String, ?> paramValCast = (Map<String, ?>) paramVal;
                ValidationEngine.validate(ValidationEngine.createValidator(validatorClass), paramValCast);
              } catch (ConstraintViolationException e) {
                if (e.getConstraintViolations() != null) {
                  bodyParamsViolations.addAll(e.getConstraintViolations());
                }
              }
            }
            
          }
        }
        
        { // process @QueryParams
          final QueryParams annotation = paramDef.getAnnotation(QueryParams.class);
          
          if (annotation != null) {
            // annotation is present
            
            Class<? extends ConstraintValidator<?, ? super Map<String, ?>>> validatorClass = annotation.validator();
            if (validatorClass != null && validatorClass != QueryParams.VoidValidator.class) {
              try {
                @SuppressWarnings("unchecked")
                Map<String, ?> paramValCast = (Map<String, ?>) paramVal;
                ValidationEngine.validate(ValidationEngine.createValidator(validatorClass), paramValCast);
              } catch (ConstraintViolationException e) {
                if (e.getConstraintViolations() != null) {
                  queryParamsViolations.addAll(e.getConstraintViolations());
                }
              }
            }
            
          }
        }
      }
      
      if (!queryParamsViolations.isEmpty() || !bodyParamsViolations.isEmpty()) {
        // TODO add 'query' or 'body' identifiers to ConstraintViolations
        
        final Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.addAll(queryParamsViolations);
        violations.addAll(bodyParamsViolations);
        
        throw new ConstraintViolationException(violations);
      }
    }
  }
  
  public void validateResult(Object arg0, Invocable arg1, Object arg2) throws ConstraintViolationException {
    // TODO Auto-generated method stub
    
  }
}
