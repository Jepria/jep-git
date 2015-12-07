package com.technology.jep.jepria.auto;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.AutomationConstant.ALERT_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.AutomationConstant.CONFIRM_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.AutomationConstant.CONFIRM_MESSAGE_BOX_YES_BUTTON_ID;
import static com.technology.jep.jepria.client.AutomationConstant.DETAIL_FORM_COMBOBOX_MENU_ITEM_PREFIX;
import static com.technology.jep.jepria.client.AutomationConstant.ERROR_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.AutomationConstant.TOOLBAR_ADD_BUTTON_ID;
import static com.technology.jep.jepria.client.AutomationConstant.TOOLBAR_DELETE_BUTTON_ID;
import static com.technology.jep.jepria.client.AutomationConstant.TOOLBAR_EDIT_BUTTON_ID;
import static com.technology.jep.jepria.client.AutomationConstant.TOOLBAR_FIND_BUTTON_ID;
import static com.technology.jep.jepria.client.AutomationConstant.TOOLBAR_LIST_BUTTON_ID;
import static com.technology.jep.jepria.client.AutomationConstant.TOOLBAR_SAVE_BUTTON_ID;
import static com.technology.jep.jepria.client.AutomationConstant.TOOLBAR_SEARCH_BUTTON_ID;
import static com.technology.jep.jepria.client.AutomationConstant.TOOLBAR_VIEW_DETAILS_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.technology.jep.jepria.auto.conditions.ConditionChecker;
import com.technology.jep.jepria.auto.conditions.DisplayChecker;
import com.technology.jep.jepria.auto.conditions.ExpectedConditions;
import com.technology.jep.jepria.auto.conditions.TextChangeChecker;
import com.technology.jep.jepria.auto.entrance.ApplicationEntranceAuto;
import com.technology.jep.jepria.auto.entrance.EntranceAppAuto;
import com.technology.jep.jepria.auto.entrance.pages.JepRiaApplicationPageManager;
import com.technology.jep.jepria.auto.exceptions.NotExpectedException;
import com.technology.jep.jepria.auto.exceptions.WrongOptionException;
import com.technology.jep.jepria.auto.widget.field.Field;
import com.technology.jep.jepria.auto.widget.statusbar.StatusBar;
import com.technology.jep.jepria.auto.widget.statusbar.StatusBarImpl;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;
import com.technology.jep.jepria.shared.exceptions.UnsupportedException;

/**
 * Реализация JepRiaModuleAuto
 */
public class JepRiaModuleAutoImpl<A extends EntranceAppAuto, P extends JepRiaApplicationPageManager> extends ApplicationEntranceAuto<A, P> implements JepRiaModuleAuto {
	private static Logger logger = Logger.getLogger(JepRiaModuleAutoImpl.class.getName());
	private WorkstateEnum currentWorkstate;
	private StatusBar statusBar;

	public JepRiaModuleAutoImpl(A app, P pageManager) {
		super(app, pageManager);
	}
	
	public JepRiaModuleAutoImpl(A app, P pageManager, WorkstateEnum initialWorkstate) {
		this(app, pageManager);
		currentWorkstate = initialWorkstate;
	}

	@Override
	public StatusBar getStatusBar() {
		if(statusBar == null) {
			statusBar = new StatusBarImpl<P>(pages);
		}
		
		return statusBar;
	}
	
	public void clickButton(String buttonId) {
		getWait().until(elementToBeClickable(By.id(buttonId)));
		this.pages.getApplicationPage().getWebDriver().findElement(By.id(buttonId)).click();
	}
	
	@Override
	public void find(Map<String, String> template) {
		if(getCurrentWorkstate() != VIEW_LIST) {
			setWorkstate(SEARCH);
			fillFields(template);
			
			String statusBarTextBefore = getStatusBar().getText();
			clickButton(getToolbarButtonId(VIEW_LIST));
	        waitTextToBeChanged(getStatusBar(), statusBarTextBefore);
	        
	        setCurrentWorkstate(VIEW_LIST);
		}
	}
	
	@Override
	public void find() {
		pages.getApplicationPage().ensurePageLoaded();
		
		getWait().until(elementToBeClickable(By.id(TOOLBAR_FIND_BUTTON_ID)));
		
		pages.getApplicationPage().findButton.click();
	}

