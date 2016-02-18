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
<#assign classField = classField + "\tprivate static NumberFormat defaultDecimalFormatter = NumberFormat.getFormat(\"#0.00\");\n\tprivate static NumberFormat defaultNumberFormatter = NumberFormat.getFormat(\"#\");">
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
<#assign classField = classField + "\tprivate static DateTimeFormat defaultDateFormatter = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);\n">
</#if>
</#if>
<#if field.isTimeType>
<#if !import?contains("com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT")>
<#assign import = import + "com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT;">
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT;
</#if>
<#if !classField?contains("defaultTimeFormatter")>
<#assign classField = classField + "\tprivate static DateTimeFormat defaultTimeFormatter = DateTimeFormat.getFormat(DEFAULT_TIME_FORMAT);\n">
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
<#assign classField = classField + "\tprivate static DateTimeFormat defaultDateTimeFormatter = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT + \" \" + DEFAULT_TIME_FORMAT);\n">
</#if>
</#if>
</#if>
</#list>
import com.google.gwt.user.client.ui.HeaderPanel;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.client.ui.form.list.ListFormViewImpl;
import java.util.List;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.client.widget.list.JepGrid;
import com.technology.jep.jepria.client.widget.list.GridManager;
import com.technology.jep.jepria.client.widget.toolbar.PagingStandardBar;
import java.util.ArrayList;

public class ${form.formName}ListFormViewImpl extends ListFormViewImpl<GridManager> { 
	
 	public ${form.formName}ListFormViewImpl() {
		super(new GridManager());
		
		HeaderPanel gridPanel = new HeaderPanel();
		setWidget(gridPanel);
		gridPanel.setHeight("100%");
		gridPanel.setWidth("100%");
		
		JepGrid<JepRecord> grid = new JepGrid<JepRecord>(getClass().getCanonicalName(), getColumnConfigurations());
		PagingStandardBar pagingBar = new PagingStandardBar(25);
		
		gridPanel.setContentWidget(grid);
		gridPanel.setFooterWidget(pagingBar);
 
		list.setWidget(grid);
		list.setPagingToolBar(pagingBar);
	}
	<#if classField??><#t>
${classField}
	</#if>
	private static List<JepColumn> getColumnConfigurations() {
		final List<JepColumn> columns = new ArrayList<JepColumn>();
		<#list form.sortListFormFields as field>
		columns.add(new JepColumn(${field.fieldId?upper_case}, ${form.formName?uncap_first}Text.${form.formName?uncap_first}_list_${field.fieldId?lower_case}(), ${field.columnWidth}<#if field.isDateType>, new DateCell(defaultDateFormatter)<#elseif field.isTimeType>, new DateCell(defaultTimeFormatter)<#elseif field.isDateTimeType>, new DateCell(defaultDateTimeFormatter)<#elseif field.isBooleanType>, new JepCheckBoxCell()<#elseif field.isIntegerType>, new NumberCell(defaultNumberFormatter)<#elseif field.isBigDecimalType>, new NumberCell(defaultDecimalFormatter)</#if>));
		</#list>
		return columns;
	}
}
