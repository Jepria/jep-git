package org.jepria.validator.core.base.resource;

import java.io.InputStream;

/**
 * Общий предок всех ресурсов, для разделения их по типам в наследниках  
 */
public class AbstractResource implements PlainResource {
  
  private final PlainResource delegate;
  
  public AbstractResource(PlainResource resource) {
    this.delegate = resource;
  }
  
  @Override
  public String getName() {
    return delegate.getName();
  }
  
  @Override
  public InputStream newInputStream() {
    return delegate.newInputStream();
  }
  
  @Override
  public String getPathName() {
    return delegate.getPathName();
  }
  
  /**
   * @return ResourceClassName[pathName]
   */
  @Override
  public String toString() {
    return this.getClass().getSimpleName()+"["+getPathName()+"]";
  }
}
