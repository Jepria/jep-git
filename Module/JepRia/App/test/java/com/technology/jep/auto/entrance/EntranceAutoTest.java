/*package com.technology.jep.auto.entrance;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.entrance.ApplicationEntranceAppAuto;

public class EntranceAutoTest {
  private ApplicationEntranceAppAuto cut;
  
  @Parameters({"baseUrl", "browserName", "browserVersion", "browserPlatform", "browserPath", "driverPath", "jepriaVersion", "username", "password"})
  @BeforeMethod
  public void setUp(String baseUrl,
      String browserName,
      String browserVersion,
      String browserPlatform,
      String browserPath,
      String driverPath,
      String jepriaVersion,
      String username,
      String password) {
    cut = new ApplicationEntranceAppAuto(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password);
    cut.start(baseUrl);
  }
  
  @AfterMethod
  public void tearDown() {
    cut.stop();
  }
  
  @Test
  public void testEntranceAutoPresence() {
    AssertJUnit.assertNotNull(cut.getEntranceAuto());
  }
}
*/