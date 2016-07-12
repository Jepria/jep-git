package com.technology.jep.jepriashowcase.auto;

import org.apache.log4j.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class JepRiaShowcaseAutoTest {
  private static Logger logger = Logger.getLogger(JepRiaShowcaseAutoTest.class.getName());

  private JepRiaShowcaseAutoImpl cut;

  @Parameters({"baseUrl", "browserName", "browserVersion", "browserPlatform", "browserPath", "driverPath", "jepriaVersion", "username", "password"})
  @BeforeMethod
  public void setUp(String baseUrl,
      String browserName,
      @Optional("fake") String browserVersion,
      @Optional("fake") String browserPlatform,
      String browserPath,
      String driverPath,
      String jepriaVersion,
      String username,
      String password) {
    logger.info(this.getClass() + ".setUp(" + baseUrl + ")");
    
    cut = new JepRiaShowcaseAutoImpl(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password);
    cut.start(baseUrl);
  }
  
  @AfterMethod
  public void tearDown() {
    cut.stop();
  }

  @Test
  public void testCustomAutoPresence() {
    AssertJUnit.assertNotNull(cut.getCustomAuto());
  }

  @Test
  public void testCustomAutoReadiness() {
    AssertJUnit.assertTrue(cut.getCustomAuto().isReady());
  }

  @Test
  public void testEntranceAutoPresence() {
    AssertJUnit.assertNotNull(cut.getEntranceAuto());
  }
}
