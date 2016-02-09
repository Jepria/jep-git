package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.service;
 
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
<#if form.hasOptionField>

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.field.option.JepOption;
</#if>
 
public interface ${form.formName}ServiceAsync extends JepDataServiceAsync {
	<#list form.fields as field><#t>
	<#if field.isOptionField>
	void get${field.fieldIdAsParameter}(AsyncCallback<List<JepOption>> callback);
	</#if>
	</#list>
}
