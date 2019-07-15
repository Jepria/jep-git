package org.jepria.server.service.rest;

/**
 * Exception thrown while parsing request entity into a JSON tree
 */
public class JsonParseException extends RuntimeException {

  private static final long serialVersionUID = -4603133863031582870L;

  public JsonParseException(String message) {
    super(message);
  }

  public JsonParseException(Throwable cause) {
    super(cause);
  }

  public JsonParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
