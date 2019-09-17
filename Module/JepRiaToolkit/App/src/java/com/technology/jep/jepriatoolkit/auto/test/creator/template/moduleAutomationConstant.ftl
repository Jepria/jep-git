package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client;
 
public class ${form.formName?cap_first}AutomationConstant {
  
  <#if form.sortDetailFormFields?has_content>
  /**
   * Константы для автоматического тестирования
   */
    <#list form.sortDetailFormFields as field>
  public static final String ${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD = "${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD";
    </#list>
  public static final String ${form.formName?upper_case}_MAXROWCOUNT_FIELD = "${form.formName?upper_case}_MAXROWCOUNT_FIELD";
  </#if>
  <#if form.sortListFormFields?has_content>
  public static final String ${form.formName?upper_case}_GRID_ID = "${form.formName?upper_case}_GRID_ID";
  </#if>
}
