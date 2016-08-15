package com.technology.jep.jepriashowcase.arsenic.auto;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.exceptions.WrongOptionException;
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
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepTextField(String value) {
    cut.find();
    cut.setJepTextField(value);
    assertEquals(value, cut.getJepTextField());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepTextAreaField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepTextAreaField(String value) {
    cut.find();
    cut.setJepTextAreaField(value);
    assertEquals(value, cut.getJepTextAreaField());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepIntegerField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepIntegerField(String value) {
    cut.find();
    cut.setJepIntegerField_maxRowCount(value);
    assertEquals(value, cut.getJepIntegerField_maxRowCount());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepLongField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepLongField(String value) {
    cut.find();
    cut.setJepLongField(value);
    assertEquals(value, cut.getJepLongField());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepMoneyField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepMoneyField(String value) {
    cut.find();
    cut.setJepMoneyField(value);
    assertEquals(value, cut.getJepMoneyField());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepNumberField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepNumberField(String value) {
    cut.find();
    cut.setJepNumberField(value);
    assertEquals(value, cut.getJepNumberField());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepDateField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepDateField(String value) {
    cut.find();
    cut.setJepDateField(value);
    assertEquals(value, cut.getJepDateField());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_1.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepComboBoxFieldNotLazy(String value) {
    cut.find();
    cut.setJepComboBoxFieldNotLazy(value);
    assertEquals(value, cut.getJepComboBoxFieldNotLazy());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_1.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepComboBoxFieldSimple(String value) {
    cut.find();
    cut.setJepComboBoxFieldSimple(value);
    assertEquals(value, cut.getJepComboBoxFieldSimple());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_1.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepComboBoxFieldDurable(String value) {
    cut.find();
    cut.setJepComboBoxFieldDurable(value);
    assertEquals(value, cut.getJepComboBoxFieldDurable());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_2.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepComboBoxFieldReloading(String value) {
    cut.find();
    cut.setJepComboBoxFieldReloading(value);
    assertEquals(value, cut.getJepComboBoxFieldReloading());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_2.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepComboBoxField3chReloading(String value) {
    cut.find();
    cut.setJepComboBoxField3chReloading(value);
    assertEquals(value, cut.getJepComboBoxField3chReloading());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepListField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepDualListField(String...values) {
    cut.find();
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
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepCheckBoxField(String value) {
    cut.find();
    
    final boolean boolValue;
    
    if ("true".equals(value) || "1".equals(value)) {
      boolValue = true;
    } else if ("false".equals(value) || "0".equals(value)) {
      boolValue = false;
    } else {
      throw new IllegalArgumentException("'" + value + "' is not a valid argument. Pass 'true'/'false' or '1'/'0' only.");
    }
    
    cut.setJepCheckBoxField(boolValue);
    assertEquals(boolValue, cut.getJepCheckBoxField());
    
    cut.changeJepCheckBoxField();
    assertEquals(!boolValue, cut.getJepCheckBoxField());
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepListField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepListField(String...values) {
    cut.find();
    cut.setJepListField(values);
    
    // Поскольку getJepListField возвращает опции в порядке, определяемом самим полем JepListField,
    // то следует производить сравнение без учета порядка.
    String[] actualValues = cut.getJepListField();
    assertEquals(values.length, actualValues.length);
    Arrays.sort(values);
    Arrays.sort(actualValues);
    assertArrayEquals(values, actualValues);
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepListField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepListFieldCheckAll(String...values) {
    cut.find();
    cut.setJepListFieldCheckAll(values);
    
    // Поскольку getJepListField возвращает опции в порядке, определяемом самим полем JepListField,
    // то следует производить сравнение без учета порядка.
    String[] actualValues = cut.getJepListFieldCheckAll();
    assertEquals(values.length, actualValues.length);
    Arrays.sort(values);
    Arrays.sort(actualValues);
    assertArrayEquals(values, actualValues);
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepTreeField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepTreeField(String...values) {
    cut.find();
    cut.setJepTreeField(values);
    
    // Поскольку getJepTreeField_nodes возвращает опции в порядке, определяемом самим полем JepTreeField,
    // то следует производить сравнение без учета порядка.
    String[] actualValues = cut.getJepTreeField_checked();
    assertEquals(values.length, actualValues.length);
    Arrays.sort(values);
    Arrays.sort(actualValues);
    assertArrayEquals(values, actualValues);
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepTreeField_nodes.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepTreeField_nodes(String...values) {
    cut.find();
    cut.setJepTreeField_nodes(values);
    
    // Поскольку getJepTreeField_nodes возвращает опции в порядке, определяемом самим полем JepTreeField,
    // то следует производить сравнение без учета порядка.
    String[] actualValues = cut.getJepTreeField_nodes_checked();
    assertEquals(values.length, actualValues.length);
    Arrays.sort(values);
    Arrays.sort(actualValues);
    assertArrayEquals(values, actualValues);
    
    
    // Тестируем флажок "Выделить все"
    // Важно тестировать полное выделение именно здесь, а не в отдельном методе,
    // чтобы до этого момента иметь какую-либо развернутость дерева.
    if (cut.selectAllJepTreeField_nodes(true)) {
      actualValues = cut.getJepTreeField_nodes_checked();
      String[] visibleOptions = cut.getJepTreeField_nodes_visible();
      assertEquals(visibleOptions.length, actualValues.length);
      Arrays.sort(visibleOptions);
      Arrays.sort(actualValues);
      assertArrayEquals(visibleOptions, actualValues);
    }
    if (cut.selectAllJepTreeField_nodes(false)) {
      actualValues = cut.getJepTreeField_nodes_checked();
      assertEquals(0, actualValues.length);
    }
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepTreeField.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepTreeField_casc(String...values) {
    cut.find();
    cut.setJepTreeField_casc(values);
    
    // Поскольку getJepTreeField_casc возвращает опции в порядке, определяемом самим полем JepTreeField,
    // то следует производить сравнение без учета порядка.
    String[] actualValues = cut.getJepTreeField_casc_checked();
    assertEquals(values.length, actualValues.length);
    Arrays.sort(values);
    Arrays.sort(actualValues);
    assertArrayEquals(values, actualValues);
  }
  
  
  
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/fields.incorrect.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetFieldsIncorrect(String value) {
    cut.find();
    
    cut.setJepIntegerField_maxRowCount(value);
    Assert.assertNotEquals(value, cut.getJepIntegerField_maxRowCount());
    
    cut.setJepLongField(value);
    Assert.assertNotEquals(value, cut.getJepLongField());
    
    cut.setJepMoneyField(value);
    Assert.assertNotEquals(value, cut.getJepMoneyField());
    
    cut.setJepNumberField(value);
    Assert.assertNotEquals(value, cut.getJepNumberField());
    
    cut.setJepDateField(value);
    Assert.assertNotEquals(value, cut.getJepDateField());
    
    try {
      cut.setJepComboBoxFieldNotLazy(value);
      fail();
    } catch (WrongOptionException woe) {
      // OK
    }
    
    try {
      cut.setJepComboBoxFieldSimple(value);
      fail();
    } catch (WrongOptionException woe) {
      // OK
    }
    
    try {
      cut.setJepComboBoxFieldDurable(value);
      fail();
    } catch (WrongOptionException woe) {
      // OK
    }
    
    try {
      cut.setJepComboBoxFieldReloading(value);
      fail();
    } catch (WrongOptionException woe) {
      // OK
    }
    
    try {
      cut.setJepComboBoxField3chReloading(value);
      fail();
    } catch (WrongOptionException woe) {
      // OK
    }
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/fields.incorrect_multiple.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetFieldsIncorrectMultiple(String...values) {
    cut.find();
    
    try {
      cut.setJepDualListField(values);
      fail();
    } catch (WrongOptionException woe) {
      // OK
    }
    
    try {
      cut.setJepListField(values);
      fail();
    } catch (WrongOptionException woe) {
      // OK
    }
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepComboBoxField_1char.data")
  @Test(groups="setAndGetFields!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepComboBoxField3chReloadingIncorrect(String value) {
    cut.find();
    
    try {
      cut.setJepComboBoxField3chReloading(value);
      fail();
    } catch (WrongOptionException woe) {
      // OK
    }
  }
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/field.jepTreeField.incorrect.data")
  @Test(groups="setAndGetFields", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetJepTreeField_nodesIncorrect(String...values) {
    cut.find();
    
    try {
      cut.setJepTreeField_nodes(values);
      fail();
    } catch (WrongOptionException woe) {
      // OK
    } catch (IllegalArgumentException  iae) {
      // OK
    }
  }
  
  
  // С осторожностью: оставить visiblity=true в конце!
  @Test(groups="fieldStates")
  public void testSwitchVisiblity() {
    cut.find();
    
    cut.setCheckBox_switchVsbl(false);
    assertEquals(true, cut.checkAllFieldsVisibility(false));
    
    cut.setCheckBox_switchVsbl(true);
    assertEquals(true, cut.checkAllFieldsVisibility(true));
  }
  
  // С осторожностью: оставить enability=true в конце!
  @Test(groups="fieldStates")
  public void testSwitchEnability() {
    cut.find();
    
    cut.setCheckBox_switchEnbl(false);
    assertEquals(true, cut.checkAllFieldsEnability(false));
    
    cut.setCheckBox_switchEnbl(true);
    assertEquals(true, cut.checkAllFieldsEnability(true));
  }
  
  // С осторожностью: оставить editability=true в конце!
  @Test(groups="fieldStates")
  public void testSwitchEditability() {
    cut.find();
    
    cut.setCheckBox_switchEdtb(false);
    assertEquals(true, cut.checkAllFieldsEditability(false));
    
    cut.setCheckBox_switchEdtb(true);
    assertEquals(true, cut.checkAllFieldsEditability(true));
  }
  
  // С осторожностью: оставить allowBlank=true в конце!
  @Test(groups="fieldStates")
  public void testSwitchAllowBlank() {
    cut.find();
    
    cut.setCheckBox_switchAlbl(false);
    assertEquals(true, cut.checkAllFieldsAllowBlank(false));
    
    cut.setCheckBox_switchAlbl(true);
    assertEquals(true, cut.checkAllFieldsAllowBlank(true));
  }
  
  @Override
  protected void createTestRecord(String keyFieldValue) {
    //TODO
  }
  
  
  
  
  
  
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/list.gridHeaders.data")
  @Test(groups="list!", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void testGridHeaders(Object... headers) {
    cut.find();
    
    cut.doSearch();
    
    List<String> headersList = cut.getGridHeaders();
    
    assertArrayEquals(headers, headersList.toArray(new String[headersList.size()]));
  }
  
  @Test(groups="list!")
  public void testGridData() {
    cut.find();
    
    cut.setJepTextField("abc");
    cut.setJepTextAreaField("DEF");
    
    cut.doSearch();
    
    List<List<Object>> data = cut.getGridData();
    List<String> headers = cut.getGridHeaders();
    
    for (List<Object> row: data) {
      Object c = row.get(headers.indexOf("Text"));
      assert c instanceof String;
      assertTrue(((String)c).startsWith("abc"));
      
      Object d = row.get(headers.indexOf("TextArea"));
      assert d instanceof String;
      assertTrue(((String)d).startsWith("DEF"));
    }
  }
  
  /**
   * Переменная используется для недопущения повторного перехода на списочную форму
   * после первого запуска тестового метода testColumnOrderWithCookies.
   * Переменная не влияет на другие тестовые методы.
   */
  
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/arsenic/auto/list.gridHeaders.settings.data")
  @Test(groups="list", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void testColumnOrderWithCookies(String...values) {
    cut.find();
    cut.doSearch();
    
    cut.doGridColumnSettings(values);
    List<String> headers = cut.getGridHeaders();
    assertArrayEquals(values, headers.toArray(new String[headers.size()]));
    
    cut.refreshPage();
    
    assertArrayEquals(values, headers.toArray(new String[headers.size()]));
  }
}
