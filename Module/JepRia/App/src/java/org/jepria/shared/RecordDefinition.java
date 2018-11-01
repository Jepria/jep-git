package org.jepria.shared;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;

public interface RecordDefinition {
  Set<String> getFieldNames();
  JepTypeEnum getFieldType(String fieldName);
  JepLikeEnum getFieldMatch(String fieldName);
  List<String> getPrimaryKey();
  Comparator<Object> getFieldComparator(String fieldName);
}
