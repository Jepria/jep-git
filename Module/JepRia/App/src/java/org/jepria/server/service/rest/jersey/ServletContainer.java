package org.jepria.server.service.rest.jersey;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.glassfish.jersey.server.ServerProperties;
import org.jepria.server.service.rest.StandardResourceDescription;

public class ServletContainer extends org.glassfish.jersey.servlet.ServletContainer {

  private static final long serialVersionUID = 5499009928437165425L;

  
  
  // TODO move into separate file?
  
  // init-param names
  public static final String STANDARD_RESOURCE_DESCRIPTION_CLASSNAME = "org.jepria.server.service.rest.jersey.config.StandardResourceDescriptionClassname";
  
  // request attribute constants
  public static final String STANDARD_RESOURCE_DESCRIPTION_REQUEST_ATTR = "org.jepria.server.service.rest.jersey.config.StandardResourceDescriptionClassname";
  
  //
  
  
  
  private StandardResourceDescription standardResourceDescription;
  
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setAttribute(STANDARD_RESOURCE_DESCRIPTION_REQUEST_ATTR, standardResourceDescription);
    super.service(req, resp);
  }
  
  @Override
  public void init(ServletConfig config) throws ServletException {
    
    Map<String, String> initParameters = new HashMap<>();
    Enumeration<String> initParameterNames = config.getInitParameterNames();
    while (initParameterNames.hasMoreElements()) {
      String initParameterName = initParameterNames.nextElement();
      initParameters.put(initParameterName, config.getInitParameter(initParameterName));
    }
    
    
    // init resourceDescription
    String standardRecourceDescriptionClassname = initParameters.remove(STANDARD_RESOURCE_DESCRIPTION_CLASSNAME);
    if (standardRecourceDescriptionClassname == null) {
      // TODO no recourceDescriptionClassname init param 
    } else {
      standardResourceDescription = instantiateStandardResourceDescription(standardRecourceDescriptionClassname);
      
      // add standard resource provider (only if standardResourceDescription is defined in init-params)
      addInitParam(initParameters, ServerProperties.PROVIDER_CLASSNAMES, org.jepria.server.service.rest.jaxrs.JaxrsStandardResourceEndpoint.class.getCanonicalName());
    }
      
    
    // add swagger
    addInitParam(initParameters, ServerProperties.PROVIDER_PACKAGES, "io.swagger.jaxrs.listing");
    
    
    final ServletConfig newConfig = new ServletConfig() {
      @Override
      public String getServletName() {
        return config.getServletName();
      }
      @Override
      public ServletContext getServletContext() {
        return config.getServletContext();
      }
      @Override
      public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameters.keySet());
      }
      @Override
      public String getInitParameter(String arg0) {
        return initParameters.get(arg0);
      }
    };
    
    super.init(newConfig);
  }
  
  /**
   * Adds one more init parameter value to the map with comma-separated values. If the value already exists in the map, does nothing  
   * @param initParams
   * @param initParamName
   * @param initParamAddValue
   */
  private static void addInitParam(Map<String, String> initParams, String initParamName, String initParamAddValue) {
    if (initParams == null || initParamName == null || initParamAddValue == null) {
      return;
    }
    
    String initParamValue = initParams.get(initParamName);
    if (initParamValue == null || initParamValue.length() == 0) {
      initParams.put(initParamName, initParamAddValue);
    } else {
      // check the value already exists
      if (!Arrays.asList(initParamValue.split("\\s*,\\s*")).contains(initParamAddValue)) {
        initParams.put(initParamName, initParamValue + "," + initParamAddValue);
      }
    }
  }
  
  private static StandardResourceDescription instantiateStandardResourceDescription(String resourceDescriptionClassname) {
    
    try {
      Class<StandardResourceDescription> resourceDescriptionClass = (Class<StandardResourceDescription>)Class.forName(resourceDescriptionClassname);
      StandardResourceDescription resourceDescription = resourceDescriptionClass.newInstance();
      return resourceDescription;
      
    } catch (Throwable e) {
      // TODO
      throw new RuntimeException(e);
    }
  }
}