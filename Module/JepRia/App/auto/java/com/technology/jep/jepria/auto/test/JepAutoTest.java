package com.technology.jep.jepria.auto.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;
import com.technology.jep.jepria.auto.exceptions.AutomationException;
import com.technology.jep.jepria.auto.manager.AutomationManager;
import com.technology.jep.jepria.auto.model.User;
import com.technology.jep.jepria.auto.model.dao.UserData;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

/**
 * TODO: преобразовывать класс (создать отдельную структуру, которая будет поверх текущего функционала) 
 * для тестирования приложения в целом, а не отдельных модулей.
 */
@SuppressWarnings("serial")
public abstract class JepAutoTest<C extends JepRiaModuleAuto> extends AssertJUnit {
  private static Logger logger = Logger.getLogger(JepAutoTest.class.getName());

  protected AutomationManager automationManager;
  
  protected C cut;

  abstract protected void createTestRecord(String keyFieldValue);

  /**
   * Пользователи, которые были созданы и использовались во время тестирования. <br/>
   * TODO: перенести в класс, тестирующий приложение. 
   */
  protected Map<String, User> users = new HashMap<String, User>();
  
  protected abstract AutomationManager getAutomationManager(
      String baseUrl,
      String browserName,
      String browserVersion,
      String browserPlatform,
      String browserPath,
      String driverPath,
      String jepriaVersion,
      String username,
      String password);
  
  protected abstract C getCut();
  
  /**
   * Конфигурирование теста
   * 
   * @param baseUrl - URL запуска приложения
   * @param browserName - используемый браузер
   * @param browserVersion - версия браузера
   * @param browserPlatform - платформа, для которой реализован браузер
   * @param jepriaVersion - версия JepRia
   * @param forceNewBrowser - условие запуска нового браузера: если true - запускать 
   * @param forceLogin - условие перелогинивания: если true - перелогиниваться
   * @param username - имя пользователя
   * @param password - пароль пользователя
   */
  @Parameters({
    "baseUrl",
    "browserName",
    "browserVersion",
    "browserPlatform",
    "browserPath",
    "driverPath",
    "jepriaVersion",
    "forceNewBrowser",
    "forceLogin",
    "username",
    "password"})
//  @BeforeMethod(groups = "all") не работает для отдельно взятых групп, входящих в "all"
  @BeforeMethod(groups = {"find", "create", "delete", "edit", "goto", "list", "setAndGetFields", "fieldStates"})
  public void setUp(
      String baseUrl,
      String browserName,
      @Optional("fake") String browserVersion,
      @Optional("fake") String browserPlatform,
      String browserPath,
      @Optional String driverPath,
      String jepriaVersion,
      @Optional("No") String forceNewBrowser,
      @Optional("No") String forceLogin,
      String username,
      String password) {
    
    automationManager = startAutomationManager(automationManager, baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, forceNewBrowser, forceLogin, username, password);
    cut = getCut();
    if("Yes".equalsIgnoreCase(forceLogin) || !cut.isLoggedIn()) {
      cut.login(username, password);
    }
  }
  
  /**
   * Действия после окончания тестового метода
   * 
   * @param forceNewBrowser - условие запуска нового браузера: если true - запускать 
   * @param forceLogin - условие перелогинивания: если true - перелогиниваться
   */
  @AfterMethod(groups = {"find", "create", "delete", "edit", "goto", "list", "setAndGetFields", "fieldStates"})
  @Parameters({
    "forceNewBrowser",
    "forceLogin"})
  public void tearDown(
      @Optional("No") String forceNewBrowser,
      @Optional("No") String forceLogin) {

      if("Yes".equalsIgnoreCase(forceNewBrowser)) {
          automationManager.stop();
      } else {
          if("Yes".equalsIgnoreCase(forceLogin) && cut.isLoggedIn()) {
            cut.logout();
          } else {
            
//            TODO: По окончанию теста проверять, что приложение находится в рабочем состоянии (не висит сообщение (алерт) об ошибке)
//            Иначе новый тест не запуститься, возвращать в работоспособное состояние (Переход по ссылке baseUrl?).
//            Считывать сообщение и логировать (выбросить исключение?) 
//            На данный момент, во избежание подобного, приходится перезапускать браузер для каждого нового теста, 
//            используя forceNewBrowser
            cut.find(); // Приведение модуля в исходное состояние
          }
      }
  }

