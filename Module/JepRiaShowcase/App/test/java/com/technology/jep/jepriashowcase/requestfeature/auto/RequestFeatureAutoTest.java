package com.technology.jep.jepriashowcase.requestfeature.auto;

import static com.technology.jep.jepria.client.AutomationConstant.ERROR_MESSAGE_BOX_OK_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.SaveResultEnum;
import com.technology.jep.jepria.auto.Util;
import com.technology.jep.jepria.auto.exceptions.WrongOptionException;
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
public class RequestFeatureAutoTest extends JepAutoTest<RequestFeatureAuto> {
	private static Logger logger = Logger.getLogger(RequestFeatureAutoTest.class.getName());
	
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

	/*============================= CREATE BLOCK ================================*/@SuppressWarnings("unused")private void _________CREATE_BLOCK_________(){/*A useless method to visually split blocks in Outline*/}
	
	/**
	 * Тест перехода на форму создания
	 */
	@Test(groups = {"create", "goto"})
	public void goToCreate() {
		cut.setWorkstate(CREATE);
        
		assertEquals(
				Util.getResourceString("workstate.add"),
				cut.getStatusBar().getText());
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
				logger.warn("createRecordForTest(): аналогичная (по какому признаку?) запись уже существует");
				cut.clickButton(ERROR_MESSAGE_BOX_OK_BUTTON_ID);
			} else {
				fail("Ошибка создания тестовой записи");
			}
		}
	}
	
	
	
	
	/**
	 * Тест установки/получения поля featureName на форме создания
	 * 
	 * @param featureName - устанавливаемое значение поля featureName
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.featureName.data")
	@Test(groups={"create", "setAndGetTextField"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetGoodsNameOnCreate(String featureName) {
		cut.setWorkstate(CREATE);
		
        cut.setFeatureName(featureName);
        
        assertEquals(featureName, cut.getFeatureName());
	}
	
	/**
	 * Тест установки/получения поля featureNameEn на форме создания
	 * 
	 * @param featureNameEn - устанавливаемое значение поля featureNameEn
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.featureNameEn.data")
	@Test(groups={"create", "setAndGetTextField"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameEnOnCreate(String featureNameEn) {
		cut.setWorkstate(CREATE);
		
        cut.setFeatureNameEn(featureNameEn);
        
        assertEquals(featureNameEn, cut.getFeatureNameEn());
	}
	
	/**
	 * Тест установки/получения поля description на форме создания
	 * 
	 * @param description - устанавливаемое значение поля description
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.description.data")
	@Test(groups={"create", "setAndGetTextField"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetDescriptionOnCreate(String description) {
		cut.setWorkstate(CREATE);
		
		cut.setDescription(description);
		
		assertEquals(description, cut.getDescription());
	}
	
	//TODO вынести как абстракный метод на уровень выше? // -- А надо?
	/**
	 * Тест заполнения формы создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/form.create.data")
	@Test(groups={"create", "setAndGetTextField"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
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
	
	/**
	 * Тест проверки появления popup-окна после нажатия на save при незаполненных полях 
	 */
	@Test(groups = {"edit", "create"})
	public void createWithEmptyFields() {
		cut.setWorkstate(CREATE);
		
        cut.fillCreateForm("", "", "");
		
        assertEquals(SaveResultEnum.STATUS_HAS_CHANGED, cut.save());
	}
	
	/**
	 * Собственно тест создания
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/form.create.data")
	@Test(groups={"create"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void create(String featureName, String featureNameEn, String description) {
		cut.setWorkstate(CREATE);
		
        cut.fillCreateForm(
        		featureName,
        		featureNameEn,
        		description);
        
        assertEquals(SaveResultEnum.STATUS_HAS_CHANGED, cut.save());
	}
	
	/*============================= end of CREATE BLOCK ================================*/
	
	
	/*============================= FIND BLOCK ================================*/@SuppressWarnings("unused")private void __________FIND_BLOCK__________(){/*A useless method to visually split blocks in Outline*/}
	
	/**
	 * Тест перехода на форму поиска
	 */
	@Test(groups = "goto")
	public void goToSearch() {
		cut.setWorkstate(SEARCH);
        
		assertEquals(
				Util.getResourceString("workstate.search"),
				cut.getStatusBar().getText());
	}
	
	/**
	 * Тест операции поиска по пустому шаблону
	 */
	@Test(groups = "find")
	public void find() {
		cut.setWorkstate(SEARCH);
		
		String statusBarTextBefore = cut.getStatusBar().getText();
		cut.find();
        cut.waitTextToBeChanged(cut.getStatusBar(), statusBarTextBefore);
        
		assertEquals(
				Util.getResourceString("workstate.viewList"),
				cut.getStatusBar().getText());
	}

	/**
	 * Тест операции поиска по заданному шаблону
	 */
	@Test(groups = "find")
	public void findByTemplate(Map<String, String> template) {
		cut.setWorkstate(SEARCH);
		
		String statusBarTextBefore = cut.getStatusBar().getText();
		cut.find(template);
        cut.waitTextToBeChanged(cut.getStatusBar(), statusBarTextBefore);
        
		assertEquals(
				Util.getResourceString("workstate.viewList"),
				cut.getStatusBar().getText());
	}

	/**
	 * Тест заполнения формы поиска
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/form.search.data")
	@Test(groups="find", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void fillSearchForm(String featureId, String featureName, String featureNameEn, String maxRowCount) {
		cut.setWorkstate(SEARCH);
        
        cut.fillSearchForm(
        		featureId,
        		featureName,
        		featureNameEn,
        		maxRowCount);
        
        assertEquals(featureId, cut.getFeatureId());
        assertEquals(featureName, cut.getFeatureName());
        assertEquals(featureNameEn, cut.getFeatureNameEn());
        assertEquals(maxRowCount, cut.getRowCount());
	}

	
	
	
	/**
	 * Тест установки/получения поля featureId на форме поиска
	 * 
	 * @param featureId - устанавливаемое значение поля featureId
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.featureId.data")
	@Test(groups= "setAndGetTextField", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureIdOnSearch(String featureId) {
		cut.setWorkstate(SEARCH);
		
        cut.setFeatureId(featureId);
        
        assertEquals(featureId, cut.getFeatureId());
	}
	
	/**
	 * Тест установки/получения поля featureName на форме поиска
	 * 
	 * @param featureName - устанавливаемое значение поля featureName
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.featureName.data")
	@Test(groups= "setAndGetTextField", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameOnSearch(String featureName) {
		cut.setWorkstate(SEARCH);
		
        cut.setFeatureName(featureName);
        
        assertEquals(featureName, cut.getFeatureName());
	}
	
	/**
	 * Тест установки/получения поля featureNameEn на форме поиска
	 * 
	 * @param featureNameEn - устанавливаемое значение поля featureNameEn
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.featureNameEn.data")
	@Test(groups= "setAndGetTextField", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameEnOnSearch(String featureNameEn) {
		cut.setWorkstate(SEARCH);
		
        cut.setFeatureNameEn(featureNameEn);
        
        assertEquals(featureNameEn, cut.getFeatureNameEn());
	}
	
	/**
	 * Тест установки/получения поля rowCount на форме поиска
	 * 
	 * @param rowCount - устанавливаемое значение поля rowCount
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.rowCount.data")
	@Test(groups= "setAndGetTextField", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetRowCountOnSearch(String rowCount) {
		cut.setWorkstate(SEARCH);
		
        cut.setRowCount(rowCount);
        
        assertEquals(rowCount, cut.getRowCount());
	}

	
	

	/**
	 * Тест перехода на списочную форму 
	 * 
	 * @param featureName - значение ключевого поля featureName, идентифицирующего тестовую запись
	 */
	@Test(groups = {"list", "goto"})
	@Parameters({"KEY_FIELD_VALUE"})
	public void goToList(final String featureName) {
		cut.find(new HashMap<String, String>(){{
			put(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);
		}});
        
		assertEquals(
				Util.getResourceString("workstate.viewList"),
				cut.getStatusBar().getText());
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
			put(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);
		}});
		
		cut.selectItem(0);
		
		assertEquals(
				Util.getResourceString("workstate.selected"),
				cut.getStatusBar().getText());
	}
	
	/*============================= end of FIND BLOCK ================================*/
	
	
	/*============================= DELETE BLOCK ================================*/@SuppressWarnings("unused")private void _________DELETE_BLOCK_________(){/*A useless method to visually split blocks in Outline*/}
	
	//TODO restore, consider fields in data provider
