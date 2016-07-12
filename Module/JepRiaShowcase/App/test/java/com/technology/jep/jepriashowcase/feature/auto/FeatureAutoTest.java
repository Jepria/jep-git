package com.technology.jep.jepriashowcase.feature.auto;

import static com.technology.jep.jepria.client.AutomationConstant.CONFIRM_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.AutomationConstant.CONFIRM_MESSAGE_BOX_YES_BUTTON_ID;
import static com.technology.jep.jepria.client.AutomationConstant.ERROR_MESSAGE_BOX_OK_BUTTON_ID;
import static com.technology.jep.jepria.client.AutomationConstant.TOOLBAR_DELETE_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;

import org.apache.log4j.Logger;
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
      String driverPath,
      String jepriaVersion,
      String username,
      String password) {
    
    return new JepRiaShowcaseAutoImpl(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password);
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
    cut.fillCreateForm(featureName, "featureNameEn_"+random, null);
    
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
//    // Ждем перехода на форму просмотра после успешного создания
//    cut.waitTextToBeChanged(cut.getStatusBar(), Util.getResourceString("workstate.viewDetails"));
    
    // Проверяем, что осуществился переход на форму просмотра после создания
     assertEquals(
         Util.getResourceString("workstate.viewDetails"),
         cut.getStatusBar().getText());
     
     // Проверяем, что поля созданной записи имеют такие же значения, как и те, которыми мы их заполнили
    assertEquals(featureName, cut.getFeatureName());
    assertEquals(featureNameEn, cut.getFeatureNameEn());
    assertEquals(description, cut.getDescription());
  }
  
  /**
   * Тест операции поиска по шаблону
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/feature/auto/form.search.data")
  @Test(groups={"find"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void findByTemplate(String featureId, String featureName, String featureNameEn, String fromDateIns, String toDateIns, String maxRowCount) {
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
        fromDateIns,
        toDateIns,
        maxRowCount);
    
    // Проверяем, что поля заполненны именно теми значениями, которыми мы их заполнили
    assertEquals(featureId, cut.getFeatureId());
    assertEquals(featureName, cut.getFeatureName());
    assertEquals(featureNameEn, cut.getFeatureNameEn());
    assertEquals(fromDateIns, cut.getFromDateIns());
    assertEquals(toDateIns, cut.getToDateIns());
    assertEquals(maxRowCount, cut.getMaxRowCount());
      
    // Осуществляем поиск
    cut.setWorkstate(VIEW_LIST);
    
    // Проверяем, что осуществился переход на форму списка после поиска
     assertEquals(
         Util.getResourceString("workstate.viewList"),
         cut.getStatusBar().getText());
     
     // Проверяем, что данные в списке имеют значения, соответствующие поисковому шаблону
     // TODO obtain data from list cells and assert they are equal with original
  }

  //TODO restore, consider fields in data provider
  /**
   * Тест удаления записи
   * 
   * @param featureName - значение ключевого поля featureName, идентифицирующего тестовую запись
   */
  @Test(groups = "delete")
  public void delete() {
    createTestRecord(KEY_FIELD_VALUE);
    
    // Проверяем, что мы находимся на форме просмотра после создания
     assertEquals(
         Util.getResourceString("workstate.viewDetails"),
         cut.getStatusBar().getText());
     
     // Запоминаем feature_id созданной записи, чтобы далее проверить корректность удаления
     final String feature_id = cut.getFeatureId();
     
     // Нажимаем на кнопку удаление, подтверждаем удаление
     cut.clickButton(TOOLBAR_DELETE_BUTTON_ID);
     assertEquals(true, cut.checkMessageBox(CONFIRM_MESSAGEBOX_ID));
    cut.clickButton(CONFIRM_MESSAGE_BOX_YES_BUTTON_ID);
    
    // Проверяем корректность удаления: пытаемся найти запись по сохраненному feature_id
    cut.setFeatureId(feature_id);
    // Осуществляем поиск
    cut.find();
    //TODO убедиться что список пуст
  }
       
  //TODO create a test method that deletes a particular record of a list 
  
  /**
   * Собственно тест редактирования
   */
  @DataProviderArguments("filePath=test/resources/com/technology/jep/jepriashowcase/feature/auto/form.edit.data")
  @Test(groups={"edit"}, dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile")
  public void edit(String featureName, String featureNameEn) {
    // создаем запись, которую собираемся редактировать
    createTestRecord("ABCDE");
    
    // переходим в состояние редактирования
    cut.setWorkstate(EDIT);
    
    // Проверяем, что осуществился переход на форму редактирования
    assertEquals(
        Util.getResourceString("workstate.edit"),
        cut.getStatusBar().getText());
    
    // Редактируем поля
    cut.fillEditForm(
        featureName,
        featureNameEn);
    
    // Проверяем, что поля заполнены именно теми значениями, которыми мы их заполнили
    assertEquals(featureName, cut.getFeatureName());
    assertEquals(featureNameEn, cut.getFeatureNameEn());
    
    // Осуществляем сохранение записи
    SaveResultEnum saveResult = cut.save();
    assertEquals(SaveResultEnum.STATUS_HAS_CHANGED, saveResult);
//    
//    //TODO работает без строчки ниже, однако по хорошему она должна присутствовать, но почему то с ней не работает
////    // Ждем перехода на форму просмотра после успешного создания
////    cut.waitTextToBeChanged(cut.getStatusBar(), Util.getResourceString("workstate.viewDetails"));
//    
    // Проверяем, что осуществился переход на форму просмотра после редактирования
    assertEquals(
        Util.getResourceString("workstate.viewDetails"),
        cut.getStatusBar().getText());
    
    // Проверяем, что поля отредактированной записи имеют такие же значения, как и те, которыми мы их заполнили
    assertEquals(featureName, cut.getFeatureName());
    assertEquals(featureNameEn, cut.getFeatureNameEn());
  }
}
