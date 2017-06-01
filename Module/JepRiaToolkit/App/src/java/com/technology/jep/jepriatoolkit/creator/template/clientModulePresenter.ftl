package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.plain;

import com.google.gwt.place.shared.Place;

import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus.${form.formName}EventBus;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.service.${form.formName}ServiceAsync;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.ui.plain.StandardModulePresenter;
import com.technology.jep.jepria.client.ui.plain.StandardModuleView;
<#assign hasCustomButtons = (form.toolBarCustomButtonsOnBothForms?size != 0)>
<#if hasCustomButtons>
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.event.shared.EventBus;
<#list form.toolBarCustomButtonsOnBothForms as button>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus.event.${button.customEvent}Event;
</#list>
</#if>

public class ${form.formName}ModulePresenter<V extends StandardModuleView, E extends ${form.formName}EventBus, S extends ${form.formName}ServiceAsync, F extends StandardClientFactory<E, S>> 
  extends StandardModulePresenter<V, E, S, F><#if !hasCustomButtons> {</#if>
    <#if hasCustomButtons>implements <#list form.toolBarCustomButtonsOnBothForms as button>${button.customEvent}Event.Handler<#if button_has_next>, </#if></#list> {</#if> 
  <#if hasCustomButtons>
   
   @Override
   public void start(AcceptsOneWidget container, EventBus eventBus) {
     super.start(container, eventBus);
     <#list form.toolBarCustomButtonsOnBothForms as button>
     eventBus.addHandler(${button.customEvent}Event.TYPE, this);
     </#list>
   }
   </#if>
   
   public ${form.formName}ModulePresenter(String moduleId, Place place, F clientFactory) {
    super(moduleId, place, clientFactory);
  }
   <#list form.toolBarCustomButtonsOnBothForms as button>
   
  public void on${button.customEvent}Event(${button.customEvent}Event event) { 
    //TODO: your business logic; 
  }
  </#list>
}
