package org.jepria.server.service.rest.jersey.validate;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

public class ConstraintViolationImpl<T> implements ConstraintViolation<T> {
  private String message;
  private String messageTemplate;
  private T rootBean;
  private Class<T> rootBeanClass;
  private Object leafBean;
  private Object[] executableParameters;
  private Object executableReturnValue;
  private Path propertyPath;
  private Object invalidValue;
  private ConstraintDescriptor<?> constraintDescriptor;

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String getMessageTemplate() {
    return messageTemplate;
  }

  public void setMessageTemplate(String messageTemplate) {
    this.messageTemplate = messageTemplate;
  }

  @Override
  public T getRootBean() {
    return rootBean;
  }

  public void setRootBean(T rootBean) {
    this.rootBean = rootBean;
  }

  @Override
  public Class<T> getRootBeanClass() {
    return rootBeanClass;
  }

  public void setRootBeanClass(Class<T> rootBeanClass) {
    this.rootBeanClass = rootBeanClass;
  }

  @Override
  public Object getLeafBean() {
    return leafBean;
  }

  public void setLeafBean(Object leafBean) {
    this.leafBean = leafBean;
  }

  @Override
  public Object[] getExecutableParameters() {
    return executableParameters;
  }

  public void setExecutableParameters(Object[] executableParameters) {
    this.executableParameters = executableParameters;
  }

  @Override
  public Object getExecutableReturnValue() {
    return executableReturnValue;
  }

  public void setExecutableReturnValue(Object executableReturnValue) {
    this.executableReturnValue = executableReturnValue;
  }

  @Override
  public Path getPropertyPath() {
    return propertyPath;
  }

  public void setPropertyPath(Path propertyPath) {
    this.propertyPath = propertyPath;
  }

  @Override
  public Object getInvalidValue() {
    return invalidValue;
  }

  public void setInvalidValue(Object invalidValue) {
    this.invalidValue = invalidValue;
  }

  @Override
  public ConstraintDescriptor<?> getConstraintDescriptor() {
    return constraintDescriptor;
  }

  public void setConstraintDescriptor(ConstraintDescriptor<?> constraintDescriptor) {
    this.constraintDescriptor = constraintDescriptor;
  }

  @Override
  public <U> U unwrap(Class<U> aClass) {
    throw new UnsupportedOperationException();
  }
}
