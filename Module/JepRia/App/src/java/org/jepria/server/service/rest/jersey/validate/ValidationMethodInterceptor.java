package org.jepria.server.service.rest.jersey.validate;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

public class ValidationMethodInterceptor implements MethodInterceptor {
  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    // collect violations for all arguments in a single set
    Set<ConstraintViolation<?>> violations = new HashSet<>();

    for (Object argument : methodInvocation.getArguments()) {
      Set<ConstraintViolation<Object>> violationsForArgument = validator.validate(argument);
      violations.addAll(violationsForArgument);
    }

    if (violations.isEmpty()) {
      // data is valid
      return methodInvocation.proceed();

    } else {
      // data is invalid
      throw new ValidationException(violations);
    }

  }
}