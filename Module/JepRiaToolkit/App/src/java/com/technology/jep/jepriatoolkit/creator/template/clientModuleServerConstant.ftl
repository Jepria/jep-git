package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server;
 
import com.technology.jep.jepria.server.JepRiaServerConstant;
 
public class ${form.formName}ServerConstant extends JepRiaServerConstant {
 
  public static final String RESOURCE_BUNDLE_NAME = "com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.text.${form.formName}Text";
 
  public static final String DATA_SOURCE_JNDI_NAME = "jdbc/${form.dataSource}";
 
}
