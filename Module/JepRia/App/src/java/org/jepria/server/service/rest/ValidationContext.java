package org.jepria.server.service.rest;

// the type might be parametrized with T, but not so important
public interface ValidationContext {
  /**
   * Reports an invalid parameter
   * @param name parameter name
   * @param invalidValue invalid value
   * @param message message to display to the client
   */
  void invalidParameter(String name, Object invalidValue, String message);
}
