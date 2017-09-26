package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.ui.main;
 
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}ClientConstant.*;
import static com.technology.jep.jepria.client.security.ClientSecurity.CHECK_ROLES_BY_OR;

import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}MainClientFactoryImpl;

import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.main.MainModulePresenter;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

public class ${moduleName}MainModulePresenter
  extends MainModulePresenter<${moduleName}MainViewImpl, MainEventBus, JepMainServiceAsync, ${moduleName}MainClientFactoryImpl> {
     
  public ${moduleName}MainModulePresenter(${moduleName}MainClientFactoryImpl clientFactory) {
    super(clientFactory);
    <#list forms![] as form>
    <#if form.moduleRoleNames?has_content>
     addModuleProtection(${form.formName?upper_case}_MODULE_ID, "<#list form.moduleRoleNames![] as roleName>${roleName}<#if roleName_has_next>, </#if></#list>", CHECK_ROLES_BY_OR);
     </#if>
    </#list>
  }
}
