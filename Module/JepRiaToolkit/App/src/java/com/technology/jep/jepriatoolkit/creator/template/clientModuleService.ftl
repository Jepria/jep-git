package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.service;
 
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.service.data.JepDataService;
<#if form.hasOptionField>

import java.util.List;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
</#if> 

@RemoteServiceRelativePath("${form.formName}Service")
public interface ${form.formName}Service extends JepDataService {
  <#list form.fields as field><#t>
  <#if field.isOptionField>
  List<JepOption> get${field.fieldIdAsParameter}() throws ApplicationException;
  </#if>
  </#list>
}
