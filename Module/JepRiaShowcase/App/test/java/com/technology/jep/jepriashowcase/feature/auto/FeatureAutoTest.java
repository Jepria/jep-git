package com.technology.jep.jepriashowcase.feature.auto;

import static com.technology.jep.jepria.client.AutomationConstant.ERROR_MESSAGE_BOX_OK_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.FEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.SaveResultEnum;
import com.technology.jep.jepria.auto.Util;
import com.technology.jep.jepria.auto.manager.JepRiaAuto;
import com.technology.jep.jepria.auto.test.JepAutoTest;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAutoImpl;
import com.technology.jep.test.util.DataProviderArguments;
import com.technology.jep.test.util.JepFileDataProvider;

/**
 * Below, every single newline has its meaning to somehow increase readability of the code!
 * @author RomanovAS
 */
@SuppressWarnings("serial")
public class FeatureAutoTest extends JepAutoTest<FeatureAuto> {
	private static Logger logger = Logger.getLogger(FeatureAutoTest.class.getName());
	
	/**
	 * Значение идентифицирующего поля записи (псевдо-ID, используется вместо PrimaryKey) 
	 */
	protected final String KEY_FIELD_VALUE = "featureName_" + System.currentTimeMillis();
	
	@Override
	protected FeatureAuto getCut() {
		return ((JepRiaShowcaseAutoImpl)automationManager).getFeatureAuto(true);
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

	/*============================= CREATE BLOCK ================================*/
	
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
		cut.fillCreateForm(featureName, "featureNameEn_"+random, "This is a description #"+random);
		
		SaveResultEnum saveResult = cut.save();
		if(saveResult != SaveResultEnum.STATUS_HAS_CHANGED) {
			if(saveResult == SaveResultEnum.ERROR_MESSAGE_BOX) { // TODO Предполагаем, что запись уже существует, но нужно всё-таки убедиться
				logger.warn("createRecordForTest(): аналогичная (по какому признаку?) запись уже существует");
				cut.clickButton(ERROR_MESSAGE_BOX_OK_BUTTON_ID);
			} else {
				fail("Ошибка создания тестовой записи");
			}
		}
	}
	
	/**
	 * Собственно тест создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/feature/auto/form.create.data")
	@Test(groups={"create"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void create(String featureName, String featureNameEn, String description) {
		cut.setWorkstate(CREATE);
		
		// Проверяем, что осуществился переход на форму создания
		assertEquals(
				Util.getResourceString("workstate.add"),
				cut.getStatusBar().getText());
		
		// Заполняем форму создания
		cut.fillCreateForm(
				featureName,
				featureNameEn,
				description);
		
		// Проверяем, что поля заполненны именно теми значениями, которыми мы их заполнили
		assertEquals(featureName, cut.getFeatureName());
		assertEquals(featureNameEn, cut.getFeatureNameEn());
		assertEquals(description, cut.getDescription());
		
		// Осуществляем сохранение записи
		SaveResultEnum saveResult = cut.save();
		assertEquals(SaveResultEnum.STATUS_HAS_CHANGED, saveResult);
		
		//TODO работает без строчки ниже, однако по хорошему она должна присутствовать, но почему то с ней не работает
//		// Ждем перехода на форму просмотра после успешного создания
//		cut.waitTextToBeChanged(cut.getStatusBar(), Util.getResourceString("workstate.viewDetails"));
		
		// Проверяем, что осуществился переход на форму просмотра после создания
 		assertEquals(
 				Util.getResourceString("workstate.viewDetails"),
 				cut.getStatusBar().getText());
 		
 		// Проверяем, что поля созданной записи имеют такие же значения, как и те, которыми мы их заполнили
		assertEquals(featureName, cut.getFeatureName());
		assertEquals(featureNameEn, cut.getFeatureNameEn());
		assertEquals(description, cut.getDescription());
	}
	
	/*============================= end of CREATE BLOCK ================================*/
	
	
	/*============================= FIND BLOCK ================================*/
	
