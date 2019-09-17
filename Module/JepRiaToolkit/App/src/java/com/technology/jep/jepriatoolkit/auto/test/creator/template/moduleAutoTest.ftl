package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.auto;
 
import static com.technology.jep.jepria.client.ui.WorkstateEnum.*;

import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}AutomationConstant.${form.formName?upper_case}_GRID_ID;
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}ClientConstant.${form.formName?uncap_first}Text;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.technology.jep.test.util.DataProviderArguments;
import com.technology.jep.test.util.JepFileDataProvider;
import com.technology.jep.jepria.auto.module.JepRiaModuleAuto.SaveResultEnum;

import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.auto.${moduleName}AutoTest;

/**
 * Тесты модуля ${form.formName}.
 */
<#-- TODO: Генерация стандартный тестов add/edit/find/delete -->
public class ${form.formName}AutoTest extends ${moduleName}AutoTest { 
  
  @SuppressWarnings("unused")
  private static Logger logger = Logger.getLogger(${form.formName}AutoTest.class.getName());
  
  String primaryKey = "";
  
  @Override
  protected void beforeTestLaunch() {
    super.beforeTestLaunch();
    //Вход в модуль. Фактически переход по ссылке, указанной в ModuleDescription.
    //Устанавливает cut соответствующий этому модулю.
    enterModule(${form.formName?uncap_first});
    //Логин по умолчанию (указаны в test.propertiest).
    if(!isLoggedIn()) loginDefault();
  }
  
