package com.technology.jep.jepriashowcase.feature.shared.record;
 
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DESCRIPTION;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_ID;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME_EN;

import java.util.HashMap;
import java.util.Map;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.lob.JepLobRecordDefinition;

@SuppressWarnings("serial")
public class FeatureRecordDefinition extends JepLobRecordDefinition {
 
  public static FeatureRecordDefinition instance = new FeatureRecordDefinition();
 
  private FeatureRecordDefinition() {
    super(buildTypeMap()
      , new String[]{FEATURE_ID}
      , "v_jrs_feature_lob"
      , buildFieldMap()
    );
    setLikeMap(new HashMap<String, JepLikeEnum>(){{
      put(FEATURE_NAME, JepLikeEnum.CONTAINS);
      put(FEATURE_NAME_EN, JepLikeEnum.CONTAINS);
    }});
  }
 
  private static Map<String, JepTypeEnum> buildTypeMap() {
    return new HashMap<String, JepTypeEnum>() {{
      put(FEATURE_ID, JepTypeEnum.INTEGER);
      put(FEATURE_NAME, JepTypeEnum.STRING);
      put(FEATURE_NAME_EN, JepTypeEnum.STRING);
      put(DESCRIPTION, JepTypeEnum.CLOB);
    }};
  }
  
  private static Map<String, String> buildFieldMap() {
    return new HashMap<String, String>() {{
      put(FEATURE_ID, FEATURE_ID);
      put(DESCRIPTION, DESCRIPTION);
    }};
  }
}
