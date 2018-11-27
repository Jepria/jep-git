package org.jepria.compat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jepria.shared.RecordDefinition;

import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.field.JepFieldNames;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.DefaultComparator;

/**
 * Временный класс для поддержания обратной совместимости "новой" и "старой" архитектур Jerpia.
 * Предназначен для использования только в системном коде
 */
@Deprecated
public class CoreCompat {
  
  public static RecordDefinition convertRecordDefinition(JepRecordDefinition jepRecordDefinition) {
    if (jepRecordDefinition == null) {
      return null;
    }
    
    return new RecordDefinition() {
      
      @Override
      public List<String> getPrimaryKey() {
        return Arrays.asList(jepRecordDefinition.getPrimaryKey());
      }
      
      @Override
      public JepTypeEnum getFieldType(String fieldName) {
        return jepRecordDefinition.getType(fieldName);
      }
      
      @Override
      public Set<String> getFieldNames() {
        Set<String> fields = new HashSet<>();
        fields.addAll(jepRecordDefinition.getTypeMap().keySet());
        fields.addAll(jepRecordDefinition.getLikeMap().keySet());
        fields.addAll(getPrimaryKey());
        return fields;
      }
      
      @Override
      public JepLikeEnum getFieldMatch(String fieldName) {
        return jepRecordDefinition.getLikeMap().get(fieldName);
      }
      
      @Override
      public Comparator<Object> getFieldComparator(String fieldName) {
        return null;
      }
    };
  }
  
  /**
   * Convert List(Map(String, Object)) to List(JepRecord)
   */
  public static List<Map<String, ?>> convertList(List<JepRecord> list) {
    if (list == null) {
      return null;
    }
    
    return list.stream().map(rec -> (Map<String, ?>)rec).collect(Collectors.toList());
  }
  
  public static Comparator<Object> getDefaultComparator() {
    return DefaultComparator.instance;
  }
  
  public static final int DEFAULT_MAX_ROW_COUNT = JepRiaConstant.DEFAULT_MAX_ROW_COUNT;
  public static final String MAX_ROW_COUNT__FIELD_NAME = JepFieldNames.MAX_ROW_COUNT;
  
  public static final String OPTION_NAME_KEY = JepOption.OPTION_NAME;
  public static final String OPTION_VALUE_KEY = JepOption.OPTION_VALUE;
  
}
