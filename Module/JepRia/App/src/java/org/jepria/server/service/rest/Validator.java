package org.jepria.server.service.rest;

import java.util.Map;

public interface Validator<T> {
  boolean validate(T value, Context context);
  
  interface Context {
    void invalidParameter(String name, Object invalidValue, String message);
  }
  
  public static final class Void implements Validator<Map<String, Object>> {
    @Override
    public boolean validate(Map<String, Object> value, Validator.Context context) {
      return true;
    }
  }
}
