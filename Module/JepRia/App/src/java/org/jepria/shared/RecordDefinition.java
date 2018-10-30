package org.jepria.shared;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.jepria.compat.Compat;

import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;

public interface RecordDefinition {
  Set<String> getFieldNames();
  JepTypeEnum getFieldType(String fieldName);
  JepLikeEnum getFieldMatch(String fieldName);
  List<String> getPrimaryKey();
  
  default int getMaxResultsetSize() {
    return JepRiaConstant.DEFAULT_MAX_ROW_COUNT;
  }
  
  Comparator<Object> getFieldComparator(String fieldName);
  
  @Deprecated
  /**
   * converts JepRecordDefinition to RecordDefinition
   * @param jepRecordDefinition
   * @return
   */
  public static RecordDefinition convert(JepRecordDefinition jepRecordDefinition) {
    return Compat.convertRecordDefinition(jepRecordDefinition);
  }
}
