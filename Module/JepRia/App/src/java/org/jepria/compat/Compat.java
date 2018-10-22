package org.jepria.compat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jepria.shared.RecordDefinition;

import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;

/**
 * Временный класс для поддержания обратной совместимости "новой" и "старой" архитектур Jerpia
 */
@Deprecated
public class Compat {
  
  /**
   * Convert Map(String, Object) to JepRecord
   */
  public static JepRecord convertRecord(Map<String, Object> map) {
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
  
  /**
   * Convert JepRecordDefinition to RecordDefiniton
   */
  public static RecordDefinition convertRecordDefinition(JepRecordDefinition jepRecordDefinition) {
    if (jepRecordDefinition == null) {
      return null;
    }
    
    return new RecordDefinition() {
      
      @Override
      public List<String> getPrimaryKey() {
        if (jepRecordDefinition.getPrimaryKey() == null) {
          return null;
        }
        return Arrays.asList(jepRecordDefinition.getPrimaryKey());
      }
      
      @Override
      public int getMaxResultsetSize() {
        return JepRiaConstant.DEFAULT_MAX_ROW_COUNT;
      }
      
      @Override
      public Map<String, JepTypeEnum> getFieldTypes() {
        return jepRecordDefinition.getTypeMap();
      }
      
      @Override
      public Map<String, JepLikeEnum> getFieldMatch() {
        return jepRecordDefinition.getLikeMap();
      }
      
      @Override
      public Map<String, Comparator<Object>> getFieldComparators() {
        return null;
      }
    }; 
  }
}
