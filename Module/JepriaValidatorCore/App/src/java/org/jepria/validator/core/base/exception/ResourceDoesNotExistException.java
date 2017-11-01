package org.jepria.validator.core.base.exception;

public class ResourceDoesNotExistException extends RuntimeException {

  private static final long serialVersionUID = -3400026050745618822L;
  
  private final String pathName;

  public ResourceDoesNotExistException(String pathName) {
    super(pathName);
    this.pathName = pathName;
  }

  public String getPathName() {
    return pathName;
  }

}
