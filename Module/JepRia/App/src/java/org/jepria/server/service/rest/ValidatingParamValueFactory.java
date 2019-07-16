package org.jepria.server.service.rest;

import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;

/**
 * An {@link AbstractContainerRequestValueFactory} that validates parameter map
 * @param <T>
 */
public abstract class ValidatingParamValueFactory<T> extends AbstractContainerRequestValueFactory<T> {
  
  protected ValidatorEngine<? super T> validatorEngine = null;
  
  protected void validate(T target) {
    if (validatorEngine != null) {
      validatorEngine.validate(target);
    }
  }
  
  public void injectValidator(Class<Validator<T>> validatorClass) {
    this.validatorEngine = new ValidatorEngine<>(validatorClass);
  }
}
