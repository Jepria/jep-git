package com.technology.${packageName?lower_case}.${moduleName?lower_case}.auto;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}ClientConstant.*;
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.auto.${moduleName}AutoTestConstant.*;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.auto.model.module.ModuleDescription;
import com.technology.jep.jepria.auto.test.JepRiaApplicationAutoTest;
<#list forms as form>
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.auto.${form.formName}Auto;
</#list>

/**
 * Класс для тестов на уровне приложения. <br />
 * Все основные тесты пишутся в классах-наследниках на уровне модулей приложения. <br/>
 * Примеры-образцы различных тестов: <br/>
 * <ul>
 *  <li>Стандартные (add/edit/delete/find) тесты: /JepRiaShowcase/Trunk/App/test/java/com/technology/jep/jepriashowcase/feature/auto/FeatureAutoTestStandard.java</li>
 *  <li>Демострация использования API тестирования JepRia-виджетов: /JepRiaShowcase/Trunk/App/test/java/com/technology/jep/jepriashowcase/arsenic/auto/ArsenicAutoTest.java</li>
 *  <li>Тест кастомной авторизации: /Navigation/Trunk/App/test/java/com/technology/jep/navigation/navigation/auto/NavigationAutoTest.java</li>
 *  <li>Тест вложенных модулей: /Cms/Trunk/App/test/java/com/technology/communicationsite/cms/auto/site/PagesAutoTest.java</li>
 * </ul>
 */
public class ${moduleName}AutoTest extends JepRiaApplicationAutoTest<${moduleName}AutoImpl> {

  @SuppressWarnings("unused")
  private static Logger logger = Logger.getLogger(${moduleName}AutoTest.class.getName());
  
  <#list forms as form>
  /**
   * Описание модуля ${form.formName}.
   */
  protected ModuleDescription<${form.formName}Auto> ${form.formName?uncap_first} = null;
  </#list>
   
  @Override
  protected ${moduleName}AutoImpl provideAutomationManager(String baseUrl, String browserName, String browserVersion,
      String browserPlatform, String browserPath, String driverPath, String jepriaVersion, String username,
      String password, String dbUrl, String dbUser, String dbPassword) {

    return new ${moduleName}AutoImpl(baseUrl, browserName, browserVersion,
        browserPlatform, browserPath, driverPath, jepriaVersion, username, password, dbUrl, dbUser, dbPassword);
  }
  
  @Override
  protected void beforeTestLaunch() {
    <#list forms as form>
    if(${form.formName?uncap_first} == null) {
      ${form.formName?uncap_first} = new ModuleDescription<${form.formName}Auto>(${form.formName?upper_case}_MODULE_ID, 
          ENTRANCE_URL_${form.formName?upper_case}_MODULE,
          SEARCH,
          applicationAuto.get${form.formName}Auto(true));
    }
    </#list>
  }
}
