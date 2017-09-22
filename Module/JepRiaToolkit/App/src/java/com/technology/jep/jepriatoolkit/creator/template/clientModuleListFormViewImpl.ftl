package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.form.list;
 
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.field.${form.formName}FieldNames.*;
<#assign import = "">
<#list form.sortListFormFields as field>
<#if field_index == 0>
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}ClientConstant.${form.formName?uncap_first}Text;
</#if>
<#if field.isDateType || field.isTimeType || field.isDateTimeType || field.isBooleanType || field.isIntegerType || field.isBigDecimalType>
<#if !import?contains("com.technology.jep.jepria.shared.field.JepTypeEnum;")>
<#assign import = import + "com.technology.jep.jepria.shared.field.JepTypeEnum;">
import com.technology.jep.jepria.shared.field.JepTypeEnum;
</#if>
</#if>
</#list>

import java.util.ArrayList;
import java.util.List;

import com.technology.jep.jepria.client.ui.form.list.StandardListFormViewImpl;
import com.technology.jep.jepria.client.widget.list.JepColumn;

public class ${form.formName}ListFormViewImpl extends StandardListFormViewImpl { 
  
  public ${form.formName}ListFormViewImpl() {
    super(${form.formName}ListFormViewImpl.class.getCanonicalName());
  }
  
  @Override
  protected List<JepColumn> getColumnConfigurations() {
    return new ArrayList<JepColumn>() {{
      <#list form.sortListFormFields as field>
      add(new JepColumn(${field.fieldId?upper_case}, ${form.formName?uncap_first}Text.${form.formName?uncap_first}_list_${field.fieldId?lower_case}(), ${field.columnWidth}<#if field.isDateType>, JepTypeEnum.DATE<#elseif field.isTimeType>, JepTypeEnum.TIME<#elseif field.isDateTimeType>, JepTypeEnum.DATETIME<#elseif field.isBooleanType>, JepTypeEnum.BOOLEAN<#elseif field.isIntegerType>, JepTypeEnum.INTEGER<#elseif field.isBigDecimalType>, JepTypeEnum.BIGDECIMAL</#if>));
      </#list>
    }};
  }
}
