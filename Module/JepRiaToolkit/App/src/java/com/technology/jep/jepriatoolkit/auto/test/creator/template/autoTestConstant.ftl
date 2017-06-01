package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.auto;
 
public class ${moduleName}AutoTestConstant {
  
  <#list forms as form>
  /**
   * URL для входа в модуль ${form.formName}.
   */
  public static final String ENTRANCE_URL_${form.formName?upper_case}_MODULE = "${moduleName}.jsp?em=${form.formName}&es=sh";
  </#list>
}
