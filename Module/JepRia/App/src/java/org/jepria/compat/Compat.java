package org.jepria.compat;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.field.JepFieldNames;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.DefaultComparator;

/**
 * Временный класс для поддержания обратной совместимости "новой" и "старой" архитектур Jerpia
 */
@Deprecated
public class Compat {
  
  /**
   * Convert Map(String, Object) to JepRecord
   */
  private static JepRecord convertRecord(Map<String, Object> map) {
    if (map == null) {
      return null;
    }
    
    JepRecord rec = new JepRecord();
    map.forEach((k, v) -> rec.put(k, v));
    return rec;
  }
  
  /**
   * Convert List(Map(String, Object)) to List(JepRecord)
   */
  public static List<Map<String, Object>> convertList(List<JepRecord> list) {
    if (list == null) {
      return null;
    }
    
    return list.stream().map(rec -> (Map<String, Object>)rec).collect(Collectors.toList());
  }
  
  public static Comparator<Object> getDefaultComparator() {
    return DefaultComparator.instance;
  }
  
  public static final int DEFAULT_MAX_ROW_COUNT = JepRiaConstant.DEFAULT_MAX_ROW_COUNT;
  public static final String MAX_ROW_COUNT__FIELD_NAME = JepFieldNames.MAX_ROW_COUNT;
}
