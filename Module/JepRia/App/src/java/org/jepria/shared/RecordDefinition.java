package org.jepria.shared;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;

public interface RecordDefinition {
  Set<String> getFieldNames();
  JepTypeEnum getFieldType(String fieldName);
  JepLikeEnum getFieldMatch(String fieldName);
  /**
   * 
   * @return non null
   */
  List<String> getPrimaryKey();
  Comparator<Object> getFieldComparator(String fieldName);
  
  default Map<String, Object> buildPrimaryKeyMap(Map<String, Object> values) {
    if (values != null) {
      return values.keySet().stream().filter(k -> getPrimaryKey().contains(k)).collect(Collectors.toMap(k -> k, v -> v));
    } else {
      return new HashMap<>();
    }
  }
}
