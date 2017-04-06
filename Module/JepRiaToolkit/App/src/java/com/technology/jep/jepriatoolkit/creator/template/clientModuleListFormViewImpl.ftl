package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.form.list;
 
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.field.${form.formName}FieldNames.*;
<#assign import = "" classField = "">
<#list form.sortListFormFields as field>
<#if field_index == 0>
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}ClientConstant.${form.formName?uncap_first}Text;
</#if>
<#if field.isIntegerType || field.isBigDecimalType>
<#if !import?contains("com.google.gwt.cell.client.NumberCell")>
<#assign import = import + "com.google.gwt.i18n.client.NumberFormat;">
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
</#if>
<#if !classField?contains("defaultDecimalFormatter")>
<#assign classField = classField + "  private static NumberFormat defaultDecimalFormatter = NumberFormat.getFormat(\"#0.00\");\n  private static NumberFormat defaultNumberFormatter = NumberFormat.getFormat(\"#\");">
</#if>
</#if>
<#if field.isBooleanType>
<#if !import?contains("com.technology.jep.jepria.client.widget.list.cell.JepCheckBoxCell")>
<#assign import = import + "com.technology.jep.jepria.client.widget.list.cell.JepCheckBoxCell;">
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.jepria.client.widget.list.cell.JepCheckBoxCell;
</#if>
</#if>
<#if field.isDateType || field.isTimeType || field.isDateTimeType>
<#if !import?contains("com.google.gwt.i18n.client.DateTimeFormat")>
<#assign import = import + "com.google.gwt.i18n.client.DateTimeFormat;">
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.cell.client.DateCell;
</#if>
<#if field.isDateType>
<#if !import?contains("com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT")>
<#assign import = import + "com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;">
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
</#if>
<#if !classField?contains("defaultDateFormatter")>
<#assign classField = classField + "  private static DateTimeFormat defaultDateFormatter = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);\n">
</#if>
</#if>
<#if field.isTimeType>
<#if !import?contains("com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT")>
<#assign import = import + "com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT;">
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT;
</#if>
<#if !classField?contains("defaultTimeFormatter")>
<#assign classField = classField + "  private static DateTimeFormat defaultTimeFormatter = DateTimeFormat.getFormat(DEFAULT_TIME_FORMAT);\n">
</#if>
</#if>
<#if field.isDateTimeType>
<#if !import?contains("com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT")>
<#assign import = import + "com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;">
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
</#if>
<#if !import?contains("com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT")>
<#assign import = import + "com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT;">
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT;
</#if>
<#if !classField?contains("defaultDateTimeFormatter")>
<#assign classField = classField + "  private static DateTimeFormat defaultDateTimeFormatter = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT + \" \" + DEFAULT_TIME_FORMAT);\n">
</#if>
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
  <#if classField??><#t>
${classField}
  </#if>
  @SuppressWarnings({ "rawtypes", "unchecked", "serial" })
  @Override
  protected List<JepColumn> getColumnConfigurations() {
      return new ArrayList<JepColumn>() {{
        <#list form.sortListFormFields as field>
        add(new JepColumn(${field.fieldId?upper_case}, ${form.formName?uncap_first}Text.${form.formName?uncap_first}_list_${field.fieldId?lower_case}(), ${field.columnWidth}<#if field.isDateType>, new DateCell(defaultDateFormatter)<#elseif field.isTimeType>, new DateCell(defaultTimeFormatter)<#elseif field.isDateTimeType>, new DateCell(defaultDateTimeFormatter)<#elseif field.isBooleanType>, new JepCheckBoxCell()<#elseif field.isIntegerType>, new NumberCell(defaultNumberFormatter)<#elseif field.isBigDecimalType>, new NumberCell(defaultDecimalFormatter)</#if>));
        </#list>
      }};
  }
}
