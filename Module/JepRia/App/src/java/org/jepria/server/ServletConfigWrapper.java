package org.jepria.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * Utility wrapper class
 */
public class ServletConfigWrapper implements ServletConfig {

  protected final ServletConfig servletConfig;

  public ServletConfigWrapper(ServletConfig servletConfig) {
    this.servletConfig = servletConfig;
  }


  @Override
  public String getServletName() {
    return servletConfig.getServletName();
  }

  @Override
  public ServletContext getServletContext() {
    return servletConfig.getServletContext();
  }

  @Override
  public String getInitParameter(String name) {
    return servletConfig.getInitParameter(name);
  }

  @Override
  public Enumeration<String> getInitParameterNames() {
    return servletConfig.getInitParameterNames();
  }
}
