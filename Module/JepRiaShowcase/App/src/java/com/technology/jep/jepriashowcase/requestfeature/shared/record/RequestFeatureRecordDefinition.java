package com.technology.jep.jepriashowcase.requestfeature.shared.record;
 
import static com.technology.jep.jepriashowcase.requestfeature.shared.field.RequestFeatureFieldNames.FEATURE_ID;

import java.util.HashMap;
import java.util.Map;

import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 
public class RequestFeatureRecordDefinition extends JepRecordDefinition {
 
	public static RequestFeatureRecordDefinition instance = new RequestFeatureRecordDefinition();
 
	private RequestFeatureRecordDefinition() {
		super(buildTypeMap()
			, new String[]{FEATURE_ID}
		);
	}
 
	private static Map<String, JepTypeEnum> buildTypeMap() {
		Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
		return typeMap;
	}
}
