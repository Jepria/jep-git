package com.technology.jep.jepriashowcase.requestfeature.auto;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.Util;
import com.technology.jep.test.util.DataProviderArguments;
import com.technology.jep.test.util.JepFileDataProvider;

@SuppressWarnings("serial")
public class RequestFeatureFindAutoTest extends RequestFeatureBasicAutoTest {
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

	
	
	
	
	//========================================================================================
	
	/**
	 * Тест установки/получения поля featureId на форме поиска
	 * 
	 * @param featureId - устанавливаемое значение поля featureId
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureId.field.data")
	@Test(groups= "setAndGetFields", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
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
	@Test(groups= "setAndGetFields", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
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
	@Test(groups= "setAndGetFields", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
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
	@Test(groups= "setAndGetFields", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
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
	@Test(groups= "setAndGetFields", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
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
	@Test(groups= "setAndGetFields", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetRowCountOnSearch(String rowCount) {
		cut.setWorkstate(SEARCH);
		
        cut.setRowCount(rowCount);
        
        assertEquals(rowCount, cut.getRowCount());
	}

	
	//=====================================================================================================
	

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
	
	
}
