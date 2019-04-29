package org.jepria;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Обёртка {@link Map}, реализующая интерфейс {@link CastMap} 
 *
 * @param <K>
 * @param <V>
 */
public class WrapperCastMap<K, V> implements CastMap<K, V> {

  protected final Map<K, V> map;

  public Map<K, V> unwrap() {
    return map;
  }

  public WrapperCastMap(Map<K, V> map) {
    this.map = map;
  }


  /////////////////////
  // CastMap methods //
  /////////////////////

  @Override
  public Integer getInteger(Object key) {
    try {
      return (Integer)get(key);
    } catch (ClassCastException e) {
      handleCastException(key, Integer.class, e);

      // generally unreachable
      throw e;
    }
  }

  @Override
  public String getString(Object key) {
    try {
      return (String)get(key);
    } catch (ClassCastException e) {
      handleCastException(key, String.class, e);

      // generally unreachable
      throw e;
    }
  }

  @Override
  public BigDecimal getBigDecimal(Object key) {
    try {
      return (BigDecimal)get(key);
    } catch (ClassCastException e) {
      handleCastException(key, BigDecimal.class, e);

      // generally unreachable
      throw e;
    }
  }

  @Override
  public Number getNumber(Object key) {
    try {
      return (Number)get(key);
    } catch (ClassCastException e) {
      handleCastException(key, Number.class, e);

      // generally unreachable
      throw e;
    }
  }

  protected void handleCastException(Object key, Class<?> castTo, ClassCastException e) {
    throw new CastOnGetException(key, castTo, e);
  }

  //////////////////////////
  // Map delegate methods //
  //////////////////////////

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return map.get(key);
  }

  @Override
  public V put(K key, V value) {
    return map.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    map.putAll(m);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<K> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<V> values() {
    return map.values();
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    return map.entrySet();
  }

  @Override
  public boolean equals(Object o) {
    return map.equals(o);
  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }




}
