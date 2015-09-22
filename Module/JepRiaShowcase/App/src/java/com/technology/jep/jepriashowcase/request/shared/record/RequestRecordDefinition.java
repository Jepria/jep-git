package com.technology.jep.jepriashowcase.request.shared.record;
 
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.*;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.*;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 
import java.util.HashMap;
import java.util.Map;
 
public class RequestRecordDefinition extends JepRecordDefinition {
 
	private static final long serialVersionUID = 1L;
 
	public static RequestRecordDefinition instance = new RequestRecordDefinition();
 
	private RequestRecordDefinition() {
		super(buildTypeMap()
			, new String[]{REQUEST_ID}
		);
	}
 
	private static Map<String, JepTypeEnum> buildTypeMap() {
		Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
		typeMap.put(REQUEST_ID, INTEGER);
		typeMap.put(SHOP_ID, INTEGER);
		typeMap.put(SHOP_NAME, STRING);
		typeMap.put(GOODS_ID, INTEGER);
		typeMap.put(GOODS_NAME, STRING);
		typeMap.put(GOODS_QUANTITY, INTEGER);
		typeMap.put(REQUEST_DATE_FROM, DATE);
		typeMap.put(REQUEST_DATE_TO, DATE);
		typeMap.put(REQUEST_DATE, DATE);
		typeMap.put(REQUEST_STATUS_CODE, STRING);
		typeMap.put(REQUEST_STATUS_NAME, STRING);
		return typeMap;
	}
}
