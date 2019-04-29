package org.jepria;

public interface TypedValueParser {
  <T> T parse(Object value, Class<T> type) throws TypedValueParseException;
  
  public static class TypedValueParseException extends Exception {
    private static final long serialVersionUID = 1L;
    
    private final Object value;
    private final Class<?> type;
    
    public TypedValueParseException(Object value, Class<?> type) {
      this.value = value;
      this.type = type;
    }

    public Object getValue() {
      return value;
    }

    public Class<?> getType() {
      return type;
    }
    
    @Override
    public String getMessage() {
      return "Cannot cast value [" + value  + "] to " + type;
    }
  }
}
