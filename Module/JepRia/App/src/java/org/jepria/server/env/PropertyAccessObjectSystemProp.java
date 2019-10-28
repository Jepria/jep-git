package org.jepria.server.env;

/*package*/class PropertyAccessObjectSystemProp extends PropertyAccessObject {
  
  @Override
  protected String location() {
    return "System or JVM properties / java.lang.System.getProperty";
  }
  
  @Override
  protected String getPropertyValue(String name) {
    return System.getProperty(name);
  }
}
