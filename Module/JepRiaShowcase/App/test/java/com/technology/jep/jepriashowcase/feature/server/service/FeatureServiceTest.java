package com.technology.jep.jepriashowcase.feature.server.service;

import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DATE_INS;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DESCRIPTION;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_ID;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME_EN;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.OPERATOR_NAME;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    beforeServiceTest(new FeatureServiceImpl());
  }

  @After
  public void after() {
    afterServiceTest();
  }

  private static final String DESCRIPTION_VALUE = "DESCRIPTION_VALUE";
  private static final String FEATURE_NAME_VALUE = "FEATURE_NAME_VALUE";
  private static final String FEATURE_NAME_EN_VALUE = "FEATURE_NAME_EN_VALUE";

  @Test
  public void testCreate() throws ApplicationException {
    logger.trace("testCreate()");
    JepRecord testRecord = createRecord(getDefaultFieldMap());
    // TODO FindConfig нужно переименовать
    FindConfig createConfig = new FindConfig(testRecord);
      
    JepRecord resultRecord = service.create(createConfig);

    addToClear(createConfig);
    
    assertNotNull(resultRecord);
    assertTrue(isFieldValueSubSet(testRecord, resultRecord));
    assertNotNull(resultRecord.get(FEATURE_ID));
    assertNotNull(resultRecord.get(DATE_INS));
    assertNotNull(resultRecord.get(OPERATOR_NAME));
  }

  @Test
  public void testFindById() throws ApplicationException {
    logger.trace("testFindById()");
    
    JepRecord featureRecord = createRecordInDb(true, getDefaultFieldMap());

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
    
    assertTrue(isFieldValueSubSet(featureRecord, resultRecord));
  }
  
  @Test
  public void testFind() throws ApplicationException {
    logger.trace("testFind()");
    
    String currentTimeInMillis =  "_" + System.currentTimeMillis();
    
    Map<String, String> uniqueFieldMap = new HashMap<String, String>();
    uniqueFieldMap.put(DESCRIPTION, DESCRIPTION_VALUE + currentTimeInMillis);
    uniqueFieldMap.put(FEATURE_NAME, FEATURE_NAME_VALUE + currentTimeInMillis);
    uniqueFieldMap.put(FEATURE_NAME_EN, FEATURE_NAME_EN_VALUE + currentTimeInMillis);

    JepRecord featureRecord = createRecordInDb(uniqueFieldMap);

    JepRecord templateRecord = new JepRecord();
    templateRecord.set(DESCRIPTION, DESCRIPTION_VALUE + currentTimeInMillis);
    templateRecord.set(FEATURE_NAME, FEATURE_NAME_VALUE + currentTimeInMillis);
    templateRecord.set(FEATURE_NAME_EN, FEATURE_NAME_EN_VALUE + currentTimeInMillis);
    PagingConfig pagingConfig = new PagingConfig(templateRecord);
    
    PagingResult<JepRecord> pagingResult = service.find(pagingConfig);
    
    assertNotNull(pagingResult);
    // Почему в PagingResult Integer size, а не int size ? Этот же вопрос и по другим членам.
    assertEquals((Integer)1, pagingResult.getSize());
    List<JepRecord> records = pagingResult.getData();
    assertEquals(1, records.size());
    JepRecord resultRecord = records.get(0);
    
    assertTrue(isFieldValueSubSet(featureRecord, resultRecord));
  }

  @Test
  public void testDelete() throws ApplicationException {
    logger.trace("testDelete()");
    
    JepRecord featureRecord = createRecordInDb(false, getDefaultFieldMap());

    //assertEquals((Integer) 1, findById(featureRecord, FEATURE_ID).getSize());
    
    service.delete(new FindConfig(featureRecord));
    
    assertEquals((Integer) 0, findById(featureRecord, FEATURE_ID).getSize());
  }

  @Test
  public void testUpdate() throws ApplicationException {
    logger.trace("testUpdate()");
    
    JepRecord featureRecord = createRecordInDb(getDefaultFieldMap());
    
    Map<String, String> newFieldMap = new HashMap<String, String>();
    newFieldMap.put(DESCRIPTION, DESCRIPTION_VALUE + "_CHANGED");
    newFieldMap.put(FEATURE_NAME, FEATURE_NAME_VALUE + "_CHANGED");
    newFieldMap.put(FEATURE_NAME_EN, FEATURE_NAME_EN_VALUE + "_CHANGED");
    
    service.update(new FindConfig(updateRecord(featureRecord, newFieldMap)));

    PagingResult<JepRecord> pagingResult = findById(featureRecord, FEATURE_ID);
    assertEquals((Integer) 1, pagingResult.getSize());
    assertEquals(DESCRIPTION_VALUE + "_CHANGED", pagingResult.getData().get(0).get(DESCRIPTION));
    assertEquals(FEATURE_NAME_VALUE + "_CHANGED", pagingResult.getData().get(0).get(FEATURE_NAME));
    assertEquals(FEATURE_NAME_EN_VALUE + "_CHANGED", pagingResult.getData().get(0).get(FEATURE_NAME_EN));
  }

  private Map<String, String> getDefaultFieldMap() {
    Map<String, String> fieldMap = new HashMap<String, String>();
    fieldMap.put(DESCRIPTION, DESCRIPTION_VALUE);
    fieldMap.put(FEATURE_NAME, FEATURE_NAME_VALUE);
    fieldMap.put(FEATURE_NAME_EN, FEATURE_NAME_EN_VALUE);
    
    return fieldMap;
  }
}
