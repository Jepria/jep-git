package com.technology.jep.jepriashowcase.requestfeature.auto;

import static com.technology.jep.jepria.client.AutomationConstant.ERROR_MESSAGE_BOX_OK_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID;

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
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureName.field.data")
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
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureNameEn.field.data")
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
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/description.field.data")
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
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/createForm.fields.data")
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
	@Test(groups = "edit, create")
	public void createWithEmptyFields() {
		cut.setWorkstate(CREATE);
		
        cut.fillCreateForm("", "", "");
		
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
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/searchForm.fields.data")
	@Test(groups="find", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void fillSearchForm(String featureId, String featureName, String featureNameEn, String fromDateIns, String toDateIns, String maxRowCount) {
		cut.setWorkstate(SEARCH);
        
        cut.fillSearchForm(
        		featureId,
        		featureName,
        		featureNameEn,
        		fromDateIns,
        		toDateIns,
        		maxRowCount);
        
        assertEquals(featureId, cut.getFeatureId());
        assertEquals(featureName, cut.getFeatureName());
        assertEquals(featureNameEn, cut.getFeatureNameEn());
        assertEquals(fromDateIns, cut.getFromDateIns());
        assertEquals(toDateIns, cut.getToDateIns());
        assertEquals(maxRowCount, cut.getRowCount());
	}

	
	
	
	/**
	 * Тест установки/получения поля featureId на форме поиска
	 * 
	 * @param featureId - устанавливаемое значение поля featureId
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureId.field.data")
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
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureName.field.data")
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
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureNameEn.field.data")
	@Test(groups= "setAndGetTextField", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameEnOnSearch(String featureNameEn) {
		cut.setWorkstate(SEARCH);
		
        cut.setFeatureNameEn(featureNameEn);
        
        assertEquals(featureNameEn, cut.getFeatureNameEn());
	}
	
	/**
	 * Тест установки/получения поля fromDateIns на форме поиска
	 * 
	 * @param fromDateIns - устанавливаемое значение поля fromDateIns
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/fromDateIns.field.data")
	@Test(groups= "setAndGetTextField", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFromDateInsOnSearch(String fromDateIns) {
		cut.setWorkstate(SEARCH);
		
		cut.setFromDateIns(fromDateIns);
		
		assertEquals(fromDateIns, cut.getFromDateIns());
	}
	
	/**
	 * Тест установки/получения поля toDateIns на форме поиска
	 * 
	 * @param toDateIns - устанавливаемое значение поля toDateIns
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/toDateIns.field.data")
	@Test(groups= "setAndGetTextField", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetToDateInsOnSearch(String toDateIns) {
		cut.setWorkstate(SEARCH);
		
		cut.setToDateIns(toDateIns);
		
		assertEquals(toDateIns, cut.getToDateIns());
	}
	
	/**
	 * Тест установки/получения поля rowCount на форме поиска
	 * 
	 * @param rowCount - устанавливаемое значение поля rowCount
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/rowCount.field.data")
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
			put(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID, featureName);
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
			put(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID, featureName);
		}});
		
		cut.selectItem(0);
		
		assertEquals(
				Util.getResourceString("workstate.selected"),
				cut.getStatusBar().getText());
	}
	
	/*============================= end of FIND BLOCK ================================*/
	
	
	/*============================= DELETE BLOCK ================================*/@SuppressWarnings("unused")private void _________DELETE_BLOCK_________(){/*A useless method to visually split blocks in Outline*/}
	
	/**
	 * Тест удаления записи
	 * 
	 * @param featureName - значение ключевого поля featureName, идентифицирующего тестовую запись
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/deleteRecord.fields.data")
	@Test(groups = "delete", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = IndexOutOfBoundsException.class)
	public void delete(final String featureName) {
		try {
			createTestRecord(featureName);
			
			Map<String, String> key = new HashMap<String, String>() {{put(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID, featureName);}};
			cut.find(key);
			
			try {
				cut.selectItem(key);
			} catch(IndexOutOfBoundsException ex) {
		        fail("Элемент с ключом " + key + " отсутствует в списке");
			}
			
			cut.delete(key);
			
			cut.selectItem(key); // Должно вызвать IndexOutOfBoundsException
		} finally {
	        deleteTestRecord(featureName); // На случай, если cut.delete не сработал
		}
	}
		
	//TODO вынести как абстракный метод на уровень выше?
	protected void deleteTestRecord(String featureName) {
		super.deleteTestRecord(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID, featureName);
	}
	
	/*============================= end of DELETE BLOCK ================================*/
	
	
	/*============================= EDIT BLOCK ================================*/@SuppressWarnings("unused")private void _________EDIT_BLOCK_________(){/*A useless method to visually split blocks in Outline*/}
	
	/**
	 * Тест заполнения формы редактирования
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/editForm.fields.data")
	@Test(groups="edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void fillEditForm(String featureId, final String featureName, String featureNameEn, String desription) {

		try {
			createTestRecord(featureName);
			
			cut.edit(new HashMap<String, String>() {{
				put(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID, featureName);
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
	public void edit(String featureName) {
		Map<String, String> key = new HashMap<String, String>();
		key.put(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID, featureName);
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
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureId.field.data")
	@Test(groups={"edit"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureIdOnEdit(String featureId) {
		testSetAndGetTextFieldValueOnEdit(
				REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_INPUT_ID,
				featureId);
	}
	
	/**
	 * Тест установки/получения поля featureName на форме редактирования
	 * 
	 * @param featureName - устанавливаемое значение поля featureName
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureName.field.data")
	@Test(groups={"edit"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameOnEdit(String featureName) {
		testSetAndGetTextFieldValueOnEdit(
				REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID,
				featureName);
	}
	
	/**
	 * Тест установки/получения поля featureNameEn на форме редактирования
	 * 
	 * @param featureNameEn - устанавливаемое значение поля featureNameEn
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureNameEn.field.data")
	@Test(groups={"edit"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetFeatureNameEnOnEdit(String featureNameEn) {
		testSetAndGetTextFieldValueOnEdit(
				REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_INPUT_ID,
				featureNameEn);
	}
	
	/**
	 * Тест установки некорректных значений поля featureId на форме редактирования
	 * 
	 * @param wrondFeatureId - значение поля featureId
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureId.wrong.field.data")
	@Test(groups = "edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = WrongOptionException.class)
	public void setWrongFeatureIdOnEdit(String wrondFeatureId) {
		testSetAndGetTextFieldValueOnEdit(
				REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_INPUT_ID,
				wrondFeatureId);
	}

	private void testSetAndGetTextFieldValueOnEdit(String testFieldId, String testFieldNewValue) {
		testSetAndGetTextFieldValueOnEdit(
				cut,
				REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID,
				KEY_FIELD_VALUE,
				testFieldId,
				testFieldNewValue,
				true);
	}
	
	/*============================= end of EDIT BLOCK ================================*/
	
	
	
}
