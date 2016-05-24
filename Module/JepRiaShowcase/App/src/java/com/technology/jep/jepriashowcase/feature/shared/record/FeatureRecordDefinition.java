package com.technology.jep.jepriashowcase.feature.shared.record;
 
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.*;

import java.util.HashMap;
import java.util.Map;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 
public class FeatureRecordDefinition extends JepRecordDefinition {
 
	public static FeatureRecordDefinition instance = new FeatureRecordDefinition();
 
	private FeatureRecordDefinition() {
		super(buildTypeMap()
			, new String[]{FEATURE_ID}
		);
		setLikeMap(new HashMap<String, JepLikeEnum>(){{
			put(FEATURE_NAME, JepLikeEnum.CONTAINS);
			put(FEATURE_NAME_EN, JepLikeEnum.CONTAINS);
		}});
	}
 
	private static Map<String, JepTypeEnum> buildTypeMap() {
		Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
		return typeMap;
	}
	
}
