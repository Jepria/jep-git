package com.technology.jep.jepriashowcase.featurerequest.shared.record;
 
import static com.technology.jep.jepriashowcase.featurerequest.shared.field.FeatureRequestFieldNames.FEATURE_ID;

import java.util.HashMap;
import java.util.Map;

import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 
public class FeatureRequestRecordDefinition extends JepRecordDefinition {
 
	public static FeatureRequestRecordDefinition instance = new FeatureRequestRecordDefinition();
 
	private FeatureRequestRecordDefinition() {
		super(buildTypeMap()
			, new String[]{FEATURE_ID}
		);
	}
 
	private static Map<String, JepTypeEnum> buildTypeMap() {
		Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
		return typeMap;
	}
}
