package org.jepria.server.env;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/*package*/abstract class PropertyAccessObject {

  /*package*/static class Property {
    public String value;
    public String source;
  }

  /**
   * @param name
   * @return property value from this property source
   */
  protected abstract String getPropertyValue(String name);

  protected abstract String location();

  public static final String PROPERTY_NAME__APP_CONF_FILE = "app-conf.file";

  /**
   * Main method for external invocation
   * @param context
   * @param name
   * @return
   */
  public Property lookupProperty(String context, String name) {


    { // 1) context#name: get property by context#name
      if (context != null) {
        String contextName = getContextName(context, name);
        String value = getPropertyValue(contextName);
        if (value != null) {
          Property property = new Property();
          property.value = value;
          property.source = "[" + location() + "]:[" + contextName + "]";
          return property;
        }
      }
    }


    { // 2) context#app-conf.file -> name: get property by name from the external conf file denoted by context#app-conf.file property value
      if (context != null) {
        String confFilePropName = getContextName(context, PROPERTY_NAME__APP_CONF_FILE);
        String confFilePropValue = getPropertyValue(confFilePropName);
        
        // initialize the external conf file denoted by context#app-conf.file property value, if any
        final Properties contextConfFileProps = initPropsByConfFileProperty(confFilePropValue);


        if (contextConfFileProps != null) {
          String value = contextConfFileProps.getProperty(name);
          if (value != null) {
            Property property = new Property();
            property.value = value;
            property.source = "[" + location() + "]:[" + confFilePropName + "]->"
                + "[" + confFilePropValue + "]:[" + name + "]";
            return property;
          }
        }
      }
    }


    // initialize the external conf file denoted by app-conf.file property value, if any
    String confFilePropValue = getPropertyValue("app-conf.file");
    final Properties confFileProps = initPropsByConfFileProperty(confFilePropValue);


    { // 3) app-conf.file -> context#name: get property by context#name from the external conf file denoted by app-conf.file property value
      if (context != null) {
        if (confFileProps != null) {
          String contextName = getContextName(context, name);
          String value = confFileProps.getProperty(contextName);
          if (value != null) {
            Property property = new Property();
            property.value = value;
            property.source = "[" + location() + "]:[" + PROPERTY_NAME__APP_CONF_FILE + "]->"
                + "[" + confFilePropValue + "]:[" + contextName + "]";
            return property;
          }
        }
      }
    }



    { // 4) name: get property by name
      String value = getPropertyValue(name);
      if (value != null) {
        Property property = new Property();
        property.value = value;
        property.source = "[" + location() + "]:[" + name + "]";
        return property;
      }
    }


    { // 5) app-conf.file -> name: get property by name from the external conf file denoted by app-conf.file property value
      if (confFileProps != null) {
        String value = confFileProps.getProperty(name);
        if (value != null) {
          Property property = new Property();
          property.value = value;
          property.source = "[" + location() + "]:[" + "app-conf.file" + "]->"
              + "[" + confFilePropValue + "]:[" + name + "]";
          return property;
        }
      }
    }


    return null;
  }

  /**
   * 
   * @param context
   * @param name
   * @return {@code null} if the context is {@code null}
   */
  protected String getContextName(String context, String name) {
    if (context == null) {
      return null;
    } else {
      context = context.replaceAll("/", "#"); // slash to hash
      return context + (context.endsWith("#") ? "" : "#") + name;
    }
  }

  protected Properties initPropsByConfFileProperty(String confFilePropertyValue) {
    if (confFilePropertyValue == null) {
      return null;
    } else {
      try (InputStream in = Files.newInputStream(Paths.get(confFilePropertyValue))) {
        Properties properties = new Properties();
        properties.load(in);
        return properties;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
