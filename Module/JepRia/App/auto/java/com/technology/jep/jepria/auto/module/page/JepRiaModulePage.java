package com.technology.jep.jepria.auto.module.page;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ALERT_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ENTRANCE_PANEL_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.MODULE_PANEL_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.MODULE_TAB_PANEL_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.STATUSBAR_PANEL_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.STATUSBAR_PANEL_WORKSTATE_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_ADD_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_EDIT_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_FIND_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_LIST_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_PANEL_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_SAVE_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_SEARCH_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_VIEW_DETAILS_BUTTON_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.application.page.JepRiaApplicationPageImpl;
import com.technology.jep.jepria.auto.page.PlainPage;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

/**
 * Базовая страница JepRia-приложения. Содержит основные (общие для всех приложений) элементы:
 * кнопки тулбара, панель вкладок, кнопка выхода и т.д.
 */
public class JepRiaModulePage extends PlainPage {
  
  @SuppressWarnings("unused")
  private static Logger logger = Logger.getLogger(JepRiaApplicationPageImpl.class.getName());

  @FindBy(id = TOOLBAR_PANEL_ID)
  public WebElement toolBarPanel;

  @FindBy(id = TOOLBAR_FIND_BUTTON_ID)
  public WebElement findButton;

  @FindBy(id = MODULE_PANEL_ID)
  public WebElement formPanel;

  @FindBy(id = STATUSBAR_PANEL_ID)
  public WebElement statusBarPanel;


  @FindBy(id = TOOLBAR_ADD_BUTTON_ID)
  public WebElement addButton;

  @FindBy(id = TOOLBAR_EDIT_BUTTON_ID)
  public WebElement editButton;

  @FindBy(id = TOOLBAR_SAVE_BUTTON_ID)
  public WebElement saveButton;

  @FindBy(id = TOOLBAR_VIEW_DETAILS_BUTTON_ID)
  public WebElement viewButton;

  @FindBy(id = TOOLBAR_LIST_BUTTON_ID)
  public WebElement listButton;

  @FindBy(id = TOOLBAR_SEARCH_BUTTON_ID)
  public WebElement searchButton;

  @FindBy(id = ALERT_MESSAGEBOX_ID)
  public WebElement alertMessageBox;
  
  @Override
  public void ensurePageLoaded() {
    super.ensurePageLoaded();
    
    getWait().until(visibilityOfElementLocated(By.id(ENTRANCE_PANEL_ID)));
    getWait().until(visibilityOfElementLocated(By.id(MODULE_TAB_PANEL_ID)));
    getWait().until(visibilityOfElementLocated(By.id(TOOLBAR_PANEL_ID)));
    getWait().until(visibilityOfElementLocated(By.id(MODULE_PANEL_ID)));
    getWait().until(visibilityOfElementLocated(By.id(STATUSBAR_PANEL_ID)));
  }
    
  public WorkstateEnum getWorkstateFromStatusBar() {
    return WorkstateEnum.fromString(
        statusBarPanel.getAttribute(STATUSBAR_PANEL_WORKSTATE_HTML_ATTR));
  }
}
