package com.technology.jep.jepria.auto.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;
import com.technology.jep.jepria.auto.manager.JepRiaAuto;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

@SuppressWarnings("serial")
public abstract class JepAutoTest<C extends JepRiaModuleAuto> extends AssertJUnit {
	private static Logger logger = Logger.getLogger(JepAutoTest.class.getName());

	protected JepRiaAuto automationManager;
	
	protected C cut;

	abstract protected void createTestRecord(String keyFieldValue);

	protected abstract JepRiaAuto getAutomationManager(
			String baseUrl,
			String browserName,
			String browserVersion,
			String browserPlatform,
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
		"jepriaVersion",
		"forceNewBrowser",
		"forceLogin",
		"username",
		"password"})
//	@BeforeMethod(groups = "all") не работает для отдельно взятых групп, входящих в "all"
	@BeforeMethod(groups = {"find", "create", "delete", "edit", "goto", "list", "setAndGetTextField"})
	public void setUp(
			String baseUrl,
			String browserName,
			@Optional("fake") String browserVersion,
			@Optional("fake") String browserPlatform,
			String jepriaVersion,
			@Optional("No") String forceNewBrowser,
			@Optional("No") String forceLogin,
			String username,
			String password) {
		
		automationManager = startAutomationManager(automationManager, baseUrl, browserName, browserVersion, browserPlatform, jepriaVersion, forceNewBrowser, forceLogin, username, password);
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
	@AfterMethod(groups = {"find", "create", "delete", "edit", "goto", "list", "setAndGetTextField"})
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
        		cut.setWorkstate(WorkstateEnum.SEARCH); // Приведение модуля в исходное состояние
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
	
	private JepRiaAuto startAutomationManager(
			JepRiaAuto automationManager,
			String baseUrl,
			String browserName,
			String browserVersion,
			String browserPlatform,
			String jepriaVersion,
			String forceNewBrowser,
			String forceLogin,
			String username,
			String password) {
		
		if(automationManager == null || "Yes".equalsIgnoreCase(forceNewBrowser)) {
			automationManager = getAutomationManager(baseUrl, browserName, browserVersion, browserPlatform, jepriaVersion, username, password);
		}
		
		if(!automationManager.isStarted()) {
			automationManager.start(baseUrl);
		}
		
		return automationManager;
	}
}
