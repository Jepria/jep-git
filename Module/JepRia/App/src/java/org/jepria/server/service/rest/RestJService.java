package org.jepria.server.service.rest;

public class RestJService {
  
  protected <T> void validate(T target, Class<? extends Validator<? super T>> validatorClass) {
    new ValidatorEngine<>(validatorClass).validate(target);
  }
  
  protected <T> void validate(T target, Validator<? super T> validator) {
    new ValidatorEngine<>(validator).validate(target);
  }
}
