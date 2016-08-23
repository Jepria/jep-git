package com.technology.jep.jepriashowcase.feature.server.service;

import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DATE_INS;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DESCRIPTION;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_ID;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME_EN;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.OPERATOR_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.gwt.test.GwtModule;
import com.technology.jep.jepria.service.test.DataSourceDef;
import com.technology.jep.jepria.service.test.JepRiaServiceTest;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.load.FindConfig;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepriashowcase.feature.server.dao.Feature;

/**
 * Пример теста сервисного класса JepRia
 */
@GwtModule("com.technology.jep.jepriashowcase.feature.Feature")
public class FeatureServiceTest extends JepRiaServiceTest<Feature> {
  
  private static Logger logger = Logger.getLogger(FeatureServiceTest.class.getName());

  private FeatureServiceImpl service;

  @BeforeClass
  public static void setUpClass() throws Exception {
    prepareDataSources(new ArrayList<DataSourceDef>(Arrays.asList(new DataSourceDef(
        "java:/comp/env/jdbc/ITMDS",
        "jdbc:oracle:thin:@//srvt14.d.t:1521/RFINFOT1",
        "itm",
        "itm"
    ))));
  }  

  @Before
  public void before() {
    service = new FeatureServiceImpl(); 
    prepareServletEnvironment(service);
  }

  @After
  public void after() {
    //logout(); TODO Нужен logoff
    service = null;
  }

   private static final String DESCRIPTION_VALUE = "DESCRIPTION_VALUE";
   private static final String FEATURE_NAME_VALUE = "FEATURE_NAME_VALUE";
   private static final String FEATURE_NAME_EN_VALUE = "FEATURE_NAME_EN_VALUE";

  @Test
  public void testCreate() throws ApplicationException {
    JepRecord testRecord = createRecord(DESCRIPTION_VALUE,
                                        FEATURE_NAME_VALUE,
                                        FEATURE_NAME_EN_VALUE);
    // TODO FindConfig нужно переименовать
    FindConfig createConfig = new FindConfig(testRecord);
    
    JepRecord resultRecord = service.create(createConfig);
    
    assertNotNull(resultRecord);
    assertTrue(JepRiaServiceTest.isFieldValueSubSet(testRecord, resultRecord));
    
    // Из-за некорректного equals поля приходится проверять по отдельности
    assertEquals(DESCRIPTION_VALUE, resultRecord.get(DESCRIPTION));
    assertEquals(FEATURE_NAME_VALUE, resultRecord.get(FEATURE_NAME));
    assertEquals(FEATURE_NAME_EN_VALUE, resultRecord.get(FEATURE_NAME_EN));
    assertNotNull(resultRecord.get(FEATURE_ID));
    assertNotNull(resultRecord.get(DATE_INS));
    assertNotNull(resultRecord.get(OPERATOR_NAME));
  }

  @Test
  public void testFindById() throws ApplicationException {
    JepRecord featureRecord = createRecordInDb(DESCRIPTION_VALUE,
                                               FEATURE_NAME_VALUE,
                                               FEATURE_NAME_EN_VALUE);

    JepRecord templateRecord = new JepRecord();
    templateRecord.set(FEATURE_ID, featureRecord.get(FEATURE_ID));
    
    PagingConfig pagingConfig = new PagingConfig(templateRecord);
    PagingResult<JepRecord> pagingResult = service.find(pagingConfig);
    assertNotNull(pagingResult);
    // Почему в PagingResult Integer size, а не int size ? Этот же вопрос и по другим членам.
    assertEquals((Integer)1, pagingResult.getSize());
    List<JepRecord> records = pagingResult.getData();
    assertEquals(1, records.size());
    JepRecord resultRecord = records.get(0);
    // Из-за некорректного equals поля приходится проверять по отдельности
    assertEquals(DESCRIPTION_VALUE, resultRecord.get(DESCRIPTION));
    assertEquals(FEATURE_NAME_VALUE, resultRecord.get(FEATURE_NAME));
    assertEquals(FEATURE_NAME_EN_VALUE, resultRecord.get(FEATURE_NAME_EN));
    
//    fail("Не реализовано удаление");
  }

  @Test
  public void testDelete() throws ApplicationException {
    fail("Не реализовано");
  }
  
  @Test
  public void testUpdate() throws ApplicationException {
    fail("Не реализовано");
  }
  
  /**
   * Создание записи с заданными параметрами в БД
   */
  private JepRecord createRecordInDb(String description,
                                        String featureName,
                                        String featureNameEn) {
    JepRecord featureRecord = new JepRecord();
    featureRecord.set(DESCRIPTION, description);   // Clob
    featureRecord.set(FEATURE_NAME, featureName);
    featureRecord.set(FEATURE_NAME_EN, featureNameEn);
    
    FindConfig createConfig = new FindConfig(featureRecord);
    JepRecord resultRecord = null;
    try {
      resultRecord = service.create(createConfig);
    } catch (ApplicationException ex) {
      fail("Create Feature record error:" + ex.getMessage());
    }
    
    return resultRecord;
  }
  
  /**
   * Создание записи с заданными полями
   */
  private JepRecord createRecord(String description,
                                 String featureName,
                                 String featureNameEn) {
    JepRecord record = new JepRecord();
    record.set(DESCRIPTION, description);
    record.set(FEATURE_NAME, featureName);
    record.set(FEATURE_NAME_EN, featureNameEn);
    
    return record;
  }
}
