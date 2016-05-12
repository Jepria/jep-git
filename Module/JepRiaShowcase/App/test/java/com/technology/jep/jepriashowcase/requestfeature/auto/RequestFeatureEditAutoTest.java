package com.technology.jep.jepriashowcase.requestfeature.auto;

import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.Util;
import com.technology.jep.jepria.auto.exceptions.WrongOptionException;
import com.technology.jep.test.util.DataProviderArguments;
import com.technology.jep.test.util.JepFileDataProvider;

@SuppressWarnings("serial")
public class RequestFeatureEditAutoTest extends RequestFeatureBasicAutoTest {
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
	
	//=======================================================================================================
	
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
}
