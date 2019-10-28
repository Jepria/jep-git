package org.jepria.server.env;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

// Тестирование: см. https://sourceforge.net/p/javaenterpriseplatform/wiki/EnvironmentPropertySupport:%20%D1%82%D0%B5%D1%81%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5/
public interface EnvironmentPropertySupport {
  
  String getProperty(String name);
  
  default String getProperty(String name, String defaultValue) {
    String value = getProperty(name);
    return value == null ? defaultValue : value;
  }
  
  /**
   * Describes where the property has been actually retrieved from 
   * (due to the multiple property access object strategy).
   * Consists of property location and actual property name 
   * (the actual property name in the storage denoted by the location 
   * may differ from the name it was requested, regarding contexts)  
   * Used for debug purposes (represented by a human-readable string)
   */
  String getPropertySource(String name);

  /**
   * @param request
   * @return property support for web applications
   */
  static EnvironmentPropertySupport getInstance(HttpServletRequest request) {
    return new EnvironmentPropertySupportWebImpl(request.getServletContext());
  }

  static EnvironmentPropertySupport getInstance(ServletContext context) {
    return new EnvironmentPropertySupportWebImpl(context);
  }
}
