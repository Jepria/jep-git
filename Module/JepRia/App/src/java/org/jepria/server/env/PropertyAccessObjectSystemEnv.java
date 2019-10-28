package org.jepria.server.env;

/*package*/class PropertyAccessObjectSystemEnv extends PropertyAccessObject {

  @Override
  protected String location() {
    return "System environment properties / java.lang.System.getenv";
  }

  @Override
  protected String getPropertyValue(String name) {
    return System.getenv(name);
  }
}
