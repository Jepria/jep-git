package org.jepria.server.service.rest;

import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;

/**
 * An {@link AbstractContainerRequestValueFactory} that validates values 
 * with an injected {@link Validator}
 * @param <T> value type
 */
public abstract class ValidatingParamValueFactory<T> extends AbstractContainerRequestValueFactory<T> {
  
  protected ValidatorEngine<? super T> validatorEngine = null;
  
  protected void validate(T target) {
    if (validatorEngine != null) {
      validatorEngine.validate(target);
    }
  }
  
  /**
   * Injects a {@link Validator} for validating the values
   * @param validatorClass
   */
  public void injectValidator(Class<? extends Validator<? super T>> validatorClass) {
    this.validatorEngine = new ValidatorEngine<>(validatorClass);
  }
}
