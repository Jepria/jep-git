package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.auto;
 
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
    <#switch field.fieldWidget>
      <#case "JepComboBoxField">
  public void set${field.fieldIdAsParameter?cap_first}(String ${field.fieldIdAsParameter?uncap_first});
  public String get${field.fieldIdAsParameter?cap_first}();
        <#break>
      <#case "JepCheckBoxField">
  public void set${field.fieldIdAsParameter?cap_first}(Boolean ${field.fieldIdAsParameter?uncap_first});
  public Boolean get${field.fieldIdAsParameter?cap_first}();
        <#break>
      <#case "JepTreeField">
  public void set${field.fieldIdAsParameter?cap_first}(String[] ${field.fieldIdAsParameter?uncap_first});
  public String get${field.fieldIdAsParameter?cap_first}();
        <#break>
      <#case "JepListField">
  public void set${field.fieldIdAsParameter?cap_first}(String[] ${field.fieldIdAsParameter?uncap_first});e
  public String get${field.fieldIdAsParameter?cap_first}();
        <#break>
      <#case "JepDualListField">
  public void set${field.fieldIdAsParameter?cap_first}(String[] ${field.fieldIdAsParameter?uncap_first});
  public String get${field.fieldIdAsParameter?cap_first}();
        <#break>
      <#default>    
  public void set${field.fieldIdAsParameter?cap_first}(String ${field.fieldIdAsParameter?uncap_first});
  public String get${field.fieldIdAsParameter?cap_first}();
    </#switch>
  </#list>
  <#if !form.isDependent>
  void setMaxRowCount(String maxRowCount);
  String getMaxRowCount();
  </#if>
}
