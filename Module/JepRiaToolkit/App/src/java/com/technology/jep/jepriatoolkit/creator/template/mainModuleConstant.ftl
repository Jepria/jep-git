package com.technology.${packageName?lower_case}.${moduleName?lower_case}.main.client;

import com.google.gwt.core.client.GWT;
import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.main.shared.text.${moduleName}Text;
 
public class ${moduleName}ClientConstant extends JepRiaConstant {
 
   <#list forms as form>
  public static final String ${form.formName?upper_case}_MODULE_ID = "${form.formName}";
  </#list>
  
  public static ${moduleName}Text ${moduleName?uncap_first}Text = (${moduleName}Text) GWT.create(${moduleName}Text.class);
  
  <#list forms as form>
  public static final String URL_${form.formName?upper_case}_MODULE = "/${moduleName}/${moduleName}.jsp?em=${form.formName}&es=sh";
  </#list>
}
