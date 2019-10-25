package org.jepria.server.env;

import javax.servlet.ServletContext;

/*package*/class EnvironmentPropertySupportServletContextImpl implements EnvironmentPropertySupport {

  protected final ServletContext context;

  public EnvironmentPropertySupportServletContextImpl(ServletContext context) {
    this.context = context;
  }

  @Override
  public Object getProperty(String name) {
    return getPropertyAsString(name);
  }

  @Override
  public String getPropertyAsString(String name) {
    return context.getInitParameter(name);
  }
}