	/**
	 * Тест операции поиска по шаблону
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/feature/auto/form.search.data")
	@Test(groups={"find"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void findByTemplate(String featureId, String featureName, String featureNameEn, String maxRowCount) {
		cut.setWorkstate(SEARCH);
		
		// Проверяем, что осуществился переход на форму поиска
		assertEquals(
				Util.getResourceString("workstate.search"),
				cut.getStatusBar().getText());
		
		// Заполняем форму поиска
		cut.fillSearchForm(
				featureId,
				featureName,
				featureNameEn,
				maxRowCount);
		
		// Проверяем, что поля заполненны именно теми значениями, которыми мы их заполнили
		assertEquals(featureId, cut.getFeatureId());
		assertEquals(featureName, cut.getFeatureName());
		assertEquals(featureNameEn, cut.getFeatureNameEn());
		assertEquals(maxRowCount, cut.getRowCount());
			
		// Осуществляем поиск
		String statusBarTextBefore = cut.getStatusBar().getText();
		cut.find();
		cut.waitTextToBeChanged(cut.getStatusBar(), statusBarTextBefore);
		
		// Проверяем, что осуществился переход на форму списка после поиска
 		assertEquals(
 				Util.getResourceString("workstate.viewList"),
 				cut.getStatusBar().getText());
 		
 		// Проверяем, что данные в списке имеют значения, соответствующие поисковому шаблону
 		// TODO obtain data from list cells and assert they are equal with original
	}

	/**
	 * Тест выделения первого элемента списка списочной формы
	 * 
	 * @param featureName - значение ключевого поля featureName, идентифицирующего тестовую запись
	 */
	@Test(groups = "list")
	@Parameters({"KEY_FIELD_VALUE"})
	public void selectFirstItem(final String featureName) {
		cut.find(new HashMap<String, String>(){{
			put(FEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);
		}});
		
		cut.selectItem(0);
		
		assertEquals(
				Util.getResourceString("workstate.selected"),
				cut.getStatusBar().getText());
	}
	
	/*============================= end of FIND BLOCK ================================*/
	
	
	/*============================= DELETE BLOCK ================================*/
	
	//TODO restore, consider fields in data provider
//	/**
//	 * Тест удаления записи
//	 * 
//	 * @param featureName - значение ключевого поля featureName, идентифицирующего тестовую запись
//	 */
//	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/feature/auto/form.delete.data")
//	@Test(groups = "delete", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = IndexOutOfBoundsException.class)
//	public void delete(final String featureName) {
//		try {
//			createTestRecord(featureName);
//			
//			Map<String, String> key = new HashMap<String, String>() {{put(FEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);}};
//			cut.find(key);
//			
//			try {
//				cut.selectItem(key);
//			} catch(IndexOutOfBoundsException ex) {
//				fail("Элемент с ключом " + key + " отсутствует в списке");
//			}
//			
//			cut.delete(key);
//			
//			cut.selectItem(key); // Должно вызвать IndexOutOfBoundsException
//		} finally {
//			deleteTestRecord(featureName); // На случай, если cut.delete не сработал
//		}
//	}
		
	//TODO вынести как абстракный метод на уровень выше?
	/**
	 * Удаление тестовой записи
	 */
	@Test(groups = "delete")
	protected void deleteTestRecord(String featureName) {
		super.deleteTestRecord(FEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);
	}
	
	/*============================= end of DELETE BLOCK ================================*/
	
	
	/*============================= EDIT BLOCK ================================*/
	
	/**
	 * Собственно тест редактирования
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/feature/auto/form.edit.data")
	@Test(groups={"edit"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void edit(String featureId, String featureName, String featureNameEn, String description) {
		createTestRecord("ABCDE");
		
		cut.setWorkstate(EDIT);
		
		cut.fillEditForm(
				featureId,
				featureName,
				featureNameEn,
				description);
		
		// Проверяем, что поля заполненны именно теми значениями, которыми мы их заполнили
		assertEquals(featureId, cut.getFeatureId());
		assertEquals(featureName, cut.getFeatureName());
		assertEquals(featureNameEn, cut.getFeatureNameEn());
		assertEquals(description, cut.getDescription());
		
		// Осуществляем сохранение записи
		SaveResultEnum saveResult = cut.save();
		assertEquals(SaveResultEnum.STATUS_HAS_CHANGED, saveResult);
		
	  //TODO работает без строчки ниже, однако по хорошему она должна присутствовать, но почему то с ней не работает
//	  // Ждем перехода на форму просмотра после успешного создания
//	  cut.waitTextToBeChanged(cut.getStatusBar(), Util.getResourceString("workstate.viewDetails"));
	  
		// Проверяем, что осуществился переход на форму просмотра после редактирования
		assertEquals(
				Util.getResourceString("workstate.viewDetails"),
				cut.getStatusBar().getText());
		
		// Проверяем, что поля отредактированной записи имеют такие же значения, как и те, которыми мы их заполнили
		assertEquals(featureId, cut.getFeatureId());
		assertEquals(featureName, cut.getFeatureName());
		assertEquals(featureNameEn, cut.getFeatureNameEn());
		assertEquals(description, cut.getDescription());
	}
	
	/*============================= end of EDIT BLOCK ================================*/
	
}
