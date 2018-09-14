package com.technology.jep.jepria.server.service.rest;

import java.util.Map;

import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Временный класс для поддержания обратной совместимости "новой" и "старой" архитектур Jerpia
 */
@Deprecated
public class Compat {
  
  public static JepRecord mapToRecord(Map<String, Object> map) {
    if (map == null) {
      return null;
    }
    
    JepRecord rec = new JepRecord();
    map.forEach((k, v) -> rec.put(k, v));
    return rec;
  }
}
