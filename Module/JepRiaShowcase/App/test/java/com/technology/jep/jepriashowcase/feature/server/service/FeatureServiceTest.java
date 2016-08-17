package com.technology.jep.jepriashowcase.feature.server.service;

import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DESCRIPTION;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME_EN;
import static org.junit.Assert.assertEquals;

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
import com.technology.jep.jepriashowcase.feature.shared.service.FeatureService;

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
    testRecord.set(FEATURE_NAME, "FEATURE_NAME");
    testRecord.set(FEATURE_NAME_EN, "FEATURE_NAME_EN");
    testRecord.set(DESCRIPTION, "DESCRIPTION");   // Clob
    FindConfig createConfig = new FindConfig(testRecord);
    JepRecord resultRecord = service.create(createConfig);
    
    assertEquals(testRecord, resultRecord); // Equals работает "неправильно" - сравнивает только по primary key
//    fail("!!!");
  }
}
