package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.dao;
 
import com.technology.jep.jepria.server.dao.JepDataStandard;
<#if form.hasOptionField>
import java.util.List;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
</#if>
 
public interface ${form.formName} extends JepDataStandard {
	<#list form.fields as field><#t>
	<#if field.isOptionField>
	List<JepOption> get${field.fieldIdAsParameter}() throws ApplicationException;
	</#if>
	</#list>
}
