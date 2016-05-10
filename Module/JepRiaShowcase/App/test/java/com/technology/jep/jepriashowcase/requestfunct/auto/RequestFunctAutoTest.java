package com.technology.jep.jepriashowcase.requestfunct.auto;

import static com.technology.jep.jepria.client.AutomationConstant.ERROR_MESSAGE_BOX_OK_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.SaveResultEnum;
import com.technology.jep.jepria.auto.manager.JepRiaAuto;
import com.technology.jep.jepria.auto.test.JepAutoTest;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAutoImpl;
import com.technology.jep.test.util.DataProviderArguments;
import com.technology.jep.test.util.JepFileDataProvider;

public class RequestFunctAutoTest extends JepAutoTest<RequestFunctAuto> {
	private static Logger logger = Logger.getLogger(RequestFunctAutoTest.class.getName());
	
	@Override
	protected RequestFunctAuto getCut() {
		return ((JepRiaShowcaseAutoImpl)automationManager).getRequestFunctAuto(true);
	}

	@Override
	protected JepRiaAuto getAutomationManager(
			String baseUrl,
			String browserName,
			String browserVersion,
			String browserPlatform,
			String browserPath,
			String jepriaVersion,
			String username,
			String password) {
		
		return new JepRiaShowcaseAutoImpl(baseUrl, browserName, browserVersion, browserPlatform, browserPath, jepriaVersion, username, password);
	}

	/**
	 * Создание тестовой записи
	 * 
	 * @param featureIdFieldValue - значение ключевого поля featureId, идентифицирующего тестовую запись
	 */
	@Override
	protected void createTestRecord(String featureIdFieldValue) {
		logger.trace("createRecordForTest()");
		cut.setWorkstate(CREATE);
		
		// arbitrary values. //TODO get rid of hardcode, fill by dataProvider
		int random = (int)Math.random();
		cut.fillCreateForm("featureName_"+random, "featureNameEn_"+random, "This is a description #"+random);
		
		SaveResultEnum saveResult = cut.save();
		if(saveResult != SaveResultEnum.STATUS_HAS_CHANGED) {
			if(saveResult == SaveResultEnum.ERROR_MESSAGE_BOX) { // TODO Предполагаем, что запись уже существует, но нужно всё-таки убедиться
				logger.warn("createRecordForTest(): Запись c featureId='" + featureIdFieldValue +"' уже существует");
				cut.clickButton(ERROR_MESSAGE_BOX_OK_BUTTON_ID);
			} else {
				fail("Ошибка создания тестовой записи");
			}
		}
	}
	
	/**
	 * Тест установки/получения поля featureName на форме создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfunct/auto/RequestFunctAutoTest.setAndGetFeatureName.group.data")
	@Test(groups={"create", "setAndGetTextField"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameOnCreate(String featureNameNewValue) {
		cut.setWorkstate(CREATE);
		
        ((RequestFunctAuto)cut).setFeatureName(featureNameNewValue);
        
        assertEquals(featureNameNewValue, cut.getFeatureName());
	}
	
	/**
	 * Тест установки/получения поля featureNameEn на форме создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfunct/auto/RequestFunctAutoTest.setAndGetFeatureNameEn.group.data")
	@Test(groups={"create", "setAndGetTextField"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameEnOnCreate(String featureNameEnNewValue) {
		cut.setWorkstate(CREATE);
		
        ((RequestFunctAuto)cut).setFeatureNameEn(featureNameEnNewValue);
        
        assertEquals(featureNameEnNewValue, cut.getFeatureNameEn());
	}
	
	/**
	 * Тест установки/получения поля description на форме создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfunct/auto/RequestFunctAutoTest.setAndGetDescription.group.data")
	@Test(groups={"create", "setAndGetTextField"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetDescriptionOnCreate(String descriptionNewValue) {
		cut.setWorkstate(CREATE);
		
        ((RequestFunctAuto)cut).setDescription(descriptionNewValue);
        
        assertEquals(descriptionNewValue, cut.getDescription());
	}
	
	/**
	 * Тест заполнения формы создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfunct/auto/RequestFunctAutoTest.fillCreateForm.method.data")
	@Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void fillCreateForm(String featureName, String featureNameEn, String description) {
		cut.setWorkstate(CREATE);
		
		cut.fillCreateForm(featureName, featureNameEn, description);
		
		assertEquals(featureName, cut.getFeatureName());
		assertEquals(featureNameEn, cut.getFeatureNameEn());
		assertEquals(description, cut.getDescription());
	}
	
}