  protected void sleep(int msc) {
    try {
      Thread.sleep(msc);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Тест установки/чтения поля детальной формы в состоянии редактирования
   * 
   * @param cut - тестируемый класс (Class Under Test)
   * @param keyFieldId - ID Web-элемента ключевого поля
   * @param keyFieldValue - значение ключевого поля
   * @param testFieldId - ID Web-элемента тестируемого поля 
   * @param testFieldNewValue - устанавливаемое значение тестируемого поля
   */
  protected void testSetAndGetTextFieldValueOnEdit(
      JepRiaModuleAuto cut,
      final String keyFieldId,
      final String keyFieldValue,
      String testFieldId,
      String testFieldNewValue, 
      boolean shouldBeEqual) {
    
    try {
      createTestRecord(keyFieldValue);
      
      cut.edit(new HashMap<String, String>(){{
        put(keyFieldId, keyFieldValue);
      }});
      
      cut.setFieldValue(testFieldId, testFieldNewValue);
      
      if(shouldBeEqual) {
        assertTrue(testFieldNewValue.equals(cut.getFieldValue(testFieldId)));
      } else {
        assertFalse(testFieldNewValue.equals(cut.getFieldValue(testFieldId)));
      }
    } catch(Throwable th) {
      logger.error("testSetAndGetTextFieldValueOnEdit error", th);
    } finally {
      deleteTestRecord(keyFieldId, keyFieldValue);
    }
  }

  /**
   * Тест установки/чтения поля ComboBox детальной формы в состоянии редактирования
   * 
   * @param cut - тестируемый класс (Class Under Test)
   * @param keyFieldId - ID Web-элемента ключевого поля
   * @param keyFieldValue - значение ключевого поля
   * @param testFieldId - ID Web-элемента тестируемого поля 
   * @param testFieldNewValue - устанавливаемое значение тестируемого поля
   */
  protected void testSetAndGetComboBoxFieldValueOnEdit(
      JepRiaModuleAuto cut,
      final String keyFieldId,
      final String keyFieldValue,
      String testFieldId,
      String testFieldNewValue) {
    
    try {
      createTestRecord(keyFieldValue);
      
      cut.edit(new HashMap<String, String>(){{
        put(keyFieldId, keyFieldValue);
      }});
      
      cut.selectComboBoxMenuItem(testFieldId, testFieldNewValue);

      assertEquals(testFieldNewValue, cut.getFieldValue(testFieldId));
    } finally {
      deleteTestRecord(keyFieldId, keyFieldValue);
    }
  }
  
  protected void deleteTestRecord(final String testFieldKey, final String testFieldValue) {
    Map<String, String> testRecordKey = new HashMap<String, String>() {{put(testFieldKey, testFieldValue);}};
    cut.delete(testRecordKey);
  }
  
  protected AutomationManager startAutomationManager(
      AutomationManager automationManager,
      String baseUrl,
      String browserName,
      String browserVersion,
      String browserPlatform,
      String browserPath,
      String driverPath,
      String jepriaVersion,
      String forceNewBrowser,
      String forceLogin,
      String username,
      String password) {
    
    if(automationManager == null || "Yes".equalsIgnoreCase(forceNewBrowser)) {
      automationManager = getAutomationManager(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password);
    }
    
    if(!automationManager.isStarted()) {
      automationManager.start(baseUrl);
    }
    
    return automationManager;
  }
  
  protected void setWorkstate(WorkstateEnum workstate) {
    cut.setWorkstate(workstate);
  }
  
  /**
   * Получает пользователя для теста.
   * @param dao - DAO, реализующий интерфейс создания пользователя.
   * @param login - Логин.
   * @param rolesNameList - Список ролей.
   * @return Пользователь. {@link com.technology.jep.jepria.auto.model.User}
   * @throws Exception 
   */
  public User getUser(UserData dao, String login, List<String> rolesNameList) {
    
    User user = null;
    
    if(users.containsKey(login)){
      user = users.get(login);
    }else{
      try {
        user = dao.createUser(login, rolesNameList);
      } catch (Exception e) {
        throw new AutomationException("Can't create user with role "+rolesNameList+" for test.", e);
      }
      users.put(login, user);
    }
    
    return user;
  }
}
