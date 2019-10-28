package org.jepria.server.env;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*package*/class PropertyAccessObjectTomcatEnv extends PropertyAccessObject {

  @Override
  protected String location() {
    return "java:comp/env maintained by Tomcat / Context/Environment entries";
  }

  @Override
  protected String getPropertyValue(String name) {
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");
      return (String) envCtx.lookup(name);
    } catch (NamingException e) {
      // TODO fail-fast or fail-safe? what if the user configured the env variable, but the exception occurred?
      return null;
    }
  }

}