//	/**
//	 * Тест удаления записи
//	 * 
//	 * @param featureName - значение ключевого поля featureName, идентифицирующего тестовую запись
//	 */
//	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/form.delete.data")
//	@Test(groups = "delete", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = IndexOutOfBoundsException.class)
//	public void delete(final String featureName) {
//		try {
//			createTestRecord(featureName);
//			
//			Map<String, String> key = new HashMap<String, String>() {{put(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);}};
//			cut.find(key);
//			
//			try {
//				cut.selectItem(key);
//			} catch(IndexOutOfBoundsException ex) {
//		        fail("Элемент с ключом " + key + " отсутствует в списке");
//			}
//			
//			cut.delete(key);
//			
//			cut.selectItem(key); // Должно вызвать IndexOutOfBoundsException
//		} finally {
//	        deleteTestRecord(featureName); // На случай, если cut.delete не сработал
//		}
//	}
		
	//TODO вынести как абстракный метод на уровень выше?
	/**
	 * Тест удаления тестовой записи
	 */
	@Test(groups = "delete")
	protected void deleteTestRecord(String featureName) {
		super.deleteTestRecord(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);
	}
	
	/*============================= end of DELETE BLOCK ================================*/
	
	
	/*============================= EDIT BLOCK ================================*/@SuppressWarnings("unused")private void _________EDIT_BLOCK_________(){/*A useless method to visually split blocks in Outline*/}
	
	/**
	 * Тест заполнения формы редактирования
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/form.edit.data")
	@Test(groups="edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void fillEditForm(String featureId, final String featureName, String featureNameEn, String desription) {

		try {
			createTestRecord(featureName);
			
			cut.edit(new HashMap<String, String>() {{
				put(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);
			}});
			
	        
	        cut.fillEditForm(
	        		featureId,
	        		featureName,
	        		featureNameEn,
	        		desription);
	        
	        assertEquals(featureId, cut.getFeatureId());
	        assertEquals(featureName, cut.getFeatureName());
	        assertEquals(featureNameEn, cut.getFeatureNameEn());
	        assertEquals(desription, cut.getDescription());
		} finally {
	        deleteTestRecord(featureName);
		}
	}
	
	/**
	 * Тест перехода на форму редактирования
	 *  
	 * @param featureName - значение ключевого поля featureName, идентифицирующего тестовую запись
	 */
	@Test(groups = "edit")
	@Parameters({"KEY_FIELD_VALUE"})
	public void goToEdit(String featureName) {
		Map<String, String> key = new HashMap<String, String>();
		key.put(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);
		cut.edit(key);
        
		assertEquals(
				Util.getResourceString("workstate.edit"),
				cut.getStatusBar().getText());
	}
	

	
	
	/**
	 * Тест установки/получения поля featureId на форме редактирования
	 * 
	 * @param featureId - устанавливаемое значение поля featureId
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.featureId.data")
	@Test(groups={"edit"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureIdOnEdit(String featureId) {
		testSetAndGetTextFieldValueOnEdit(
				REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_ID_INPUT,
				featureId);
	}
	
	/**
	 * Тест установки/получения поля featureName на форме редактирования
	 * 
	 * @param featureName - устанавливаемое значение поля featureName
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.featureName.data")
	@Test(groups={"edit"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameOnEdit(String featureName) {
		testSetAndGetTextFieldValueOnEdit(
				REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT,
				featureName);
	}
	
	/**
	 * Тест установки/получения поля featureNameEn на форме редактирования
	 * 
	 * @param featureNameEn - устанавливаемое значение поля featureNameEn
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.featureNameEn.data")
	@Test(groups={"edit"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameEnOnEdit(String featureNameEn) {
		testSetAndGetTextFieldValueOnEdit(
				REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT,
				featureNameEn);
	}
	
	/**
	 * Тест установки некорректных значений поля featureId на форме редактирования
	 * 
	 * @param wrondFeatureId - значение поля featureId
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/field.featureId.wrong.data")
	@Test(groups = "edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = WrongOptionException.class)
	public void setWrongFeatureIdOnEdit(String wrondFeatureId) {
		testSetAndGetTextFieldValueOnEdit(
				REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_ID_INPUT,
				wrondFeatureId);
	}

	private void testSetAndGetTextFieldValueOnEdit(String testFieldId, String testFieldNewValue) {
		testSetAndGetTextFieldValueOnEdit(
				cut,
				REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT,
				KEY_FIELD_VALUE,
				testFieldId,
				testFieldNewValue,
				true);
	}
	
	/**
	 * Собственно тест редактирования
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/form.edit.data")
	@Test(groups={"edit"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void edit(String featureId, String featureName, String featureNameEn, String description) {
		cut.setWorkstate(EDIT);
		
        cut.fillEditForm(
        		featureId,
        		featureName,
        		featureNameEn,
        		description);
        
        assertEquals(SaveResultEnum.STATUS_HAS_CHANGED, cut.save());
	}
	
	/*============================= end of EDIT BLOCK ================================*/
	
	
	
}
