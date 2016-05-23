package com.technology.jep.jepriashowcase.feature.shared.record;
 
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_ID;

import java.util.HashMap;
import java.util.Map;

import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 
public class FeatureRecordDefinition extends JepRecordDefinition {
 
	public static FeatureRecordDefinition instance = new FeatureRecordDefinition();
 
	private FeatureRecordDefinition() {
		super(buildTypeMap()
			, new String[]{FEATURE_ID}
		);
	}
 
	private static Map<String, JepTypeEnum> buildTypeMap() {
		Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
		return typeMap;
	}
}
