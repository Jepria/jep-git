package org.jepria;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * {@link CastMap}-расширение класса {@link HashMap}
 *  
 * @param <K>
 * @param <V>
 */

public class HashCastMap<K, V> extends HashMap<K, V> implements CastMap<K, V> {
  
  private static final long serialVersionUID = 2103627077090163943L;

  /**
   * Абстрактный кастер, который реализует интерфейс {@link CastMap} 
   * через интерфейс {@link java.util.Map} данного класса 
   */
  private final CastMap<K, V> abstractCaster = createAbstractCaster();
  
  protected CastMap<K, V> createAbstractCaster() {
    return new WrapperCastMap<>(this);
  }

  public Integer getInteger(Object key) {
    return abstractCaster.getInteger(key);
  }

  public String getString(Object key) {
    return abstractCaster.getString(key);
  }

  public BigDecimal getBigDecimal(Object key) {
    return abstractCaster.getBigDecimal(key);
  }

  public Number getNumber(Object key) {
    return abstractCaster.getNumber(key);
  }

}
