package org.jepria.shared;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;

public interface RecordDefinition {
  List<String> getPrimaryKey();
  Map<String, JepTypeEnum> getFieldTypes();
  Map<String, JepLikeEnum> getFieldMatch();
  int getMaxResultsetSize();
  Map<String, Comparator<Object>> getFieldComparators();
}
