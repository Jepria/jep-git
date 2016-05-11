package com.technology.jep.jepriashowcase.requestfeature.auto;

import static com.technology.jep.jepria.client.AutomationConstant.ERROR_MESSAGE_BOX_OK_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.SaveResultEnum;
import com.technology.jep.jepria.auto.manager.JepRiaAuto;
import com.technology.jep.jepria.auto.test.JepAutoTest;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAutoImpl;
import com.technology.jep.jepriashowcase.requestfeature.auto.RequestFeatureAuto;
import com.technology.jep.test.util.DataProviderArguments;
import com.technology.jep.test.util.JepFileDataProvider;

public class RequestFeatureAutoTest extends JepAutoTest<RequestFeatureAuto> {
	private static Logger logger = Logger.getLogger(RequestFeatureAutoTest.class.getName());
	
	@Override
	protected RequestFeatureAuto getCut() {
		return ((JepRiaShowcaseAutoImpl)automationManager).getRequestFeatureAuto(true);
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
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/RequestFeatureAutoTest.setAndGetFeatureName.group.data")
	@Test(groups={"create", "setAndGetTextField"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameOnCreate(String featureNameNewValue) {
		cut.setWorkstate(CREATE);
		
        ((RequestFeatureAuto)cut).setFeatureName(featureNameNewValue);
        
        assertEquals(featureNameNewValue, cut.getFeatureName());
	}
	
	/**
	 * Тест установки/получения поля featureNameEn на форме создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/RequestFeatureAutoTest.setAndGetFeatureNameEn.group.data")
	@Test(groups={"create", "setAndGetTextField"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameEnOnCreate(String featureNameEnNewValue) {
		cut.setWorkstate(CREATE);
		
        ((RequestFeatureAuto)cut).setFeatureNameEn(featureNameEnNewValue);
        
        assertEquals(featureNameEnNewValue, cut.getFeatureNameEn());
	}
	
	/**
	 * Тест установки/получения поля description на форме создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/RequestFeatureAutoTest.setAndGetDescription.group.data")
	@Test(groups={"create", "setAndGetTextField"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetDescriptionOnCreate(String descriptionNewValue) {
		cut.setWorkstate(CREATE);
		
        ((RequestFeatureAuto)cut).setDescription(descriptionNewValue);
        
        assertEquals(descriptionNewValue, cut.getDescription());
	}
	
	/**
	 * Тест заполнения формы создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/RequestFeatureAutoTest.fillCreateForm.method.data")
	@Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void fillCreateForm(String featureName, String featureNameEn, String description) {
		cut.setWorkstate(CREATE);
		
		cut.fillCreateForm(featureName, featureNameEn, description);
		
		assertEquals(featureName, cut.getFeatureName());
		assertEquals(featureNameEn, cut.getFeatureNameEn());
		assertEquals(description, cut.getDescription());
	}
	
}
