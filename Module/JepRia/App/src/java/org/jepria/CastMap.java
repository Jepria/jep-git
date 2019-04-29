package org.jepria;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Map, позволяющий получать типизированные значения. 
 * 
 * @param <K>
 * @param <V>
 */
// CastMap типизирован для максимального приближения к Map (хотя достаточно объявить interface CastMap extends Map<String, Object>)
// и для обеспечения возможности объявлять CastMap<K, ?> (что не эквивалентно CastMap<K, Object>) 
public interface CastMap<K, V> extends Map<K, V> {
  
  Integer getInteger(Object key) throws CastOnGetException;
  
  String getString(Object key) throws CastOnGetException;
  
  BigDecimal getBigDecimal(Object key) throws CastOnGetException;
  
  public static class CastOnGetException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private final Object key;
    private final Class<?> castTo;
    
    public CastOnGetException(Object key, Class<?> castTo) {
      this.key = key;
      this.castTo = castTo;
    }
    
    public CastOnGetException(Object key, Class<?> castTo, Throwable cause) {
      super(cause);
      this.key = key;
      this.castTo = castTo;
    }
    public Object getKey() {
      return key;
    }
    public Class<?> getCastTo() {
      return castTo;
    }
  } 
}