	@Override
	public void setWorkstate(WorkstateEnum workstateTo) {
		pages.getApplicationPage().ensurePageLoaded();
		
		if(!getCurrentWorkstate().equals(workstateTo))  {
			String toolbarButtonId = getToolbarButtonId(workstateTo);
			if(toolbarButtonId != null) {
				
				String statusBarTextBefore = getStatusBar().getText();
				clickButton(toolbarButtonId);
		        waitTextToBeChanged(getStatusBar(), statusBarTextBefore);
		        
		        setCurrentWorkstate(workstateTo);
			} else {
				throw new UnsupportedException("Wrong transition: " + this.getCurrentWorkstate() + "->" + workstateTo);
			}
		}
	}

	@Override
	public void edit(Map<String, String> template) {
		find(template);
		
        selectItem(0);
        
		String toolbarButtonId = getToolbarButtonId(EDIT);

		String statusBarTextBefore = getStatusBar().getText();
		clickButton(toolbarButtonId);
        waitTextToBeChanged(getStatusBar(), statusBarTextBefore);
        
        setCurrentWorkstate(EDIT);
	}

	@Override
	public void delete(Map<String, String> key) {
		try {
	        selectItem(key);
	        
			String statusBarTextBefore = getStatusBar().getText();
			clickButton(TOOLBAR_DELETE_BUTTON_ID);
			
			assert checkMessageBox(CONFIRM_MESSAGEBOX_ID);
			
			clickButton(CONFIRM_MESSAGE_BOX_YES_BUTTON_ID);
			
	        waitTextToBeChanged(getStatusBar(), statusBarTextBefore);
	        
	        setCurrentWorkstate(VIEW_LIST);
		} catch(IndexOutOfBoundsException ex) {
			// Нормально для случая отсутствия записи с ключом key
		}
	}

	
	public void selectItem(Map<String, String> key) {
		find(key);
        selectItem(0);
	}

	public void selectItem(int index) {
		assert getCurrentWorkstate() == VIEW_LIST;

		WebDriver wd = pages.getApplicationPage().getWebDriver();
		int size = 0;
		int i = 0;
//		List<WebElement> elements = wd.findElements(By.id(LIST_FORM_GRID_ROW_ID)); От этого пока отказались 
		List<WebElement> elements = wd.findElements(By.className("GC2PVC0CPI")); // На GWT 2.6.1 работает
		
		size = elements.size();
		while(true) { // TODO Избавиться от sleep
			if(size > 0 || i++ == 20 ) break;
			sleep(100);
//			elements = wd.findElements(By.id(LIST_FORM_GRID_ROW_ID));  От этого пока отказались
			elements = wd.findElements(By.className("GC2PVC0CPI"));  // На GWT 2.6.1 работает 
			size = elements.size();
		}
		if(index  < size) {
			WebElement item = elements.get(index);
			getWait().until(elementToBeClickable(item));
			new Actions(wd).clickAndHold(item).release().perform();
	        setCurrentWorkstate(SELECTED);
		} else {
			throw new IndexOutOfBoundsException("index = " + index + ", size = " + size);
		}
	}

