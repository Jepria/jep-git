package org.jepria.server.service.rest.jersey;

import org.jepria.server.ServletConfigWrapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * A JerseyJaxrsConfig servlet that supports context-relative api basepath under {@code swagger.api.basepath.relative} init-param name
 */
public class JerseyJaxrsConfig extends io.swagger.jersey.config.JerseyJaxrsConfig {

  @Override
  public void init(ServletConfig servletConfig) throws ServletException {

    final ServletConfig servletConfigDelegate = new ServletConfigWrapper(servletConfig) {
      @Override
      public String getInitParameter(String name) {
        if ("swagger.api.basepath".equals(name)) {
          String basePath = servletConfig.getInitParameter(name);
          if (basePath != null) {
            // return explicit parameter
            return basePath;
          } else {
            String basePathRelative = servletConfig.getInitParameter("swagger.api.basepath.relative");
            if (basePathRelative == null) {
              // absent
              return null;
            } else {
              // convert relative to absolute
              return servletConfig.getServletContext().getContextPath() + basePathRelative;
            }
          }
        } else {
          return super.getInitParameter(name);
        }
      }
    };

    super.init(servletConfigDelegate);
  }
}