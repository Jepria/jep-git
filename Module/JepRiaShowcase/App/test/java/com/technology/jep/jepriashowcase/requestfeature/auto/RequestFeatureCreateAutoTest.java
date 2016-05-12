package com.technology.jep.jepriashowcase.requestfeature.auto;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.SaveResultEnum;
import com.technology.jep.jepria.auto.Util;
import com.technology.jep.test.util.DataProviderArguments;
import com.technology.jep.test.util.JepFileDataProvider;

@SuppressWarnings("serial")
public class RequestFeatureCreateAutoTest extends RequestFeatureBasicAutoTest {

	
	

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
	 * Тест установки/получения поля featureName на форме создания
	 * 
	 * @param featureName - устанавливаемое значение поля featureName
	 */
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/requestfeature/auto/featureName.field.data")
	@Test(groups={"create", "setAndGetFields"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
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
	@Test(groups={"create", "setAndGetFields"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
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
	@Test(groups={"create", "setAndGetFields"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetDescriptionOnCreate(String description) {
		cut.setWorkstate(CREATE);
		
		cut.setDescription(description);
		
		assertEquals(description, cut.getDescription());
	}
	
	
	//TODO похожие, но различные для create, edit?
	/**
	 * Тест сохранения записи 
	 */
	@Test(groups = "edit, create")
	public void save_AlertMessageBoxShouldAppear() {
		cut.setWorkstate(CREATE);
		
        assertEquals(SaveResultEnum.ALERT_MESSAGE_BOX, cut.save());
	}
	
	//TODO похожие, но различные для create, edit?
	/**
	 * Тест проверки появления popup-окна после нажатия на save при незаполненных полях 
	 */
	@Test(groups = "edit, create")
	public void save() {
		cut.setWorkstate(CREATE);
		
        cut.fillCreateForm("", "", "");
		
        assertEquals(SaveResultEnum.STATUS_HAS_CHANGED, cut.save());
	}
	
	//TODO похожие, но различные для view_details, edit, view_list?
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
}