	private void sleep(int msc) {
		try {
			Thread.sleep(msc);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void fillFields(Map<String, String> fieldMap) {
		for(String fieldName: fieldMap.keySet()) {
			setFieldValue(fieldName, fieldMap.get(fieldName));
		}
	}

	@Override
	public void setFieldValue(String fieldInputId, String value) {
		pages.getApplicationPage().ensurePageLoaded();
		
		WebElement fieldInput = pages.getApplicationPage().getWebDriver().findElement(By.id(fieldInputId)); 
		getWait().until(elementToBeClickable(fieldInput));
		
		String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE; 
		fieldInput.sendKeys(del + value);
	}

	@Override
	public String getFieldValue(String fieldInputId) {
		pages.getApplicationPage().ensurePageLoaded();
		
		WebElement fieldInput = pages.getApplicationPage().getWebDriver().findElement(By.id(fieldInputId)); 
		return fieldInput.getAttribute("value");
	}

	//TODO унифицировать простые и сложные поля через get/set-FieldValue() - Нужна иерархия полей ! 
	@Override
	public void selectComboBoxMenuItem(String comboBoxFieldInputId, String menuItemText) { // TODO Поддержать локализацию
		String comboBoxButtonXPath = "//input[@id='"
				+ comboBoxFieldInputId
				+ "']/following-sibling::img";
		
		WebElement comboBoxButton = pages.getApplicationPage().getWebDriver().findElement(By.xpath(comboBoxButtonXPath));
		comboBoxButton.click();
		
		WebElement comboBoxMenuItem;
		// TODO Нормально обработать ситуацию установки некорректных значений (сейчас сваливаемся на ожидание полного timeout с последующим TimeoutException)
		try {
			comboBoxMenuItem = getWait().until(presenceOfElementLocated(By.id(DETAIL_FORM_COMBOBOX_MENU_ITEM_PREFIX + menuItemText)));
		} catch(Exception ex) {
			throw new WrongOptionException("Wrong combobox option", ex);			
		}
		
		getWait().until(elementToBeClickable(comboBoxMenuItem));
		comboBoxMenuItem.click();
	}

	@Override
	public SaveResultEnum save() {
		SaveResultEnum result = null;
		pages.getApplicationPage().ensurePageLoaded();
		
		getWait().until(elementToBeClickable(By.id(TOOLBAR_SAVE_BUTTON_ID)));
		
		pages.getApplicationPage().saveButton.click();

		WebDriver wd = pages.getApplicationPage().getWebDriver();
		
		ConditionChecker conditionChecker = new WebDriverWait(wd, 5000).until(
				ExpectedConditions.atLeastOneOfConditionIsSatisfied(
						new DisplayChecker(wd, ALERT_MESSAGEBOX_ID),
						new DisplayChecker(wd, ERROR_MESSAGEBOX_ID),
						new TextChangeChecker(getStatusBar())
		        )
		);		

		if(conditionChecker instanceof TextChangeChecker) {
			result = SaveResultEnum.STATUS_HAS_CHANGED;
			this.setCurrentWorkstate(VIEW_DETAILS);
		} else if(ALERT_MESSAGEBOX_ID.equals(((DisplayChecker)conditionChecker).getId())) {
			result = SaveResultEnum.ALERT_MESSAGE_BOX;
		} else if(ERROR_MESSAGEBOX_ID.equals(((DisplayChecker)conditionChecker).getId())) {
			result = SaveResultEnum.ERROR_MESSAGE_BOX;
		} else {
			throw new NotExpectedException("Save error");
		}
		
		return result;
	}
	
	public boolean checkMessageBox(String messageBoxId) {
		return isDisplayed(messageBoxId);
	}

	public WorkstateEnum getCurrentWorkstate() {
		if(currentWorkstate == null) {
			currentWorkstate = SEARCH;
		}
		return currentWorkstate;
	}

	public void setCurrentWorkstate(WorkstateEnum currentWorkstate) {
		this.currentWorkstate = currentWorkstate;
	}

	/**
	 * Получение кнопки toolbar для перехода в заданное состояние
	 * @param workstate
	 * @return id кнопки Toolbar
	 */
	protected String getToolbarButtonId(WorkstateEnum workstate) {
		String toolbarButtonId = null;
		if(Util.isWorkstateTransitionAcceptable(this.getCurrentWorkstate(), workstate)) { // Проверка возможности перехода (во избежание "бесконечного ожидания")
			switch(workstate) {
			case CREATE:
				toolbarButtonId = TOOLBAR_ADD_BUTTON_ID;
				break;
			case EDIT:
				toolbarButtonId = TOOLBAR_EDIT_BUTTON_ID;
				break;
			case SEARCH:
				toolbarButtonId = TOOLBAR_SEARCH_BUTTON_ID;
				break;
			case SELECTED:
				// TODO Что здесь делать ?
				toolbarButtonId = TOOLBAR_SEARCH_BUTTON_ID;
				break;
			case VIEW_DETAILS:
				toolbarButtonId = TOOLBAR_VIEW_DETAILS_BUTTON_ID;
				break;
			case VIEW_LIST:
				toolbarButtonId = currentWorkstate == SEARCH ? TOOLBAR_FIND_BUTTON_ID : TOOLBAR_LIST_BUTTON_ID;
				break;
			}
		}
		
		return toolbarButtonId;
	}
	
	
	private boolean isDisplayed(String id) {
		try {
			pages.getApplicationPage().getWebDriver().findElement(By.id(id));
			return true;
		} catch(NoSuchElementException ex) {
			return false;
		}
	}

	@Override
	public Field getField(String fieldId) {
		throw new NotImplementedYetException();
	}

	@Override
	public void edit(Map<String, String> key, RecordSelector recordSelector) {
		throw new NotImplementedYetException();
	}

	@Override
	public void edit(String idFieldName, String id) {
		throw new NotImplementedYetException();
	}
}
