package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.auto;
 
import com.technology.jep.jepria.auto.module.JepRiaModuleAuto;

/**
 * Автоматизация модуля ${form.formName}.
 */
public interface ${form.formName}Auto extends JepRiaModuleAuto { 
  
  <#if form.sortDetailFormFields?has_content>
  /**
   * Заполнение полей и получение их значений.
   */
  </#if>
  <#list form.sortDetailFormFields as field>
  <#--TODO: реализовать поддержку всех виджетов (может быть не только String).
  void set${field.fieldIdAsParameter?cap_first}(String ${field.fieldId});
  String get${field.fieldName?cap_first}();
  -->
  </#list>
  <#if !form.isDependent>
  void setMaxRowCount(String maxRowCount);
  String getMaxRowCount();
  </#if>
}
