package com.technology.jep.jepriashowcase.arsenic.shared.record;
 
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.PRIMARY_KEY;

import java.util.HashMap;
import java.util.Map;

import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 
public class ArsenicRecordDefinition extends JepRecordDefinition {
 
	public static ArsenicRecordDefinition instance = new ArsenicRecordDefinition();
 
	private ArsenicRecordDefinition() {
		super(buildTypeMap(), new String[]{PRIMARY_KEY});
	}
 
	private static Map<String, JepTypeEnum> buildTypeMap() {
		Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
		typeMap.put(PRIMARY_KEY, JepTypeEnum.INTEGER);
		return typeMap;
	}
}
