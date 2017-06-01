package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client;

import com.google.gwt.core.client.GWT;
import com.technology.jep.jepria.shared.JepRiaConstant;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.shared.text.${moduleName}Text;
 
public class ${moduleName}ClientConstant extends JepRiaConstant {
 
   <#list forms as form>
  public static final String ${form.formName?upper_case}_MODULE_ID = "${form.formName}";
  </#list>
  
  public static ${moduleName}Text ${moduleName?uncap_first}Text = (${moduleName}Text) GWT.create(${moduleName}Text.class);
}
