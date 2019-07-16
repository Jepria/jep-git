package org.jepria.server.service.rest;

import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;

/**
 * An {@link AbstractContainerRequestValueFactory} that validates parameter map
 * @param <T>
 */
public abstract class ValidatingParamValueFactory<T> extends AbstractContainerRequestValueFactory<T> {
  
  protected ValidatorEngine<T> validatorEngine = null;
  
  protected void validate(T target) {
    if (validatorEngine != null) {
      validatorEngine.validate(target);
    }
  }
  
  public void injectValidator(Class<? extends Validator<?>> validatorClass) {
    if (validatorClass != null 
        && (Class<?>)validatorClass != (Class<?>)Validator.Void.class) {
      @SuppressWarnings("unchecked")
      Class<Validator<T>> validatorClass0 = (Class<Validator<T>>)validatorClass;
      this.validatorEngine = new ValidatorEngine<>(validatorClass0);
    }
  }
}
