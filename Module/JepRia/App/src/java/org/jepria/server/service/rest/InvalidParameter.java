package org.jepria.server.service.rest;

public class InvalidParameter {
  /**
   * Type of the parameter (e.g. query, path or body)
   */
  public final Object type;
  /**
   * Parameter name or path within a JSON tree
   */
  public final String name;
  public final Object invalidValue;
  public final String message;
  
  public InvalidParameter(Object type, String name, Object invalidValue, String message) {
    this.type = type;
    this.name = name;
    this.invalidValue = invalidValue;
    this.message = message;
  }
}