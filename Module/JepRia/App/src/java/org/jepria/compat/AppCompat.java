package org.jepria.compat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jepria.shared.RecordDefinition;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;

/**
 * Временный класс для обеспечения постепенного перехода со "старой" на "новую" архитектуру Jerpia
 */
@Deprecated
public class AppCompat {
  
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
  
}