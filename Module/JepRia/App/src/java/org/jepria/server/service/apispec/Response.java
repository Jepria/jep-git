package org.jepria.server.service.apispec;

public class Response {
  
  public final String description;
  public final Type type;
  
  public Response(String description, Type type) {
    this.description = description;
    this.type = type;
  }
}
