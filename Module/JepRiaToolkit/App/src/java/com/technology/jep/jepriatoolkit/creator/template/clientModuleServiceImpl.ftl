package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.service;

import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.${form.formName}ServerConstant.DATA_SOURCE_JNDI_NAME;
<#if form.hasLobFields>
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.${form.formName}ServerConstant.RESOURCE_BUNDLE_NAME;
</#if>

import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.dao.${form.formName};
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.dao.${form.formName}Dao;
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.record.${form.formName}RecordDefinition;
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.service.${form.formName}Service;
import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
<#if form.hasOptionField>
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.dao.${form.formName};
import java.util.List;
</#if>
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
 
@RemoteServiceRelativePath("${form.formName}Service")
public class ${form.formName}ServiceImpl extends JepDataServiceServlet<${form.formName}> implements ${form.formName}Service  {
 
	private static final long serialVersionUID = 1L;
 
	public ${form.formName}ServiceImpl() {
		super(${form.formName}RecordDefinition.instance, new ServerFactory<${form.formName}>(new ${form.formName}Dao(), DATA_SOURCE_JNDI_NAME)<#if form.hasLobFields>, DATA_SOURCE_JNDI_NAME</#if>);
	}
	<#list form.fields as field><#t>
	<#if field.isOptionField>

	@Override
	public List<JepOption> get${field.fieldIdAsParameter}() throws ApplicationException {
		List<JepOption> result = null;
		try {
			result = dao.get${field.fieldIdAsParameter}();
		} catch (Throwable th) {
			throw new ApplicationException(th.getLocalizedMessage(), th);
		}
		return result;
	}
	</#if>
	</#list>
}