  /**
   * Тестовый метод.
   */
  @DataProviderArguments("filePath=test/resources/${packagePrefix?lower_case}/${packageName?lower_case}/${moduleName?lower_case}/${form.formName?lower_case}/auto/form.create.data")
  @Test(dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", description = "Стандартный сценарий: Создание записи.")
  public void create(<#rt>
<#assign first = true>
<#list form.fields as field>
<#if field.isPrimaryKey>
<#assign pk=field.fieldIdAsParameter>
<#assign pkgetter="get"+field.fieldIdAsParameter?cap_first+"()">
</#if>
<#if field.isCreateParameter>
<#switch field.fieldWidget>
<#case "JepCheckBoxField">
<#assign varType = "Boolean">
<#break>
<#case "JepTreeField">
<#case "JepListField">
<#case "JepDualListField">
<#assign varType = "String[]">
<#break>
<#default>
<#assign varType = "String">
</#switch>
<#if first><#assign first = false><#else>, </#if>${varType} var${field.fieldIdAsParameter?cap_first}<#rt>
</#if>
</#list>
) {
  <#assign cutname = form.formName?lower_case + "Cut">
  
  ${form.formName?cap_first}Auto ${cutname} = this.<${form.formName?cap_first}Auto>getCut();
  ${cutname}.setWorkstate(CREATE);
  
  <#list form.sortDetailFormFields as field>
  <#if field.isCreateParameter>      
  	${cutname}.set${field.fieldIdAsParameter?cap_first}(var${field.fieldIdAsParameter?cap_first});
  </#if>
	</#list>

	// Проверяем, что поля заполненны именно теми значениями, которыми мы их заполнили
	<#list form.sortDetailFormFields as field>
	<#if field.isCreateParameter>
    assertEquals(var${field.fieldIdAsParameter?cap_first}, ${cutname}.get${field.fieldIdAsParameter?cap_first}());
  </#if>
  </#list>
  
    // Сохраняем запись
    SaveResultEnum saveResult = ${cutname}.save();
    assertEquals(SaveResultEnum.SUCCESS, saveResult);
    
    // Проверяем, что осуществился переход на форму просмотра после создания
    assertEquals(VIEW_DETAILS, ${cutname}.getWorkstateFromStatusBar());
    primaryKey = ${cutname}.${pkgetter};
	// Проверяем, что поля созданной записи имеют такие же значения, как и те, которыми мы их заполнили
	<#list form.sortDetailFormFields as field>
	<#if field.isCreateParameter>
    assertEquals(var${field.fieldIdAsParameter?cap_first}, ${cutname}.get${field.fieldIdAsParameter?cap_first}());
  </#if>
  </#list>
  }
  
  /**
   * Тестовый метод.
   */
  @DataProviderArguments("filePath=test/resources/${packagePrefix?lower_case}/${packageName?lower_case}/${moduleName?lower_case}/${form.formName?lower_case}/auto/form.search.data")
  @Test(dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", description = "Стандартный сценарий: Поиск записи.")
  public void find(<#rt>
<#assign first = true>
<#list form.fields as field>
<#if field.isFindParameter>
<#switch field.fieldWidget>
<#case "JepCheckBoxField">
<#assign varType = "Boolean">
<#break>
<#case "JepTreeField">
<#case "JepListField">
<#case "JepDualListField">
<#assign varType = "String[]">
<#break>
<#default>
<#assign varType = "String">
</#switch>
<#if first><#assign first = false><#else>, </#if>${varType} var${field.fieldIdAsParameter?cap_first}<#rt>
</#if>
</#list>
) {

<#assign cutname = form.formName?lower_case + "Cut">
  ${form.formName?cap_first}Auto ${cutname} = this.<${form.formName?cap_first}Auto>getCut();
  ${cutname}.find();
  
  // Заполняем форму поиска
  <#list form.sortDetailFormFields as field>
  <#if field.isFindParameter>      
  	${cutname}.set${field.fieldIdAsParameter?cap_first}(var${field.fieldIdAsParameter?cap_first});
  </#if>
	</#list>

	// Проверяем, что поля заполненны именно теми значениями, которыми мы их заполнили
	<#list form.sortDetailFormFields as field>
	<#if field.isFindParameter>
    assertEquals(var${field.fieldIdAsParameter?cap_first}, ${cutname}.get${field.fieldIdAsParameter?cap_first}());
  </#if>
  </#list>
  
  ${cutname}.doSearch();
  // Проверяем, что данные в списке имеют значения, соответствующие поисковому шаблону
  // В данном случае, проверяем только столбцы полей текстового типа, участвующие в поисковой форме.
    List<String> headers = ${cutname}.getGridHeaders(${form.formName?upper_case}_GRID_ID);
    List<List<Object>> table = ${cutname}.getGridDataRowwise(${form.formName?upper_case}_GRID_ID);
  
    Map<Integer, String> fieldmap = new HashMap<Integer, String>();
  <#list form.sortListFormFields as listfield>
  <#if listfield.isFindParameter && listfield.isStringType>
  	fieldmap.put(headers.indexOf(${form.formName?uncap_first}Text.${form.formName?uncap_first}_list_${listfield.fieldId?lower_case}()), ${cutname}.get${listfield.fieldIdAsParameter?cap_first}());
  </#if>
  </#list>
  
    for (List<Object> row: table) {
    	for(Integer i : fieldmap.keySet()) {
      	assertTrue(((String)row.get(i)).contains(fieldmap.get(i)));
      }
    }
 }
 
 /**
   * Тестовый метод.
   */
  @DataProviderArguments("filePath=test/resources/${packagePrefix?lower_case}/${packageName?lower_case}/${moduleName?lower_case}/${form.formName?lower_case}/auto/form.delete.data")
  @Test(dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", description = "Стандартный сценарий: Удаление записи.")
 public void delete() {
 }
 
 /**
   * Тестовый метод.
   */
 @DataProviderArguments("filePath=test/resources/${packagePrefix?lower_case}/${packageName?lower_case}/${moduleName?lower_case}/${form.formName?lower_case}/auto/form.edit.data")
  @Test(dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", description = "Стандартный сценарий: Редактирование записи.")
  public void edit(<#rt>
<#assign first = true>
<#list form.fields as field>
<#if field.isUpdateParameter>
<#switch field.fieldWidget>
<#case "JepCheckBoxField">
<#assign varType = "Boolean">
<#break>
<#case "JepTreeField">
<#case "JepListField">
<#case "JepDualListField">
<#assign varType = "String[]">
<#break>
<#default>
<#assign varType = "String">
</#switch>
<#if first><#assign first = false><#else>, </#if>${varType} var${field.fieldIdAsParameter?cap_first}<#rt>
</#if>
</#list>
) {
<#assign cutname = form.formName?lower_case + "Cut">
	${form.formName?cap_first}Auto ${cutname} = this.<${form.formName?cap_first}Auto>getCut();
	${cutname}.setWorkstate(EDIT);

<#list form.sortDetailFormFields as field>
<#if field.isUpdateParameter>      
 	${cutname}.set${field.fieldIdAsParameter?cap_first}(var${field.fieldIdAsParameter?cap_first});
</#if>
</#list>

// Проверяем, что поля заполненны именно теми значениями, которыми мы их заполнили
	<#list form.sortDetailFormFields as field>
	<#if field.isUpdateParameter>
    assertEquals(var${field.fieldIdAsParameter?cap_first}, ${cutname}.get${field.fieldIdAsParameter?cap_first}());
  </#if>
  </#list>

// Сохраняем записи
    SaveResultEnum saveResult = ${cutname}.save();
    assertEquals(SaveResultEnum.SUCCESS, saveResult);
    
    // Проверяем, что осуществился переход на форму просмотра после редактирования
    assertEquals(VIEW_DETAILS, ${cutname}.getWorkstateFromStatusBar());
    
    // Проверяем, что поля созданной записи имеют такие же значения, как и те, которыми мы их заполнили
	<#list form.sortDetailFormFields as field>
	<#if field.isUpdateParameter>
    assertEquals(var${field.fieldIdAsParameter?cap_first}, ${cutname}.get${field.fieldIdAsParameter?cap_first}());
  </#if>
  </#list>
    
  }
}
