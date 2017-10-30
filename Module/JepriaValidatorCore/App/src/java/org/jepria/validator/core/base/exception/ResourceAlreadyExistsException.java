package org.jepria.validator.core.base.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = -5146002322189613024L;
  
  private final String pathName;

  public ResourceAlreadyExistsException(String pathName) {
    super(pathName);
    this.pathName = pathName;
  }

  public String getPathName() {
    return pathName;
  }

}
