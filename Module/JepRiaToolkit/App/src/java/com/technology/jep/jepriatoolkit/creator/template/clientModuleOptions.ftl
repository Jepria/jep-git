package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.field;
 
import com.technology.jep.jepria.shared.field.JepRecordFieldNames;
 
public class ${field.fieldIdAsParameter}Options extends JepRecordFieldNames {
	public static final String ${field.fieldId?upper_case} = "${field.fieldId?lower_case}";
	public static final String ${field.displayValueForComboBox?upper_case} = "${field.displayValueForComboBox?lower_case}";
}
