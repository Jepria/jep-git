package org.jepria.server.env;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public interface EnvironmentPropertySupport {

  Object getProperty(String name);

  String getPropertyAsString(String name);

  default Object getProperty(String name, Object defaultValue) {
    Object value = getProperty(name);
    return value == null ? defaultValue : value;
  }

  default String getPropertyAsString(String name, String defaultValue) {
    String value = getPropertyAsString(name);
    return value == null ? defaultValue : value;
  }


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
