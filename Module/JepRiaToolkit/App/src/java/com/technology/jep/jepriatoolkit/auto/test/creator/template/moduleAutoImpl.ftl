package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.auto;

import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}AutomationConstant.*;
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}ClientConstant.*;
 
import com.technology.jep.jepria.auto.module.JepRiaModuleAutoImpl;
import com.technology.jep.jepria.auto.module.page.JepRiaModulePage;

/**
 * Реализация автоматизации модуля ${form.formName}.
 */
public class ${form.formName}AutoImpl extends JepRiaModuleAutoImpl<JepRiaModulePage>
    implements ${form.formName}Auto { 
  
  public ${form.formName}AutoImpl() {
    super(new JepRiaModulePage());
  }
  
  <#if form.sortDetailFormFields?has_content>
  /**
   * Заполнение полей и получение их значений.
   */
  </#if>
  <#list form.sortDetailFormFields as field>
    <#switch field.fieldWidget>
      <#case "JepComboBoxField">
      
  @Override
  public void set${field.fieldIdAsParameter?cap_first}(String ${field.fieldIdAsParameter?uncap_first}) {
    selectComboBoxMenuItem(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD, ${field.fieldIdAsParameter?uncap_first});
  }

  @Override
  public String get${field.fieldIdAsParameter?cap_first}() {
    return getFieldValue(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD);
  }
        <#break>
      <#case "JepCheckBoxField">
      
  @Override
  public void set${field.fieldIdAsParameter?cap_first}(Boolean ${field.fieldIdAsParameter?uncap_first}) {
    setCheckBoxFieldValue(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD, ${field.fieldIdAsParameter?uncap_first});
  }

  @Override
  public Boolean get${field.fieldIdAsParameter?cap_first}() {
    return getCheckBoxFieldValue(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD);
  }
        <#break>
      <#case "JepTreeField">
      
  @Override
  public void set${field.fieldIdAsParameter?cap_first}(String[] ${field.fieldIdAsParameter?uncap_first}) {
    selectTreeItems(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD, ${field.fieldIdAsParameter?uncap_first});
  }

  @Override
  public String get${field.fieldIdAsParameter?cap_first}() {
    return getTreeFieldNodesByFilter(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD, TreeItemFilter.FILTER_CHECKED);
  }
        <#break>
      <#case "JepListField">
      
  @Override
  public void set${field.fieldIdAsParameter?cap_first}(String[] ${field.fieldIdAsParameter?uncap_first}) {
    selectListMenuItems(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD, ${field.fieldIdAsParameter?uncap_first});
  }

  @Override
  public String get${field.fieldIdAsParameter?cap_first}() {
    return getListFieldValues(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD);
  }
        <#break>
      <#case "JepDualListField">
      
  @Override
  public void set${field.fieldIdAsParameter?cap_first}(String[] ${field.fieldIdAsParameter?uncap_first}) {
    selectDualListMenuItems(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD, ${field.fieldIdAsParameter?uncap_first});
  }

  @Override
  public String get${field.fieldIdAsParameter?cap_first}() {
    return getDualListFieldValues(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD);
  }
        <#break>
      <#default>
      
  @Override
  public void set${field.fieldIdAsParameter?cap_first}(String ${field.fieldIdAsParameter?uncap_first}) {
    setFieldValue(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD, ${field.fieldIdAsParameter?uncap_first});
  }
  
  @Override
  public String get${field.fieldIdAsParameter?cap_first}() {
    return getFieldValue(${form.formName?upper_case}_${field.fieldId?upper_case}_FIELD);
  }
    </#switch>
  </#list>
  
  <#if !form.isDependent>
  @Override
  public void setMaxRowCount(String maxRowCount) {
    setFieldValue(${form.formName?upper_case}_MAXROWCOUNT_FIELD, maxRowCount);
  }
  
  @Override
  public String getMaxRowCount() {
    return getFieldValue(${form.formName?upper_case}_MAXROWCOUNT_FIELD);
  }
  </#if>
}
