package com.technology.jep.jepriashowcase.goods.auto;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ERROR_MESSAGE_BOX_OK_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepriashowcase.goods.client.GoodsAutomationConstant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.technology.jep.test.util.JepTestUtil;

@SuppressWarnings("serial")
public class GoodsAutoTest extends JepAutoTest<GoodsAuto> {
  private static Logger logger = Logger.getLogger(GoodsAutoTest.class.getName());
  
  /**
   * ID Web-элемента идентифицирующего поля записи - 'Наименование товара' 
   */
  private final String KEY_FIELD_WEB_ELEMENT_ID = GOODS_GOODS_NAME_TEXT_FIELD_ID;
  /**
   * Значение идентифицирующего поля записи (псевдо-ID, используется вместо PrimaryKey) 
   */
  private final String KEY_FIELD_VALUE = "TestName_" + System.currentTimeMillis();
  
      
  @Override
  protected GoodsAuto getCut() {
      return ((JepRiaShowcaseAutoImpl)automationManager).getGoodsAuto(true); // TODO Убрать cast
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
    fail("Тест не реализован");
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
   * Тест перехода на форму создания
   */
  @Test(groups = {"create", "goto"})
  public void goToCreate() {
    cut.setWorkstate(CREATE);
        
    assertEquals(
        Util.getResourceString("workstate.add"),
        cut.getStatusBar().getText());
  }
  
  /*
   * Создание 
   */
  /**
   * Тест заполнения формы поиска
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.fillSearchForm.method.data")
  @Test(groups="find", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void fillSearchForm(String goodsName, String goodsType, String unit, String goodsSegment, String strGoodsCatalogSections) {
    cut.setWorkstate(SEARCH);
        
    Set<String> goodsCatalogSections = JepTestUtil.parseCSV(strGoodsCatalogSections);
    
        cut.fillSearchForm(
            goodsName,
            goodsType,
            unit,
            goodsSegment,
            goodsCatalogSections);
        
        assertEquals(goodsName, cut.getGoodsName());
        assertEquals(goodsType, cut.getGoodsType());
        assertEquals(unit, cut.getUnit());
        assertEquals(goodsSegment, cut.getSegment());
        assertEquals(goodsCatalogSections, cut.getCatalogSections());
  }

  
  /**
   * Тест заполнения формы создания
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.fillCreateForm.method.data")
  @Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void fillCreateForm(String goodsName, String goodsType, String unit, String motivation, String purchasingPrice) {
    cut.setWorkstate(CREATE);
    
        final String GOODS_PHOTO = "...";      // TODO Поддержать выбор файла
        final String GOODS_SPECIFICATION = "...";  // TODO Поддержать выбор файла
        
        cut.fillCreateForm(
            goodsName,
            goodsType,
            unit,
            motivation,
            purchasingPrice,
            GOODS_PHOTO,
            GOODS_SPECIFICATION);
        
        assertEquals(goodsName, cut.getGoodsName());
        assertEquals(goodsType, cut.getGoodsType());
        assertEquals(unit, cut.getUnit());
        assertEquals(motivation, cut.getMotivation());
        assertEquals(purchasingPrice, cut.getPurchasingPrice());
  }
  

  /**
   * Тест установки/получения поля 'Наименование товара' на форме поиска
   * 
   * @param goodsNameNewValue - устанавливаемое значение поля 'Наименование товара'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetGoodsName.group.data")
  @Test(groups= "setAndGetFields", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetGoodsNameOnSearch(String goodsNameNewValue) {
    cut.setWorkstate(SEARCH);
    
        cut.setGoodsName(goodsNameNewValue);
        
        assertEquals(goodsNameNewValue, cut.getGoodsName());
  }

  /**
   * Тест установки/получения поля 'Наименование товара' на форме создания
   * 
   * @param goodsNameNewValue - устанавливаемое значение поля 'Наименование товара'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetGoodsName.group.data")
  @Test(groups={"create", "setAndGetFields"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetGoodsNameOnCreate(String goodsNameNewValue) {
    cut.setWorkstate(CREATE);
    
        cut.setGoodsName(goodsNameNewValue);
        
        assertEquals(goodsNameNewValue, cut.getGoodsName());
  }
  
  /**
   * Тест установки/получения поля 'Тип товара' на форме создания
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetGoodsType.group.data")
  @Test(groups={"create", "setAndGetFields"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetGoodsTypeOnCreate(String goodsTypeNewValue) {
    cut.setWorkstate(CREATE);
    
        cut.setGoodsType(goodsTypeNewValue);
        
        assertEquals(goodsTypeNewValue, cut.getGoodsType());
  }
  
  
  /**
   * Тест установки/получения поля 'Тип товара' на форме создания
   * 
   * @param typeNewValue - значение поля 'Тип товара'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetTypeOnCreate.method.data")
  @Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetTypeOnCreate(String typeNewValue) {
    cut.setWorkstate(CREATE);
    
        cut.setGoodsType(typeNewValue);
        
        assertEquals(typeNewValue, cut.getGoodsType());
  }
  
  /**
   * Тест установки некорретного значения поля 'Тип товара' на форме создания
   * 
   * @param typeNewValue - устанавливаемое  значение поля 'Тип товара'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setWrongTypeOnCreate.method.data")
  @Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = WrongOptionException.class)
  public void setWrongTypeOnCreate(String wrongTypeNewValue) {
    cut.setWorkstate(CREATE);
    
        cut.setGoodsType(wrongTypeNewValue);
  }

  /**
   * Тест установки/получения поля 'Единицы измерения' на форме создания
   * 
   * @param unitNewValue - устанавливаемое значение поля 'Единицы измерения'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetUnitOnCreate.method.data ")
  @Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetUnitOnCreate(String unitNewValue) {
    cut.setWorkstate(CREATE);
    
        cut.setUnit(unitNewValue);
        
        assertEquals(unitNewValue, cut.getUnit());
  }
  
  /**
   * Тест установки некорретного значения поля 'Единицы измерения' на форме создания
   * 
   * @param unitNewValue - устанавливаемое  значение поля 'Единицы измерения'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setWrongUnitOnCreate.method.data")
  @Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = WrongOptionException.class)
  public void setWrongUnitOnCreate(String wrongUnitNewValue) {
    cut.setWorkstate(CREATE);
    
        cut.setUnit(wrongUnitNewValue);
  }
  

  /**
   * Тест установки/получения поля 'Мотивация' на форме создания
   * 
   * @param motivationNewValue - устанавливаемое значение поля 'Мотивация'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetMotivationOnCreate.method.data")
  @Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetMotivationOnCreate(String motivationNewValue) {
    cut.setWorkstate(CREATE);
    
        cut.setMotivation(motivationNewValue);
        
        assertEquals(motivationNewValue, cut.getMotivation());
  }
  
  /**
   * Тест установки некорретного значения поля 'Мотивация' на форме создания
   * 
   * @param motivationNewValue - устанавливаемое значение поля 'Мотивация'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setWrongMotivationOnCreate.method.data")
  @Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = WrongOptionException.class)
  public void setWrongMotivationOnCreate(String wrongMotivationNewValue) {
    cut.setWorkstate(CREATE);
    
        cut.setMotivation(wrongMotivationNewValue);
  }
  
  /**
   * Тест установки/получения значения поля 'Закупочная цена' на форме создания
   * 
   * @param purchasingPriceNewValue - устанавливаемое  значение поля 'Закупочная цена'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetPurchasingPriceOnCreateAndEdit.method.data")
  @Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setPurchasingPriceOnCreate(String purchasingPriceNewValue) {
    cut.setWorkstate(CREATE);
    
        cut.setPurchasingPrice(purchasingPriceNewValue);
        
        assertEquals(purchasingPriceNewValue, cut.getPurchasingPrice());
  }
  
  
  /**
   * Тест установки некорретного значения поля 'Закупочная цена' на форме создания
   * 
   * @param purchasingPriceNewValue - устанавливаемое значение поля 'Закупочная цена'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setWrongPurchasingPriceOnCreateAndEdit.method.data")
  @Test(groups="create", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setWrongPurchasingPriceOnCreate(String wrongPurchasingPriceNewValue) {
    cut.setWorkstate(CREATE);
    
        cut.setPurchasingPrice(wrongPurchasingPriceNewValue);
        
    assertFalse(wrongPurchasingPriceNewValue.equals(cut.getPurchasingPrice()));
  }

  /**
   * Тест перехода на форму редактирования
   *  
   * @param goodsName - значение поля 'Наименование товара', идентифицирующее тестовую запись
   */
  @Test(groups = "edit")
  @Parameters({"KEY_FIELD_VALUE"})
  public void edit(String goodsName) {
    Map<String, String> key = new HashMap<String, String>();
    key.put(GOODS_GOODS_NAME_TEXT_FIELD_ID, goodsName);
    cut.edit(key);
        
    assertEquals(
        Util.getResourceString("workstate.edit"),
        cut.getStatusBar().getText());
  }

  /**
   * Тест перехода на списочную форму 
   * @param goodsName - значение поля 'Наименование товара', идентифицирующее тестовую запись
   */
  @Test(groups = {"list", "goto"})
//  @Parameters({"KEY_FIELD_VALUE"})
  public void goToList(final String goodsName) {
    cut.find(new HashMap<String, String>(){{
      put(GOODS_GOODS_NAME_TEXT_FIELD_ID, goodsName);
    }});
        
    assertEquals(
        Util.getResourceString("workstate.viewList"),
        cut.getStatusBar().getText());
  }

  /**
   * Тест выделения первого элемента списка списочной формы
   * @param goodsName - значение поля 'Наименование товара', идентифицирующее тестовую запись
   */
  @Test(groups = "list")
//  @Parameters({"KEY_FIELD_VALUE"})
  public void selectFirstItem(final String goodsName) {
    cut.find(new HashMap<String, String>(){{
      put(GOODS_GOODS_NAME_TEXT_FIELD_ID, goodsName);
    }});
    
    cut.selectItem(0);
    
    assertEquals(
        Util.getResourceString("workstate.selected"),
        cut.getStatusBar().getText());
  }
  
  /**
   * Тест установки/получения поля 'Наименование товара' на форме редактирования
   * 
   * @param goodsNameNewValue - значение поля 'Наименование товара'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetGoodsName.group.data")
  @Test(groups = {"edit", "setAndGetFields"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetGoodsNameOnEdit(String goodsNameNewValue) {
    
    testSetAndGetTextFieldValueOnEdit(
        GOODS_GOODS_NAME_TEXT_FIELD_ID,
        goodsNameNewValue);
  }
  
  /**
   * Тест установки/получения поля 'Тип товара' на форме редактирования
   * 
   * @param typeFieldValue - значение поля 'Тип товара'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetTypeOnEdit.method.data ")
  @Test(groups = "edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetTypeOnEdit(String typeFieldValue) {
    
    testSetAndGetComboBoxFieldValueOnEdit(
        GOODS_GOODS_TYPE_COMBOBOX_FIELD_ID,
        typeFieldValue);
  }

  /**
   * Тест установки некорректных значений поля 'Единица измерения' на форме редактирования
   * 
   * @param wrongUnitFieldValue - значение поля 'Единица измерения', идентифицирующее тестовую запись
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setWrongTypeOnEdit.method.data")
  @Test(groups = "edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = WrongOptionException.class)
  public void setWrongTypeOnEdit(String wrongTypeFieldValue) {
    
    testSetAndGetComboBoxFieldValueOnEdit(
        GOODS_GOODS_TYPE_COMBOBOX_FIELD_ID,
        wrongTypeFieldValue);
  }
  
  /**
   * Тест установки/получения значения поля 'Единица измерения' на форме редактирования
   * 
   * @param unitFieldValue - значение поля 'Единица измерения'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetUnitOnEdit.method.data")
  @Test(groups = "edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetUnitOnEdit(String unitFieldValue) {
    
    testSetAndGetComboBoxFieldValueOnEdit(
        GOODS_UNIT_COMBOBOX_FIELD_ID,
        unitFieldValue);
    
  }

  /**
   * Тест установки некорректных значений поля 'Единица измерения' на форме редактирования
   * 
   * @param wrongUnitFieldValue - значение поля 'Единица измерения', идентифицирующее тестовую запись
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setWrongUnitOnEdit.method.data")
  @Test(groups = "edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = WrongOptionException.class)
  public void setWrongUnitOnEdit(String wrongUnitFieldValue) {
    
    testSetAndGetComboBoxFieldValueOnEdit(
        GOODS_UNIT_COMBOBOX_FIELD_ID,
        wrongUnitFieldValue);
  }

  /**
   * Тест установки значения поля 'Мотивация' на форме редактирования
   * 
   * @param motivationFieldValue - значение поля 'Мотивация'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetMotivationOnEdit.method.data")
  @Test(groups = "edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetMotivationOnEdit(String motivationFieldValue) {
    try {
      createTestRecord(KEY_FIELD_VALUE);
      
      cut.edit(new HashMap<String, String>() {{
        put(KEY_FIELD_WEB_ELEMENT_ID, KEY_FIELD_VALUE);
      }});
      
          cut.setMotivation(motivationFieldValue);
          assertEquals(motivationFieldValue, cut.getMotivation());
    } finally {
          deleteTestRecord(KEY_FIELD_VALUE);
    }
  }

  /**
   * Тест установки некорректных значений поля 'Мотивация' на форме редактирования
   * 
   * @param wrongMotivationFieldValue - устанавливаемое значение поля 'Мотивация'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setWrongMotivationOnEdit.method.data")
  @Test(groups = "edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = WrongOptionException.class)
  public void setWrongMotivationOnEdit(String wrongMotivationFieldValue) {
    try {
      createTestRecord(KEY_FIELD_VALUE);
      
      cut.edit(new HashMap<String, String>() {{
        put(KEY_FIELD_WEB_ELEMENT_ID, KEY_FIELD_VALUE);
      }});
      
          cut.setMotivation(wrongMotivationFieldValue);
    } finally {
          deleteTestRecord(KEY_FIELD_VALUE);
    }
  }
  
  /**
   * Тест установки/чтения значения поля 'Закупочная цена' на форме редактирования
   * 
   * @param purchasingPriceNewValue - устанавливаемое значение поля 'Закупочная цена'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setAndGetPurchasingPriceOnCreateAndEdit.method.data")
  @Test(groups = "edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setAndGetPurchasingPriceOnEdit(String purchasingPriceNewValue) {
    
    testSetAndGetTextFieldValueOnEdit(
        GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID,
        purchasingPriceNewValue);
  }
  
  /**
   * Тест установки/чтения значения поля 'Закупочная цена' на форме редактирования
   * 
   * @param purchasingPriceNewValue - устанавливаемое значение поля 'Закупочная цена'
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.setWrongPurchasingPriceOnCreateAndEdit.method.data ")
  @Test(groups = "edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void setWrongPurchasingPriceOnEdit(String purchasingPriceNewValue) {
    
    testSetAndGetTextFieldValueOnEdit(
        cut,
        KEY_FIELD_WEB_ELEMENT_ID,
        KEY_FIELD_VALUE,
        GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID,
        purchasingPriceNewValue,
        false);
  }
  

  /**
   * Тест заполнения формы редактирования
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.fillEditForm.method.data")
  @Test(groups="edit", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void fillEditForm(final String goodsName, String goodsType, String unit, String motivation, String purchasingPrice) {

    try {
      createTestRecord(goodsName);
      
      cut.edit(new HashMap<String, String>() {{
        put(GOODS_GOODS_NAME_TEXT_FIELD_ID, goodsName);
      }});
      
          final String GOODS_PHOTO = "...";      // TODO Поддержать выбор файла
          final String GOODS_SPECIFICATION = "...";  // TODO Поддержать выбор файла
          
          cut.fillEditForm(
              goodsName,
              goodsType,
              unit,
              motivation,
              purchasingPrice,
              GOODS_PHOTO,
              GOODS_SPECIFICATION);
          
          assertEquals(goodsName, cut.getGoodsName());
          assertEquals(goodsType, cut.getGoodsType());
          assertEquals(unit, cut.getUnit());
          assertEquals(motivation, cut.getMotivation());
          assertEquals(purchasingPrice, cut.getPurchasingPrice());
    } finally {
          deleteTestRecord(goodsName);
    }
  }
  
  /**
   * Тест сохранения записи 
   */
  @Test(groups = "edit, create")
  public void save_AlertMessageBoxShouldAppear() {
    cut.setWorkstate(CREATE);
    
        assertEquals(SaveResultEnum.ALERT_MESSAGE_BOX, cut.save());
  }
  
  /**
   * Тест проверки появления popup-окна после нажатия на save при незаполненных полях 
   */
  @Test(groups = "edit, create")
  public void save() {
    cut.setWorkstate(CREATE);
    
        final String GOODS_NAME = "Вино";
        final String GOODS_TYPE = "Продукты питания";
        final String UNIT = "Литры";
        final String MOTIVATION = "Процент с дохода";
        final String PURCHASING_PRICE = "12345.12";
        final String GOODS_PHOTO = "...";      // TODO Поддержать выбор файла
        final String GOODS_SPECIFICATION = "...";  // TODO Поддержать выбор файла
        
        cut.fillCreateForm(
            GOODS_NAME,
            GOODS_TYPE,
            UNIT,
            MOTIVATION,
            PURCHASING_PRICE,
            GOODS_PHOTO,
            GOODS_SPECIFICATION);
    
        assertEquals(SaveResultEnum.STATUS_HAS_CHANGED, cut.save());
  }
  
  @Test
  @Parameters({"KEY_FIELD_VALUE"})
  public void selectItemByIndex(String goodsName) {
        fail("Не реализовано");
  }
  
  @Test
  @Parameters({"KEY_FIELD_VALUE"})
  public void selectItemByKey(String goodsName) {
        fail("Не реализовано");
  }

  /**
   * Тест удаления записи
   * 
   * @param KEY_FIELD_VALUE - значение поля 'Наименование товара', идентифицирующее тестовую запись
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.delete.group.data")
  @Test(groups = "delete", dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile", expectedExceptions = IndexOutOfBoundsException.class)
  public void delete(final String goodsNameFieldValue) {
    try {
      createTestRecord(goodsNameFieldValue);
      
      Map<String, String> key = new HashMap<String, String>() {{put(GOODS_GOODS_NAME_TEXT_FIELD_ID, goodsNameFieldValue);}};
      cut.find(key);
      
      try {
        cut.selectItem(key);
      } catch(IndexOutOfBoundsException ex) {
            fail("Элемент с ключом " + key + " отсутствует в списке");
      }
      
      cut.delete(key);
      
      cut.selectItem(key); // Должно вызвать IndexOutOfBoundsException
    } finally {
          deleteTestRecord(goodsNameFieldValue); // На случай, если cut.delete не сработал
    }
  }
  
  /**
   * Создание тестовой записи
   * 
   * @param KEY_FIELD_VALUE - значение поля 'Наименование товара', идентифицирующее тестовую запись
   */
  protected void createTestRecord(String goodsNameFieldValue) {
    logger.trace("createRecordForTest()");
    cut.setWorkstate(CREATE);
    
        final String GOODS_TYPE = "Продукты питания";
        final String UNIT = "Литры";
        final String MOTIVATION = "Процент с дохода";
        final String PURCHASING_PRICE = "12345.12";
        final String GOODS_PHOTO = "...";      // TODO Поддержать выбор файла
        final String GOODS_SPECIFICATION = "...";  // TODO Поддержать выбор файла
        
        cut.fillCreateForm(
            goodsNameFieldValue,
            GOODS_TYPE,
            UNIT,
            MOTIVATION,
            PURCHASING_PRICE,
            GOODS_PHOTO,
            GOODS_SPECIFICATION);
    
        SaveResultEnum saveResult = cut.save();
        if(saveResult != SaveResultEnum.STATUS_HAS_CHANGED) {
            if(saveResult == SaveResultEnum.ERROR_MESSAGE_BOX) { // TODO Предполагаем, что запись уже существует, но нужно всё-таки убедиться
            logger.warn("createRecordForTest(): Запись '" + goodsNameFieldValue +"' уже существует");
            cut.clickButton(ERROR_MESSAGE_BOX_OK_BUTTON_ID);
            } else {
                fail("Ошибка создания тестовой записи");
            }
        }
  }
  
  
  private void deleteTestRecord(String testFieldValue) {
    super.deleteTestRecord(GOODS_GOODS_NAME_TEXT_FIELD_ID, testFieldValue);
  }

  private void testSetAndGetTextFieldValueOnEdit(String testFieldId, String testFieldNewValue) {
    testSetAndGetTextFieldValueOnEdit(
        cut,
        GOODS_GOODS_NAME_TEXT_FIELD_ID,
        KEY_FIELD_VALUE,
        testFieldId,
        testFieldNewValue,
        true);
  }
  
  private void testSetAndGetComboBoxFieldValueOnEdit(String testFieldId, String testFieldNewValue) {
    testSetAndGetComboBoxFieldValueOnEdit(
        cut,
        GOODS_GOODS_NAME_TEXT_FIELD_ID,
        KEY_FIELD_VALUE,
        testFieldId,
        testFieldNewValue);
  }
  
  /*
   * Набросок для исполнения скрипта через последовательность вызовов тестового метода
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/goods/auto/GoodsAutoTest.jepRiaCommandsTest.jrc ")
  @Test(groups = "jrc", dataProviderClass = JepFileDataProvider.class, dataProvider="JepRiaCommands")
  public void jepRiaCommandsTest(String command, List<String> parameters) {
    fail("Не реализовано");
  }
}
