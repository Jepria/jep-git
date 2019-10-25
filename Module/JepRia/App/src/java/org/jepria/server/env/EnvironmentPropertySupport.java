package org.jepria.server.env;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public interface EnvironmentPropertySupport {

  Object getProperty(String name);

  /**
   * @param request
   * @return property support for web applications
   */
  static EnvironmentPropertySupport getInstance(HttpServletRequest request) {
    return new EnvironmentPropertySupportServletContextImpl(request.getServletContext());
  }

  static EnvironmentPropertySupport getInstance(ServletContext context) {
    return new EnvironmentPropertySupportServletContextImpl(context);
  }

}
