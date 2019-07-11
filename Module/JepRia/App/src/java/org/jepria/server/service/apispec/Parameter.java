package org.jepria.server.service.apispec;

public class Parameter {
  
  public static enum In {
    QUERY,
    PATH,
    BODY;
  }
  
  public final String name;
  public final In in;
  public final boolean required;
  public final Type type;
  
  public Parameter(String name, In in, boolean required, Type type) {
    this.name = name;
    this.in = in;
    this.required = required;
    this.type = type;
  }
}
