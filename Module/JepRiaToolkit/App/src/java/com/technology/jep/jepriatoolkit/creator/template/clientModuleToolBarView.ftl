package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.toolbar;
 
import com.technology.jep.jepria.client.ui.toolbar.ToolBarView;
 
public interface ${form.formName}ToolBarView extends ToolBarView {
    
    <#list form.toolBarCustomButtons as button>
  final static String ${button.buttonId?upper_case} = "${button.buttonIdAsParameter}";
  </#list> 
   <#list form.toolBarCustomButtons as button>
  <#if button.isSeparator>
  final static String ${button.buttonId?upper_case} = "${button.buttonIdAsParameter}";
  </#if>
   </#list> 
}
