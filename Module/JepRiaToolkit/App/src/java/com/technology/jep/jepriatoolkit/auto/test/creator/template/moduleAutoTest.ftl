package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.auto;
 
import static com.technology.jep.jepria.client.ui.WorkstateEnum.*;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.technology.${packageName?lower_case}.${moduleName?lower_case}.auto.${moduleName}AutoTest;

/**
 * Тесты модуля ${form.formName}.
 */
<#-- TODO: Генерация стандартный тестов add/edit/find/delete -->
public class ${form.formName}AutoTest extends ${moduleName}AutoTest { 
  
  @SuppressWarnings("unused")
  private static Logger logger = Logger.getLogger(${form.formName}AutoTest.class.getName());
  
  @Override
  protected void beforeTestLaunch() {
    super.beforeTestLaunch();
    //Вход в модуль. Фактически переход по ссылке, указанной в ModuleDescription.
    enterModule(${form.formName?uncap_first});
    //Логин по умолчанию (указаны в test.propertiest).
    if(!isLoggedIn()) loginDefault();
  }
  
  /**
   * Тестовый метод.
   */
  @Test(description = "Описание")
  public void test(){}
}
