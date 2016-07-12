package com.technology.jep.jepriashowcase.custom.auto;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static com.technology.jep.jepria.client.AutomationConstant.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.technology.jep.jepria.auto.entrance.AutoBaseImpl;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcasePageManager;
import com.technology.jep.jepriashowcase.goods.auto.GoodsAuto;


public class CustomAutoImpl<A extends JepRiaShowcaseAuto> extends AutoBaseImpl<A, JepRiaShowcasePageManager> implements CustomAuto {
    private static final int LOGIN_LAST_ENTRANCE_OPERATION = 1;
    private static final int LOGOUT_LAST_ENTRANCE_OPERATION = 2;
  private int lastEntranceOperation = LOGOUT_LAST_ENTRANCE_OPERATION;

  public CustomAutoImpl(A jepRiaShowcaseAuto, JepRiaShowcasePageManager pageManager) {
    super(jepRiaShowcaseAuto, pageManager);
  }

  @Override
  public void login(String username, String password) {
    pages.customPage
    .ensurePageLoaded()
    .setUsername(username)
        .setPassword(password)
    .doLogin();
    
        lastEntranceOperation = LOGIN_LAST_ENTRANCE_OPERATION;
  }

  @Override
  public void logout() {
    pages.customPage.ensurePageLoaded();
    
    getWait().until(visibilityOfElementLocated(By.id(ENTRANCE_PANEL_LOGOUT_BUTTON_ID)));
    
    pages.customPage.exitButton.click();
    
    lastEntranceOperation = LOGOUT_LAST_ENTRANCE_OPERATION;
  }

  @Override
  public boolean isLoggedIn() {
    boolean result = false;
    switch (lastEntranceOperation) {
    case LOGOUT_LAST_ENTRANCE_OPERATION:
      getWait().until(presenceOfElementLocated(By.id(LOGIN_BUTTON_ID)));
      break;
      
    case LOGIN_LAST_ENTRANCE_OPERATION:
      pages.customPage.ensurePageLoaded();
      
      result = waitForDisplay(By.id(LOGGED_IN_USER_ID));
      
      break;
    default:
      result = false;
      break;
    }    
    
    return result;
  }

  private boolean waitForDisplay(final By locator) {
    boolean result = false;
    try {
      getWait().until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(locator).isDisplayed();
            }
        });
        
        result = true;
        } catch(Throwable th) {
          result = false;
        }
    
    return result;
  }

  @Override
  public GoodsAuto openFullScreenModules() {
    pages.customPage.ensurePageLoaded();
    assert(isLoggedIn());
    pages.customPage.fullScreenModulesButton.click();
    return this.applicationManager.getGoodsAuto(true);
  }

//  @Override
//  public boolean isFullScreenModulesReady() {
//    pages.fullScreenModulesPage.ensurePageLoaded();
//    return true;
//  }

  @Override
  public boolean isReady() {
    pages.customPage.ensurePageLoaded();

    return true;
  }
}
