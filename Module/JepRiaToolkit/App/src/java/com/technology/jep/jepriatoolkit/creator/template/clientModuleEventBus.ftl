package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus;
 
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
<#list form.toolBarCustomButtons as button>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus.event.${button.customEvent}Event;
</#list>
 
public class ${form.formName}EventBus extends PlainEventBus {
 
  public ${form.formName}EventBus() {
    super();
  }
   <#list form.toolBarCustomButtons as button>
   
  public void ${button.event} { 
    fireEvent(new ${button.customEvent}Event());
  }
   </#list>
}
