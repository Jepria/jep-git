package com.technology.jep.jepriashowcase.custom.shared.record;
 
import static com.technology.jep.jepria.shared.field.JepTypeEnum.INTEGER;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.STRING;
import static com.technology.jep.jepriashowcase.custom.shared.field.CustomFieldNames.LOGIN;
import static com.technology.jep.jepriashowcase.custom.shared.field.CustomFieldNames.OPERATOR_ID;

import java.util.HashMap;
import java.util.Map;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;

 
public class CustomRecordDefinition extends JepRecordDefinition {
 
  private static final long serialVersionUID = 1L;
 
  public static CustomRecordDefinition instance = new CustomRecordDefinition();
 
  private CustomRecordDefinition() {
    super(buildTypeMap()
      , new String[]{OPERATOR_ID}
    );
    super.setLikeMap(buildLikeMap());
  }
 
  private static Map<String, JepTypeEnum> buildTypeMap() {
    Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
    
    typeMap.put(OPERATOR_ID, INTEGER);
    typeMap.put(LOGIN, STRING);
    
    return typeMap;
  }
 
  private static Map<String, JepLikeEnum> buildLikeMap() {
    Map<String, JepLikeEnum> likeMap = new HashMap<String, JepLikeEnum>();
    
    likeMap.put(LOGIN, JepLikeEnum.CONTAINS);
    
    return likeMap;
  }
}
