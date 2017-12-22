package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.ui.main;
 
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}ClientConstant.*;

import java.util.ArrayList;

import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}MainClientFactoryImpl;

import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.main.MainModulePresenter;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

public class ${moduleName}MainModulePresenter
    extends MainModulePresenter<${moduleName}MainViewImpl, MainEventBus, JepMainServiceAsync, ${moduleName}MainClientFactoryImpl> {
  
  @SuppressWarnings("serial")
  public ${moduleName}MainModulePresenter(${moduleName}MainClientFactoryImpl clientFactory) {
    super(clientFactory);
    <#list forms![] as form>
      <#if form.moduleRoleNames?has_content>
    addModuleProtection(${form.formName?upper_case}_MODULE_ID, new ArrayList<String>() {{
     <#list form.moduleRoleNames![] as roleName>
     add("${roleName}");
     </#list>
    }});
      </#if>
    </#list>
  }
}
