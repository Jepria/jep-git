package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.form.list;
 
import com.google.gwt.place.shared.Place;

<#assign hasCustomButtons = (form.toolBarCustomButtonsOnListForm?size != 0)>
<#if hasCustomButtons>
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.event.shared.EventBus;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus.${form.formName}EventBus;
<#else>
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
</#if>
<#if form.isDblClickOff>
import com.technology.jep.jepria.client.widget.event.JepEvent; 
</#if>
<#assign ClientFactoryClassName = form.formName + "ClientFactoryImpl">
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${ClientFactoryClassName};
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.service.${form.formName}ServiceAsync;
import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;
import com.technology.jep.jepria.client.ui.form.list.ListFormView;
<#list form.toolBarCustomButtonsOnListForm as button>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus.event.${button.customEvent}Event;
</#list>

public class ${form.formName}ListFormPresenter
  extends ListFormPresenter<${form.formName}ListFormViewImpl, <#if hasCustomButtons>${form.formName}<#else>Plain</#if>EventBus, ${form.formName}ServiceAsync, ${ClientFactoryClassName}><#if !hasCustomButtons> {</#if>
    <#if hasCustomButtons>implements <#list form.toolBarCustomButtonsOnListForm as button>${button.customEvent}Event.Handler<#if button_has_next>, </#if></#list> {</#if> 
  
  <#if hasCustomButtons>
  @Override
  public void start(AcceptsOneWidget container, EventBus eventBus) {
    super.start(container, eventBus);
    <#list form.toolBarCustomButtonsOnListForm as button>
    eventBus.addHandler(${button.customEvent}Event.TYPE, this);
    </#list>
  }
  </#if> 
   
  public ${form.formName}ListFormPresenter(Place place, ${ClientFactoryClassName} clientFactory) {
    super(place, clientFactory);
  }
  <#if form.isDblClickOff>
  
  @Override
  public void onRowDoubleClick(JepEvent event) {}
  </#if>
   <#list form.toolBarCustomButtonsOnListForm as button>
   
  public void on${button.customEvent}Event(${button.customEvent}Event event) { 
    //TODO: your business logic; 
  }
  </#list>
}
