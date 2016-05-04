package com.technology.jep.auto.entrance;

import static com.technology.jep.jepria.auto.JepAutoProperties.*;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.entrance.EntranceAuto;
import com.technology.jep.jepria.auto.entrance.NavigationEntranceAuto;

public class NavigationEntranceAutoTest {

	private NavigationEntranceAuto automationManager;
	private EntranceAuto cut;

	@Parameters({"baseUrl", "browserName", "browserVersion", "browserPlatform", "browserPath", "jepriaVersion", "username", "password"})
	@BeforeMethod
	public void setUp(String baseUrl,
			String browserName,
			String browserVersion,
			String browserPlatform,
			String browserPath,
			String jepriaVersion,
			String username,
			String password) {
		automationManager = new  NavigationEntranceAuto(baseUrl, browserName, browserVersion, browserPlatform, browserPath, jepriaVersion, username, password);
		automationManager.start(baseUrl);
		
    	cut = automationManager.getEntranceAuto();
	}
	
	@AfterMethod
	public void tearDown() {
		automationManager.stop();
	}
	
	
	@Test(groups = {"broken"} )
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
	
	@Test(groups = {"broken"} )
	public void testIsLoggedIn() {
    	cut.login(get(USERNAME_KEY), get(PASSWORD_KEY));
		AssertJUnit.assertTrue(cut.isLoggedIn());
		cut.logout();
		AssertJUnit.assertFalse(cut.isLoggedIn());
	}

//	TODO переместить
//	@Test(groups = {"broken"} )
//	public void Login_ShouldObtainUserData() {
//		AssertJUnit.assertFalse(cut.isLoggedIn());
//		AssertJUnit.assertNull(cut.getLoggedUser());
//		
//		cut.login(get(USERNAME_KEY, get(PASSWORD_KEY);
//		
//		User user = cut.getLoggedUser();
//		AssertJUnit.assertNotNull(user);
//		AssertJUnit.assertEquals(USERNAME_KEY, user.getUsername());
//		AssertJUnit.assertNotNull(user.getRoles());
//	}

//	TODO переместить
//	@Test(groups = {"broken"} )
//	public void testUserPresenceAfterLogin() {
//		cut.login(get(USERNAME_KEY, get(PASSWORD_KEY);
//		AssertJUnit.assertNotNull(cut.getLoggedUser());
//	}

//	TODO переместить
//	@Test(groups = {"broken"}, dependsOnMethods = {"login"})
//	public void testGetUser() {
//		AssertJUnit.fail("NotImplemented");
//	}

	@Test(groups = {"broken"} )
	public void logout() {
    	cut.login(get(USERNAME_KEY), get(PASSWORD_KEY));
		AssertJUnit.assertTrue(cut.isLoggedIn());
		cut.logout();
		AssertJUnit.assertFalse(cut.isLoggedIn());
	}
}
