package org.jepria;

import java.math.BigDecimal;

public class TypedValueParserImpl implements TypedValueParser {
  @SuppressWarnings("unchecked")
  @Override
  public <T> T parse(Object value, Class<T> type) throws TypedValueParseException {
    if (value == null) {
      return null;
    }
    if (type == Integer.class) {
      if (value instanceof String) {
        // parse
        try {
          return (T)(Integer)Integer.parseInt((String)value);
        } catch (NumberFormatException e) {
          throw new TypedValueParseException(value, type);
        }
        
      } else if (value instanceof Integer) {
        // cast
        return (T)(Integer)value;
        
      } else if (value instanceof Number) {
        // convert
        int valueInt = ((Number)value).intValue();
        double valueDbl = ((Number)value).doubleValue();
        // TODO add tolerance for double, e.g. 10.00000000000000000001 == 10 ?
        if (valueInt == valueDbl) {
          return (T)(Integer)valueInt;
        }
      }
      
    } else if (type == String.class) {
      if (value instanceof String) {
        // cast
        return (T)(String)value;
      } else {
        // convert
        return (T)(String)String.valueOf(value);
      }
      
    } else if (type == BigDecimal.class) {
      if (value instanceof String) {
        // parse
        try {
          return (T)new BigDecimal((String)value);
        } catch (NumberFormatException e) {
          throw new TypedValueParseException(value, type);
        }
        
      } else if (value instanceof BigDecimal) {
        // cast
        return (T)(BigDecimal)value;
        
      } else if (value instanceof Number) {
        // convert
        try {
          return (T)BigDecimal.valueOf(((Number)value).doubleValue());
        } catch (NumberFormatException e) {
          throw new TypedValueParseException(value, type);
        }
      }
      
    }
    
    throw new TypedValueParseException(value, type);
  }
}

