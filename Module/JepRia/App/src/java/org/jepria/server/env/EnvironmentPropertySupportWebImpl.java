package org.jepria.server.env;

import org.jepria.server.env.PropertyAccessObject.Property;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvironmentPropertySupportWebImpl implements EnvironmentPropertySupport {

  protected final ServletContext servletContext;
  protected final String context;
  
  public EnvironmentPropertySupportWebImpl(ServletContext servletContext) {
    this.servletContext = servletContext;

    String context = servletContext.getContextPath();
    if (context != null && context.startsWith("/")) {
      context = context.substring(1);
    }
    this.context = context;
  }
  
  @Override
  public String getProperty(String name) {
    Property property = getPropertyInternal(name);
    return property == null ? null : property.value; 
  }
  
  @Override
  public String getPropertySource(String name) {
    Property property = getPropertyInternal(name);
    return property == null ? null : property.source; 
  }
  
  protected Property getPropertyInternal(String name) {

    Property property;
    
    
    property = getPropertyContext(name);
    if (property != null) {
      return property;
    }
    

    property = getPropertySystemProp(name);
    if (property != null) {
      return property;
    }
    
    
    property = getPropertySystemEnv(name);
    if (property != null) {
      return property;
    }
    
    
    property = getPropertyDefault(name);
    if (property != null) {
      return property;
    }
    

    return null;
  }
  
  protected Property getPropertyContext(String name) {
    return new PropertyAccessObjectTomcatEnv().lookupProperty(context, name);
  }
  
  protected Property getPropertySystemProp(String name) {
    return new PropertyAccessObjectSystemProp().lookupProperty(context, name);
  }
  
  protected Property getPropertySystemEnv(String name) {
    return new PropertyAccessObjectSystemEnv().lookupProperty(context, name);
  }

  public static final String WEB_PATH__APP_CONF_DEFAULT_FILE = "/WEB-INF/app-conf.default.properties";

  protected Property getPropertyDefault(String name) {
    try (InputStream in = servletContext.getResourceAsStream(WEB_PATH__APP_CONF_DEFAULT_FILE)) {
      // InputStream is null if no such resource exists, and the try-with-resources is null-safe
      if (in == null) {
        return null;
      } else {
        Properties propertiesConfFile = new Properties();
        propertiesConfFile.load(in);
        String value = propertiesConfFile.getProperty(name);
        if (value != null) {
          Property property = new Property();
          property.value = value;
          property.source = "Web-context:" + servletContext.getContextPath() + WEB_PATH__APP_CONF_DEFAULT_FILE + ":" + name;
          return property;
        } else {
          return null;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
