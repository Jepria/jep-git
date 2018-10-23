package org.jepria.shared;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public interface RecordDefinition extends FieldsetDefinition {
  List<String> getPrimaryKey();
  int getMaxResultsetSize();
  Map<String, Comparator<Object>> getFieldComparators();
}
