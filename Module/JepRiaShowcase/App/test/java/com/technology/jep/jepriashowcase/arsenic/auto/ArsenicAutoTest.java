package com.technology.jep.jepriashowcase.arsenic.auto;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.exceptions.AutomationException;
import com.technology.jep.jepria.auto.manager.JepRiaAuto;
import com.technology.jep.jepria.auto.test.JepAutoTest;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAutoImpl;
import com.technology.jep.test.util.DataProviderArguments;
import com.technology.jep.test.util.JepFileDataProvider;

public class ArsenicAutoTest extends JepAutoTest<ArsenicAuto> {
	
	@Override
	protected ArsenicAuto getCut() {
    	return ((JepRiaShowcaseAuto)automationManager).getArsenicAuto(true);
	}

	@Override
	protected JepRiaAuto getAutomationManager(
			String baseUrl,
			String browserName,
			String browserVersion,
			String browserPlatform,
			String browserPath,
			String driverPath,
			String jepriaVersion,
			String username,
			String password) {
		
		return new JepRiaShowcaseAutoImpl(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password);
	}

	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepTextField.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepTextField(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepTextField(value);
		assertEquals(value, cut.getJepTextField());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepTextAreaField.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepTextAreaField(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepTextAreaField(value);
		assertEquals(value, cut.getJepTextAreaField());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepIntegerField.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepIntegerField(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepIntegerField(value);
		assertEquals(value, cut.getJepIntegerField());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepLongField.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepLongField(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepLongField(value);
		assertEquals(value, cut.getJepLongField());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepMoneyField.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepMoneyField(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepMoneyField(value);
		assertEquals(value, cut.getJepMoneyField());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepNumberField.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepNumberField(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepNumberField(value);
		assertEquals(value, cut.getJepNumberField());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepDateField.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepDateField(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepDateField(value);
		assertEquals(value, cut.getJepDateField());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_1.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepComboBoxFieldNotLazy(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepComboBoxFieldNotLazy(value);
		assertEquals(value, cut.getJepComboBoxFieldNotLazy());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_1.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepComboBoxFieldSimple(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepComboBoxFieldSimple(value);
		assertEquals(value, cut.getJepComboBoxFieldSimple());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_1.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepComboBoxFieldDurable(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepComboBoxFieldDurable(value);
		assertEquals(value, cut.getJepComboBoxFieldDurable());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_2.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepComboBoxFieldReloading(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepComboBoxFieldReloading(value);
		assertEquals(value, cut.getJepComboBoxFieldReloading());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_2.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepComboBoxField3chReloading(String value) {
		cut.setWorkstate(SEARCH);
		cut.setJepComboBoxField3chReloading(value);
		assertEquals(value, cut.getJepComboBoxField3chReloading());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepDualListField.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepDualListField(String value) {
		cut.setWorkstate(SEARCH);
		String[] values = value.split(";");
		cut.setJepDualListField(values);

		// Поскольку getJepDualListField возвращает опции в порядке, определяемом самим полем JepDualListField,
		// то следует производить сравнение без учета порядка.
		String[] actualValues = cut.getJepDualListField();
		assertEquals(values.length, actualValues.length);
		Arrays.sort(values);
		Arrays.sort(actualValues);
		assertArrayEquals(values, actualValues);
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepCheckBoxField.data")
	@Test(groups="setAndGetTextField!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepCheckBoxField(String value) {
		cut.setWorkstate(SEARCH);
		
		final boolean boolValue; 
				
		if ("true".equals(value) || "1".equals(value)) {
			boolValue = true;
		} else if ("false".equals(value) || "0".equals(value)) {
			boolValue = false;
		} else {
			throw new AutomationException("'" + value + "' is not a valid argument. Pass 'true'/'false' or '1'/'0' only.");
		}
		
		cut.setJepCheckBoxField(boolValue);
		assertEquals(boolValue, cut.getJepCheckBoxField());
		
		cut.changeJepCheckBoxField();
		assertEquals(!boolValue, cut.getJepCheckBoxField());
	}
	
	@DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepDualListField.data")
	@Test(groups="setAndGetTextField", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
	public void setAndGetJepListField(String value) {
		cut.setWorkstate(SEARCH);
		String[] values = value.split(";");
		cut.setJepListField(values);
		
		// Поскольку getJepListField возвращает опции в порядке, определяемом самим полем JepListField,
		// то следует производить сравнение без учета порядка.
		String[] actualValues = cut.getJepListField();
		assertEquals(values.length, actualValues.length);
		Arrays.sort(values);
		Arrays.sort(actualValues);
		assertArrayEquals(values, actualValues);
	}
	
	@Override
	protected void createTestRecord(String keyFieldValue) {
		//TODO
	}
}
