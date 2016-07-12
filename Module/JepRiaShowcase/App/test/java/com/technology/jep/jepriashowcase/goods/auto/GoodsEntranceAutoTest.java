package com.technology.jep.jepriashowcase.goods.auto;

import static com.technology.jep.jepria.auto.JepAutoProperties.PASSWORD_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.USERNAME_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.get;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAutoImpl;

public class GoodsEntranceAutoTest {
  private JepRiaShowcaseAutoImpl automationManager;
  private GoodsAuto cut;

  @Parameters({
    "baseUrl",
    "browserName",
    "browserVersion",
    "browserPlatform",
    "browserPath",
    "driverPath",
    "jepriaVersion",
    "username",
    "password"})
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
    
    automationManager = new JepRiaShowcaseAutoImpl(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password);
    automationManager.start(baseUrl);
    
      cut = automationManager.getGoodsAuto(true); // TODO Вернуть оптимизацию
  }
  
  @AfterMethod
  public void tearDown() {
    automationManager.stop();
  }

  @Test
  public void login() {
    AssertJUnit.assertFalse(cut.isLoggedIn());
    cut.login(get(USERNAME_KEY), get(PASSWORD_KEY));
    AssertJUnit.assertTrue(cut.isLoggedIn());
  }
  
  @Test
  public void isLoggedIn() {
      cut.login(get(USERNAME_KEY), get(PASSWORD_KEY)); // TODO Оптимизировать
    AssertJUnit.assertTrue(cut.isLoggedIn());
    cut.logout();
    AssertJUnit.assertFalse(cut.isLoggedIn());
  }

  @Test
  public void logout() {
      cut.login(get(USERNAME_KEY), get(PASSWORD_KEY)); // TODO Оптимизировать
    AssertJUnit.assertTrue(cut.isLoggedIn());
    cut.logout();
    AssertJUnit.assertFalse(cut.isLoggedIn());
  }
}
