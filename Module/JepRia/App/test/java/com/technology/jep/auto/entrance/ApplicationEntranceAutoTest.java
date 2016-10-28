
/*package com.technology.jep.auto.entrance;

import static com.technology.jep.jepria.auto.JepAutoProperties.PASSWORD_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.USERNAME_KEY;
import static com.technology.jep.jepria.auto.JepAutoProperties.get;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.entrance.ApplicationEntranceAppAuto;
import com.technology.jep.jepria.auto.entrance.EntranceAuto;

public class ApplicationEntranceAutoTest {

  private ApplicationEntranceAppAuto automationManager;
  private EntranceAuto cut;
  
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
    
    automationManager = new ApplicationEntranceAppAuto(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password);
    automationManager.start(baseUrl);
    cut = automationManager.getEntranceAuto();
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
  
  @Test(groups = {"broken"} )
  public void loginShouldBeRepeatable() {
    AssertJUnit.assertFalse(cut.isLoggedIn());
    cut.login(get(USERNAME_KEY), get(PASSWORD_KEY));
    AssertJUnit.assertTrue(cut.isLoggedIn());
    cut.login(get(USERNAME_KEY), get(PASSWORD_KEY));
    AssertJUnit.assertTrue(cut.isLoggedIn());
    cut.login(get(USERNAME_KEY), get(PASSWORD_KEY));
    AssertJUnit.assertTrue(cut.isLoggedIn());
  }
  
//  @Test(dependsOnMethods = {"login"})
  @Test
  public void testIsLoggedIn() {
      cut.login(get(USERNAME_KEY), get(PASSWORD_KEY));
    AssertJUnit.assertTrue(cut.isLoggedIn());
    cut.logout();
    AssertJUnit.assertFalse(cut.isLoggedIn());
  }

//  TODO переместить
//  @Test(groups = {"broken"} )
//  public void Login_ShouldObtainUserData() {
//    AssertJUnit.assertFalse(cut.isLoggedIn());
//    AssertJUnit.assertNull(cut.getLoggedUser());
//    
//    cut.login(USERNAME_KEY, PASSWORD_KEY);
//    
//    User user = cut.getLoggedUser();
//    AssertJUnit.assertNotNull(user);
//    AssertJUnit.assertEquals(USERNAME_KEY, user.getUsername());
//    AssertJUnit.assertNotNull(user.getRoles());
//  }

//  TODO переместить
//  @Test(groups = {"broken"} )
//  public void testUserPresenceAfterLogin() {
//    cut.login(USERNAME_KEY, PASSWORD_KEY);
//    AssertJUnit.assertNotNull(cut.getLoggedUser());
//  }

//  TODO переместить
//  @Test(groups = {"broken"}, dependsOnMethods = {"login"})
//  public void testGetUser() {
//    AssertJUnit.fail("NotImplemented");
//  }

  @Test
  public void logout() {
      cut.login(get(USERNAME_KEY), get(PASSWORD_KEY));
    AssertJUnit.assertTrue(cut.isLoggedIn());
    cut.logout();
    AssertJUnit.assertFalse(cut.isLoggedIn());
  }
}
*/