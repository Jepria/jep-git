package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.auto;

<#list forms as form>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.auto.${form.formName}Auto;
</#list>

public interface ${moduleName}Auto {
  <#list forms as form>
  /**
   * Класс автоматизации модуля ${form.formName}.
   */
  ${form.formName}Auto get${form.formName}Auto(boolean newInstance);
  </#list>
}
