package org.jepria;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * Map, позволяющий получать типизированные значения. 
 * 
 * @param <K>
 * @param <V>
 */
// CastMap типизирован для максимального приближения к Map (хотя достаточно объявить interface CastMap extends Map<String, Object>)
// и для обеспечения возможности объявлять CastMap<K, ?> (что не эквивалентно CastMap<K, Object>) 
public interface CastMap<K, V> extends Map<K, V> {
  Integer getInteger(Object key);
  String getString(Object key);
  BigDecimal getBigDecimal(Object key);
  Number getNumber(Object key);
  
  public static <K, V> CastMap<K, V> from(Map<K, V> map) {
    Objects.requireNonNull(map);
    return new WrapperCastMap<>(map);
  }
}
