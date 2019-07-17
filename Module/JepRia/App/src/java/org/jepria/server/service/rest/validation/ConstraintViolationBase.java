package org.jepria.server.service.rest.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

public class ConstraintViolationBase implements ConstraintViolation<Void> {

  protected final Path path;
  protected final String message;

  public ConstraintViolationBase(Path path, String message) {
    this.path = path;
    this.message = message;
  }

  @Override
  public ConstraintDescriptor<?> getConstraintDescriptor() {
    return null;
  }

  @Override
  public Object[] getExecutableParameters() {
    return null;
  }

  @Override
  public Object getExecutableReturnValue() {
    return null;
  }

  @Override
  public Object getInvalidValue() {
    return null;
  }

  @Override
  public Object getLeafBean() {
    return null;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String getMessageTemplate() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Path getPropertyPath() {
    return path;
  }

  @Override
  public Void getRootBean() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<Void> getRootBeanClass() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <U> U unwrap(Class<U> arg0) {
    // TODO Auto-generated method stub
    return null;
  }

}
