package com.technology.jep.jepria.auto.entrance;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.entrance.pages.ApplicationEntrancePageManager;
import com.technology.jep.jepria.client.AutomationConstant;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;

public class ApplicationEntranceAuto<A extends EntranceAppAuto, P extends ApplicationEntrancePageManager>
	extends AutoBaseImpl<A, P> implements EntranceAuto {

	/*
	 * Константы для оптимизации ожидания реакции на login/logout
	 * Чтобы ожидание появления приложения/страницы логин можно было отложить 
	 */
    private static final int LOGIN_LAST_ENTRANCE_OPERATION = 1;
    private static final int LOGOUT_LAST_ENTRANCE_OPERATION = 2;
	private int lastEntranceOperation;

	public ApplicationEntranceAuto(A app, P pageManager) {
        super(app, pageManager);
    }

	@Override
	public void login(String username, String password) {
		if(lastEntranceOperation != LOGIN_LAST_ENTRANCE_OPERATION) {
			pages.getLoginPage()
	        .ensurePageLoaded()
	        .setUsername(username)
	        .setPassword(password)
	        .doLogin();
	        
	        lastEntranceOperation = LOGIN_LAST_ENTRANCE_OPERATION;
		}
	}


	@Override
	public boolean isLoggedIn() {
		boolean result = false;
		switch (lastEntranceOperation) {
		case LOGIN_LAST_ENTRANCE_OPERATION:
			try {
				pages
					.getApplicationPage()
					.ensurePageLoaded();
				result = true;
	        } catch (NoSuchElementException e) {
	            System.out.println("[NoSuchElementException] login page not loaded, " + e.toString());
				result = false; 
	        }
			break;
		case LOGOUT_LAST_ENTRANCE_OPERATION:
			try {
				pages.getLoginPage()
				.ensurePageLoaded();
	        } catch (NoSuchElementException e) {
	            System.out.println("[NoSuchElementException] login page not loaded, " + e.toString());
				result = true; 
	        }
			result = false;
			break;
		default:
			// Проверка первого входа на уже залогиненную страницу
			try {
				WebElement usernameField = applicationManager.getWebDriver().findElement(By.id(AutomationConstant.LOGGED_IN_USER_ID));
				result = usernameField.isDisplayed(); 
			} catch (Exception ex) {
				result = false;
			}
			
			break;
		}		
			
		return result;
	}

	@Override
	public void logout() {
		pages
			.getApplicationPage()
        	.ensurePageLoaded()
//        	.getContent()
        	.clickLogoutButton();
        
        lastEntranceOperation = LOGOUT_LAST_ENTRANCE_OPERATION;
	}

	@Override
	public boolean isReady() {
		throw new NotImplementedYetException();
	}
	
}
