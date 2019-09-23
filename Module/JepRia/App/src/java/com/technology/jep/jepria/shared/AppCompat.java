package com.technology.jep.jepria.shared;

import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.DefaultComparator;
import org.jepria.server.ServerFactory;
import org.jepria.server.data.RecordDefinition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppCompat {


  public static Comparator<Object> getDefaultComparator() {
    return DefaultComparator.instance;
  }
}
