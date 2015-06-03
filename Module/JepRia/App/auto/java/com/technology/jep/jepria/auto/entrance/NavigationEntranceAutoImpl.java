package com.technology.jep.jepria.auto.entrance;

import org.openqa.selenium.NoSuchElementException;

import com.technology.jep.jepria.auto.entrance.pages.NavigationEntrancePageManager;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;

public class NavigationEntranceAutoImpl<A extends EntranceAppAuto>
	extends AutoBaseImpl<A, NavigationEntrancePageManager> implements EntranceAuto {

	/*
	 * Константы для оптимизации ожидания реакции на login/logout
	 * Чтобы ожидание появления приложения/страницы логин можно было отложить 
	 */
//    private static final int UNDEFINED_LAST_ENTRANCE_OPERATION = 0;
    private static final int LOGIN_LAST_ENTRANCE_OPERATION = 1;
    private static final int LOGOUT_LAST_ENTRANCE_OPERATION = 2;
	private int lastEntranceOperation;

	public NavigationEntranceAutoImpl(A app, NavigationEntrancePageManager pageManager) {
        super(app, pageManager);
    }

	@Override
	public void login(String username, String password) {
		
		throw new NotImplementedYetException();
		
//		if(lastEntranceOperation != LOGIN_LAST_ENTRANCE_OPERATION) {
//			pages.loginPage
//	        .ensurePageLoaded()
//	        .setLogin(USERNAME_KEY)
//	        .setPswd(PASSWORD_KEY)
//	        .clickLoginBtn();
//	        
//	        lastEntranceOperation = LOGIN_LAST_ENTRANCE_OPERATION;
////		} else {
////			pages.rfiAppPage.ensurePageLoaded();
//		}
	}

	@Override
	public boolean isLoggedIn() {
		
//		throw new NotImplementedYetException();
		
		boolean result = false;
		switch (lastEntranceOperation) {
		case LOGIN_LAST_ENTRANCE_OPERATION:
			try {
				result = pages.applicationMenuPage
		        		.ensurePageLoaded()
		        		.getContent()
		        		.isChangePasswordNodeDisplayed();
	        } catch (NoSuchElementException e) {
	            System.out.println("[NoSuchElementException] login page not loaded, " + e.toString());
				result = false; 
	        }
			break;
		case LOGOUT_LAST_ENTRANCE_OPERATION:
			try {
				pages.loginPage
				.ensurePageLoaded();
	        } catch (NoSuchElementException e) {
	            System.out.println("[NoSuchElementException] login page not loaded, " + e.toString());
				result = true; 
	        }
			result = false;
			break;
		default:
			result = false;
			break;
		}		
			
		return result;
	}

	@Override
	public void logout() {
		
		throw new NotImplementedYetException();
		
//		pages.applicationMenuPage
//        		.ensurePageLoaded()
//        		.getContent()
//        		.clickLogoutButton();
//        
//        lastEntranceOperation = LOGOUT_LAST_ENTRANCE_OPERATION;
	}

	@Override
	public boolean isReady() {
		throw new NotImplementedYetException();
	}
	
}
