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
  <#--TODO: реализовать поддержку всех виджетов (может быть не только String).
  @Override
  public void set${field.fieldIdAsParameter?cap_first}(String ${field.fieldId}) {
    setFieldValue(${form.formName?upper_case}_${field.fieldId?upper_case}_DETAILFORM_FIELD_ID, ${field.fieldId});
  }
  
  @Override
  public String get${field.fieldName?cap_first}() {
    return getFieldValue(${form.formName?upper_case}_${field.fieldId?upper_case}_DETAILFORM_FIELD_ID);
  }
  -->
  </#list>
  <#if !form.isDependent>
  @Override
  public void setMaxRowCount(String maxRowCount) {
    setFieldValue(${form.formName?upper_case}_MAXROWCOUNT_DETAILFORM_FIELD_ID, maxRowCount);
  }
  
  @Override
  public String getMaxRowCount() {
    return getFieldValue(${form.formName?upper_case}_MAXROWCOUNT_DETAILFORM_FIELD_ID);
  }
  </#if>
}
