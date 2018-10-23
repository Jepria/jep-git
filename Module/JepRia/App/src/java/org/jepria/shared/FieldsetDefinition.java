package org.jepria.shared;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;

public interface FieldsetDefinition {
  Set<String> getFieldNames();
  JepTypeEnum getFieldType(String fieldName);
  JepLikeEnum getFieldMatch(String fieldName);
}
