package com.technology.jep.jepriashowcase.supplier.shared.record;
 
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.*;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.*;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 
import java.util.HashMap;
import java.util.Map;
 
public class SupplierRecordDefinition extends JepRecordDefinition {
 
	private static final long serialVersionUID = 1L;
 
	public static SupplierRecordDefinition instance = new SupplierRecordDefinition();
 
	private SupplierRecordDefinition() {
		super(buildTypeMap()
			, new String[]{SUPPLIER_ID}
		);
	}
 
	private static Map<String, JepTypeEnum> buildTypeMap() {
		Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
		typeMap.put(SUPPLIER_ID, INTEGER);
		typeMap.put(SUPPLIER_NAME, STRING);
		typeMap.put(CONTRACT_FINISH_DATE, DATE);
		typeMap.put(CONTRACT_FINISH_DATE_FROM, DATE);
		typeMap.put(CONTRACT_FINISH_DATE_TO, DATE);
		typeMap.put(EXCLUSIVE_SUPPLIER_FLAG, BOOLEAN);
		typeMap.put(EXCLUSIVE_SUPPLIER_OPTION, OPTION);
		typeMap.put(SUPPLIER_DESCRIPTION, STRING);
		typeMap.put(PHONE_NUMBER, STRING);
		typeMap.put(FAX_NUMBER, STRING);
		typeMap.put(BANK_BIC, STRING);
		typeMap.put(BANKNAME, STRING);
		typeMap.put(KS, STRING);
		typeMap.put(RECIPIENT_NAME, STRING);
		typeMap.put(SETTLEMENT_ACCOUNT, STRING);
		return typeMap;
	}
}
