package com.technology.jep.jepriashowcase.requestprocess.shared.record;
 
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.*;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.*;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 
import java.util.HashMap;
import java.util.Map;
 
public class RequestProcessRecordDefinition extends JepRecordDefinition {
 
  private static final long serialVersionUID = 1L;
 
  public static RequestProcessRecordDefinition instance = new RequestProcessRecordDefinition();
 
  private RequestProcessRecordDefinition() {
    super(buildTypeMap()
      , new String[]{REQUEST_PROCESS_ID}
    );
  }
 
  private static Map<String, JepTypeEnum> buildTypeMap() {
    Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
    typeMap.put(REQUEST_PROCESS_ID, INTEGER);
    typeMap.put(REQUEST_ID, INTEGER);
    typeMap.put(PROCESS_COMMENT, STRING);
    typeMap.put(DATE_INS, DATE);
    typeMap.put(OPERATOR_ID, INTEGER);
    typeMap.put(OPERATOR_NAME, STRING);
    return typeMap;
  }
}
