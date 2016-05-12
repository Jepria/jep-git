package com.technology.jep.jepriashowcase.requestfeature.auto;

import static com.technology.jep.jepria.client.AutomationConstant.ERROR_MESSAGE_BOX_OK_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.SaveResultEnum;
import com.technology.jep.jepria.auto.manager.JepRiaAuto;
import com.technology.jep.jepria.auto.test.JepAutoTest;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAutoImpl;
import com.technology.jep.test.util.DataProviderArguments;
import com.technology.jep.test.util.JepFileDataProvider;

public class RequestFeatureBasicAutoTest extends JepAutoTest<RequestFeatureAuto> {
	private static Logger logger = Logger.getLogger(RequestFeatureBasicAutoTest.class.getName());
	
	/**
	 * Значение идентифицирующего поля записи (псевдо-ID, используется вместо PrimaryKey) 
	 */
	protected final String KEY_FIELD_VALUE = "featureName_" + System.currentTimeMillis();
	
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
	 * @param featureName - значение ключевого поля featureName, идентифицирующего тестовую запись
	 */
	@Override
	protected void createTestRecord(String featureName) {
		logger.trace("createRecordForTest()");
		cut.setWorkstate(CREATE);
		
		// arbitrary values. //TODO get rid of hardcode, fill by dataProvider
		int random = (int)Math.random();
		fillCreateForm(featureName, "featureNameEn_"+random, "This is a description #"+random);
		
		SaveResultEnum saveResult = cut.save();
		if(saveResult != SaveResultEnum.STATUS_HAS_CHANGED) {
			if(saveResult == SaveResultEnum.ERROR_MESSAGE_BOX) { // TODO Предполагаем, что запись уже существует, но нужно всё-таки убедиться
				logger.warn("createRecordForTest(): аналогичная (TODO: по какому признаку?) запись уже существует");
				cut.clickButton(ERROR_MESSAGE_BOX_OK_BUTTON_ID);
			} else {
				fail("Ошибка создания тестовой записи");
			}
		}
	}
	
	//TODO вынести как абстракный метод на уровень выше?
	protected void deleteTestRecord(String featureName) {
		super.deleteTestRecord(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID, featureName);
	}
	
	
	//TODO вынести как абстракный метод на уровень выше?
	/**
	 * Тест заполнения формы создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/createForm.fields.data")
	@Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void fillCreateForm(String featureName, String featureNameEn, String description) {
		cut.setWorkstate(CREATE);
		
        cut.fillCreateForm(
        		featureName,
        		featureNameEn,
        		description);
        
        assertEquals(featureName, cut.getFeatureName());
        assertEquals(featureNameEn, cut.getFeatureNameEn());
        assertEquals(description, cut.getDescription());
	}
}
