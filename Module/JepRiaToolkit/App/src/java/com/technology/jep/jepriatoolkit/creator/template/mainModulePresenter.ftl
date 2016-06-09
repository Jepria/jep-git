package com.technology.${packageName?lower_case}.${moduleName?lower_case}.main.client.ui.main;
 
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}ClientConstant.*;
import static com.technology.jep.jepria.client.security.ClientSecurity.CHECK_ROLES_BY_OR;
 
import com.technology.jep.jepria.client.ui.main.MainView;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactory;
import com.technology.jep.jepria.client.ui.main.MainModulePresenter;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

public class ${moduleName}MainModulePresenter<E extends MainEventBus, S extends JepMainServiceAsync> 
	extends MainModulePresenter<MainView, E, S, MainClientFactory<E, S>> {
		 
	public ${moduleName}MainModulePresenter(MainClientFactory<E, S> clientFactory) {
		super(clientFactory);
		<#list forms![] as form>
		<#if form.moduleRoleNames![]?has_content>
 		addModuleProtection(${form.formName?upper_case}_MODULE_ID, "<#list form.moduleRoleNames![] as roleName>${roleName}<#if roleName_has_next>, </#if></#list>", CHECK_ROLES_BY_OR);
 		</#if>
		</#list>
	}
}
