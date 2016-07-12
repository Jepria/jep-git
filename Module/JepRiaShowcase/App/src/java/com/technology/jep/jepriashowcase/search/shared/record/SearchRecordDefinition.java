package com.technology.jep.jepriashowcase.search.shared.record;
 
import static com.technology.jep.jepria.shared.field.JepTypeEnum.INTEGER;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.STRING;
import static com.technology.jep.jepriashowcase.search.shared.field.SearchFieldNames.*;

import java.util.HashMap;
import java.util.Map;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;

 
public class SearchRecordDefinition extends JepRecordDefinition {
 
  private static final long serialVersionUID = 1L;
 
  public static SearchRecordDefinition instance = new SearchRecordDefinition();
 
  private SearchRecordDefinition() {
    super(buildTypeMap()
      , new String[]{OPERATOR_ID}
    );
    super.setLikeMap(buildLikeMap());
  }
 
  private static Map<String, JepTypeEnum> buildTypeMap() {
    Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
    
    typeMap.put(OPERATOR_ID, INTEGER);
    typeMap.put(LOGIN, STRING);
    typeMap.put(GOODS_ID, INTEGER);
    typeMap.put(GOODS_NAME, STRING);
    
    return typeMap;
  }
 
  private static Map<String, JepLikeEnum> buildLikeMap() {
    Map<String, JepLikeEnum> likeMap = new HashMap<String, JepLikeEnum>();
    
    likeMap.put(LOGIN, JepLikeEnum.CONTAINS);
    likeMap.put(GOODS_NAME, JepLikeEnum.CONTAINS);
    
    return likeMap;
  }
}
