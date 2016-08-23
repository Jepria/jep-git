package com.technology.jep.jepriashowcase.feature.server.service;

import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

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


  @Test
  public void testCreate() throws ApplicationException {
    JepRecord testRecord = new JepRecord();
    testRecord.set(DESCRIPTION, "DESCRIPTION_VALUE");   // Clob
    testRecord.set(FEATURE_NAME, "FEATURE_NAME_VALUE");
    testRecord.set(FEATURE_NAME_EN, "FEATURE_NAME_EN_VALUE");
    FindConfig createConfig = new FindConfig(testRecord);
    
    JepRecord resultRecord = service.create(createConfig);
    
    assertNotNull(resultRecord);
    assertEquals("DESCRIPTION_VALUE", resultRecord.get(DESCRIPTION));
    assertEquals("FEATURE_NAME_VALUE", resultRecord.get(FEATURE_NAME));
    assertEquals("FEATURE_NAME_EN_VALUE", resultRecord.get(FEATURE_NAME_EN));
    assertNotNull(resultRecord.get(FEATURE_ID));
    assertNotNull(resultRecord.get(DATE_INS));
    assertNotNull(resultRecord.get(OPERATOR_NAME));
  }
  
  @Test
  public void testFind() throws ApplicationException {
    fail("Не реализовано");
  }
  
  @Test
  public void testDelete() throws ApplicationException {
    fail("Не реализовано");
  }
  
  @Test
  public void testUpdate() throws ApplicationException {
    fail("Не реализовано");
  }
}
