package com.technology.jep.jepria.auto;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ALERT_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.CONFIRM_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.CONFIRM_MESSAGE_BOX_YES_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ERROR_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_BODY_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_HEADER_POPUP_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_HEADER_POPUP_MENU_ITEM_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_HEADER_POPUP_NAVIG_UP_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_HEADER_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_CARD_TYPE_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_CARD_TYPE_VALUE_EDTB;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_CARD_TYPE_VALUE_VIEW;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_COMBO_BOX_FIELD_DROPDOWN_BTN_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_COMBO_BOX_FIELD_MENU_ITEM_INFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_COMBO_BOX_FIELD_POPUP_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_DUAL_LIST_FIELD_LEFTPART_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_DUAL_LIST_FIELD_MENU_ITEM_INFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_DUAL_LIST_FIELD_MOVEALLLEFT_BTN_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_DUAL_LIST_FIELD_MOVERIGHT_BTN_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_FIELD_ALLOW_BLANK_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_FIELD_INPUT_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_LIST_FIELD_CHECKALL_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_LIST_FIELD_ITEM_CHECKBOX_INFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_OPTION_VALUE_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKABLE;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_INFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_ISLEAF_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREE_FIELD_CHECKALL_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_ADD_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_DELETE_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_EDIT_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_FIND_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_LIST_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_SAVE_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_SEARCH_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_VIEW_DETAILS_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
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
import com.technology.jep.jepria.auto.exceptions.AutomationException;
import com.technology.jep.jepria.auto.exceptions.NotExpectedException;
import com.technology.jep.jepria.auto.exceptions.WrongOptionException;
import com.technology.jep.jepria.auto.widget.field.Field;
import com.technology.jep.jepria.auto.widget.statusbar.StatusBar;
import com.technology.jep.jepria.auto.widget.statusbar.StatusBarImpl;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;
import com.technology.jep.jepria.shared.exceptions.UnsupportedException;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Реализация JepRiaModuleAuto
 */
public class JepRiaModuleAutoImpl<A extends EntranceAppAuto, P extends JepRiaApplicationPageManager> extends ApplicationEntranceAuto<A, P>
    implements JepRiaModuleAuto {

  private static final long WEB_DRIVER_TIMEOUT = 5;
  
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
  public void doSearch(Map<String, String> template) {
    if(getCurrentWorkstate() != VIEW_LIST) {
      find();
      fillFields(template);
      
      String statusBarTextBefore = getStatusBar().getText();
      clickButton(getToolbarButtonId(VIEW_LIST));
          waitTextToBeChanged(getStatusBar(), statusBarTextBefore);
          
          setCurrentWorkstate(VIEW_LIST);
    }
  }
  
  @Override
  public void find() {
    setWorkstate(WorkstateEnum.SEARCH);
  }
  
  @Override
  public void doSearch() {
    setWorkstate(WorkstateEnum.VIEW_LIST);
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

    //Метод оставлен для обратной совместимости.
    String gridId = null;
    this.edit(template, gridId);
  }
  
  @Override
  public void edit(Map<String, String> template, String gridId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    doSearch(template);
    
    selectItem(0, gridId);
    
    setWorkstate(EDIT);
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

  @Override
  public void selectItem(Map<String, String> key) {
    doSearch(key);
    selectItem(0);// TODO Уверенно отмечаем "единственный" первый элемент. А если список пуст, или выдалось несколько записей?
  }
  
  @Override
  public void selectItem(int index) {
    selectItem(index, null);
  }
  
  @Override
  public void selectItem(int index, String gridId) {
    
    assert getCurrentWorkstate() == VIEW_LIST;
    
    By gridBodyBy = null;
    if(gridId == null){
      gridBodyBy = By.xpath(String.format("//tbody[contains(@id, '%s')]", GRID_BODY_POSTFIX));
    }else{
      gridBodyBy = By.id(gridId+GRID_BODY_POSTFIX);
    }
    getWait().until(presenceOfElementLocated(gridBodyBy));

    WebElement gridBody = pages.getApplicationPage().getWebDriver().findElement(gridBodyBy);
    
    List<WebElement> gridRows = gridBody.findElements(By.xpath("./tr"));
    
    if (gridRows.size() <= index) {
      throw new IndexOutOfBoundsException("Failed to select the item with index "+index+" in the list, as it contains "+gridRows.size()+" items only.");
    }
    
    getWait().until(elementToBeClickable(gridRows.get(index)));
    gridRows.get(index).click();
    setCurrentWorkstate(SELECTED);
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
  public void setFieldValue(String fieldId, String value) {
    pages.getApplicationPage().ensurePageLoaded();
    
      WebElement fieldInput = pages.getApplicationPage().getWebDriver().findElement(By.id(fieldId + JEP_FIELD_INPUT_POSTFIX)); 
      getWait().until(elementToBeClickable(fieldInput));
      
      String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE; 
      fieldInput.sendKeys(del + value);

  }

  @Override
  public String getFieldValue(String fieldId) {
    pages.getApplicationPage().ensurePageLoaded();
    String value = "";
    
    try{
      WebElement fieldInput = pages.getApplicationPage().getWebDriver().findElement(By.id(fieldId + JEP_FIELD_INPUT_POSTFIX));
      value = fieldInput.getAttribute("value");
    }catch(NoSuchElementException e){
      throw new AutomationException("No element with id - " + fieldId, e);
    }
    
    return value;
  }
  
  @Override
  public void setLargeFieldValue(String fieldId, String pathToFile) {
    pages.getApplicationPage().ensurePageLoaded();
    
    WebElement fieldInput = pages.getApplicationPage().getWebDriver().findElement(By.id(fieldId + JEP_FIELD_INPUT_POSTFIX)); 
    getWait().until(elementToBeClickable(fieldInput));
    
    fieldInput.sendKeys(pathToFile);
  }
  
  @Override
  public String[] getDualListFieldValues(String jepDualListFieldId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // Получаем список всех опций внутри INPUT'а заданного поля (INPUT это правый список)
      List<WebElement> options = pages.getApplicationPage().getWebDriver().findElements(By.xpath(
          String.format("//*[@id='%s']//option",
              jepDualListFieldId + JEP_FIELD_INPUT_POSTFIX)));
      
      List<String> ret = new ArrayList<String>();
      
      // Проходим по полученному списку, и, для каждой опции, получаем ее option-value
      for (WebElement option: options) { 
          ret.add(option.getAttribute(JEP_OPTION_VALUE_HTML_ATTR));
      }
      return ret.toArray(new String[ret.size()]);
  }
  
  @Override
  public void selectComboBoxMenuItem(String comboBoxFieldId, String menuItem) {
    pages.getApplicationPage().ensurePageLoaded();
    
    selectComboBoxMenuItem(comboBoxFieldId, menuItem, false, menuItem.length());
  }
  
  @Override
  public void selectComboBoxMenuItemWithCharByCharReloadingOptions(String comboBoxFieldId, String menuItem, int minInputLength) {
    pages.getApplicationPage().ensurePageLoaded();
    // 1 is the minimal possible value
    selectComboBoxMenuItem(comboBoxFieldId, menuItem, true, Math.max(1, minInputLength));
  }
  
  //TODO унифицировать простые и сложные поля через get/set-FieldValue() - Нужна иерархия полей ! 
  private void selectComboBoxMenuItem(String comboBoxFieldId, String menuItem,
      final boolean charByCharReloadingOptions, final int minInputLength) { // TODO Поддержать локализацию
    
    // Подготовка: в случае, если на данный JepComboBoxField навешена загрузка опций по первому использованию,
    // то нажимаем на кнопку 'развернуть' для того, чтобы загрузка произошла, и ждём окончания загрузки опций.
    final WebElement dropDownButton = pages.getApplicationPage().getWebDriver().findElement(By.id(comboBoxFieldId + JEP_COMBO_BOX_FIELD_DROPDOWN_BTN_POSTFIX)); 
    getWait().until(elementToBeClickable(dropDownButton));
    dropDownButton.click();
    
    // Отслеживаем появление suggestBoxPopup, то есть когда список опций загрузится.
    final By comboBoxPopupPanelBy = By.id(comboBoxFieldId + JEP_COMBO_BOX_FIELD_POPUP_POSTFIX);
    getWait().until(presenceOfElementLocated(comboBoxPopupPanelBy));
    
    
    // Непосредственно поиск и выбор элемента в списке.
    // Очистим поле ввода.
    final WebElement fieldInput = pages.getApplicationPage().getWebDriver().findElement(By.id(comboBoxFieldId + JEP_FIELD_INPUT_POSTFIX)); 
    getWait().until(elementToBeClickable(fieldInput));
    String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE; 
    fieldInput.sendKeys(del);
    
    // Если menuItemText короче firstInputLength, сообщаем об ошибке
    if (minInputLength > menuItem.length()) {
      // Закроем открытый список опций.
      dropDownButton.click();
      
      throw new WrongOptionException("Wrong combobox option: [" + menuItem + "], "
          + "the input must be at least " + minInputLength + " chars.");
    }
    // Пишем в поле Комбо-бокса начальные символы (в случае с простой однократной загрузкой списка опций - весь текст сразу).
    fieldInput.sendKeys(menuItem.substring(0, minInputLength));
    
    if (charByCharReloadingOptions) {
      // Закроем открытый список опций.
      dropDownButton.click();
      
      // Отслеживаем появление suggestBoxPopup, то есть когда список опций загрузится.
      getWait().until(presenceOfElementLocated(comboBoxPopupPanelBy));
    }
    
    int ind = minInputLength;
    WebElement comboBoxMenuItem;
     
    // Продолжаем набирать оставшийся текст по одной букве, ожидая подгрузки списка, до тех пор пока либо не закончатся буквы,
    // либо опция не будет найдена в списке (в случае с простой однократной загрузкой списка опций - выход из цикла происходит сразу).
    while (true) {
      try {
        // Важно! лоцировать comboBoxPopupPanel нужно именно в каждой итерации цикла, несмотря на то, что элемент вроде не изменяется внутри цикла.
        comboBoxMenuItem = pages.getApplicationPage().getWebDriver().findElement(By.xpath(
            String.format("//*[@id='%s']//*[starts-with(@id, '%s') and @%s='%s']",
                comboBoxFieldId + JEP_COMBO_BOX_FIELD_POPUP_POSTFIX,
                comboBoxFieldId + JEP_COMBO_BOX_FIELD_MENU_ITEM_INFIX,
                JEP_OPTION_VALUE_HTML_ATTR,
                menuItem)));
        // Исключения не было - значит, опция найдена в списке на данном шаге.
        break;
      } catch (NoSuchElementException e) {
        
        // Опции не найдено в списке на данном шаге - значит, вводим следующую букву.
        if (ind < menuItem.length()) {
          fieldInput.sendKeys(Character.toString(menuItem.charAt(ind++)));
          
          // Закроем открытый список опций.
          dropDownButton.click();
          
          // Отслеживаем появление suggestBoxPopup, то есть когда список опций загрузится.
          getWait().until(presenceOfElementLocated(comboBoxPopupPanelBy));
        } else {
          // Закроем открытый список опций.
          dropDownButton.click();
          
          // Букв не осталось - значит, опции в списке нет.
          throw new WrongOptionException("Wrong combobox option: [" + menuItem + "]");
        }
      }
    }
    
    // Выбираем найденную опцию.
    getWait().until(elementToBeClickable(comboBoxMenuItem));
    comboBoxMenuItem.click();
  }
  
  @Override
  public void selectDualListMenuItems(String dualListFieldId, String[] menuItems) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // Очищаем правый список (переносим все влево)
    final WebElement moveAllLeftButton = pages.getApplicationPage().getWebDriver().
        findElement(By.id(dualListFieldId + JEP_DUAL_LIST_FIELD_MOVEALLLEFT_BTN_POSTFIX));
    getWait().until(elementToBeClickable(moveAllLeftButton));
    moveAllLeftButton.click();
    
    // Определяем кнопку "выбрать опцию" (переместить вправо)
    final WebElement moveRightButton = pages.getApplicationPage().getWebDriver().
        findElement(By.id(dualListFieldId + JEP_DUAL_LIST_FIELD_MOVERIGHT_BTN_POSTFIX));
    
    WebElement option;
    // Последовательно выбираем все опции из необходимых и перемещаем в правую часть
    for (String menuItem: menuItems) {
      if (!JepRiaUtil.isEmpty(menuItem)) {
        try {
          option = pages.getApplicationPage().getWebDriver().findElement(By.xpath(
              String.format("//*[@id='%s']//*[starts-with(@id, '%s') and @%s='%s']",
                  dualListFieldId + JEP_DUAL_LIST_FIELD_LEFTPART_POSTFIX,
                  dualListFieldId + JEP_DUAL_LIST_FIELD_MENU_ITEM_INFIX,
                  JEP_OPTION_VALUE_HTML_ATTR,
                  menuItem)));
          getWait().until(elementToBeClickable(option));
          option.click();
          
          getWait().until(elementToBeClickable(moveRightButton));
          moveRightButton.click();
        } catch (NoSuchElementException e) {
          throw new WrongOptionException("Wrong dualList option: [" + menuItem + "]");
        }
      }
    }
  }
  
  @Override
  public SaveResultEnum save() {
    SaveResultEnum result = null;
    pages.getApplicationPage().ensurePageLoaded();
    
    getWait().until(elementToBeClickable(By.id(TOOLBAR_SAVE_BUTTON_ID)));
    
    TextChangeChecker textChangeChecker = new TextChangeChecker(getStatusBar());
    pages.getApplicationPage().saveButton.click();
    
    WebDriver wd = pages.getApplicationPage().getWebDriver();
    ConditionChecker conditionChecker = new WebDriverWait(wd, WEB_DRIVER_TIMEOUT).until(
        ExpectedConditions.atLeastOneOfConditionIsSatisfied(
            new DisplayChecker(wd, ALERT_MESSAGEBOX_ID),
            new DisplayChecker(wd, ERROR_MESSAGEBOX_ID),
            textChangeChecker
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

  @Override
  public void setCheckBoxFieldValue(String checkBoxFieldId, boolean checked) {
    pages.getApplicationPage().ensurePageLoaded();
    
    WebElement fieldInput = pages.getApplicationPage().getWebDriver().findElement(By.id(checkBoxFieldId + JEP_FIELD_INPUT_POSTFIX)); 
    
    if (fieldInput.isSelected() != checked) {
      getWait().until(elementToBeClickable(fieldInput));
      fieldInput.click();
    }
  }

  @Override
  public void changeCheckBoxFieldValue(String checkBoxFieldId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    WebElement fieldInput = pages.getApplicationPage().getWebDriver().findElement(By.id(checkBoxFieldId + JEP_FIELD_INPUT_POSTFIX)); 
    
    getWait().until(elementToBeClickable(fieldInput));
    fieldInput.click();
  }

  @Override
  public boolean getCheckBoxFieldValue(String checkBoxFieldId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    WebElement fieldInput = pages.getApplicationPage().getWebDriver().findElement(By.id(checkBoxFieldId + JEP_FIELD_INPUT_POSTFIX));
    return fieldInput.isSelected();
  }

  @Override
  public void selectAllListMenuItems(String listFieldId, boolean selectAll) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // Cначала пробуем сделать это кнопкой "выделить все"
    WebElement selectAllCheckBox = pages.getApplicationPage().getWebDriver().
        findElement(By.id(listFieldId + JEP_LIST_FIELD_CHECKALL_POSTFIX));
    
    if ("true".equals(selectAllCheckBox.getAttribute("aria-hidden"))) {
      // Кнопка "выделить все" скрыта, значит, нужно снимать выделение с каждого элемента.
      
      // Получаем список всех чекбоксов внутри INPUT'а заданного поля 
      List<WebElement> allCheckBoxes = pages.getApplicationPage().getWebDriver().findElements(By.xpath(
          String.format("//*[@id='%s']//*[starts-with(@id, '%s')]",
              listFieldId + JEP_FIELD_INPUT_POSTFIX,
              listFieldId + JEP_LIST_FIELD_ITEM_CHECKBOX_INFIX)));
      // Кликаем на необходимые
      for (WebElement option: allCheckBoxes ) {
        if (option.isSelected() && !selectAll || !option.isSelected() && selectAll) {
          ((JavascriptExecutor) pages.getApplicationPage().getWebDriver()).executeScript("arguments[0].click();", option);
        }
      }
    } else {
      // Кнопка "выделить все" найдена.
      
      WebElement selectAllCheckBoxInput = selectAllCheckBox.findElement(By.xpath("./input[@type='checkbox']"));
      
      if (selectAllCheckBoxInput.isSelected()) {
        if (!selectAll) {
          getWait().until(elementToBeClickable(selectAllCheckBox));
          selectAllCheckBox.click();
        }
      } else {
        // устанавливаем флажок
        getWait().until(elementToBeClickable(selectAllCheckBox));
        selectAllCheckBox.click();
        
        if (!selectAll) {
          // далее при необходимости его снимаем
          getWait().until(elementToBeClickable(selectAllCheckBox));
          selectAllCheckBox.click();
        }
      }
    }
  }
  
  @Override
  public void selectListMenuItems(String listFieldId, String[] menuItems) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // Снимаем все флажки
    selectAllListMenuItems(listFieldId, false);
    
    
    WebElement option;
    // Последовательно отмечаем все опции из необходимых
    for (String menuItem: menuItems) {
      if (!JepRiaUtil.isEmpty(menuItem)) {
        try {
          // Ищем чекбокс с соответствующим искомому значению option-value.
          option = pages.getApplicationPage().getWebDriver().findElement(By.xpath(
              String.format("//*[@id='%s']//*[starts-with(@id, '%s') and @%s='%s']",
                  listFieldId + JEP_FIELD_INPUT_POSTFIX,
                  listFieldId + JEP_LIST_FIELD_ITEM_CHECKBOX_INFIX,
                  JEP_OPTION_VALUE_HTML_ATTR,
                  menuItem)));
          ((JavascriptExecutor) pages.getApplicationPage().getWebDriver()).executeScript("arguments[0].click();", option);
        
        } catch (NoSuchElementException e) {
          throw new WrongOptionException("Wrong list option: [" + menuItem + "]");
        }
      }
    }
  }

  @Override
  public String[] getListFieldValues(String jepListFieldId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // Получаем список всех чекбоксов внутри INPUT'а заданного поля 
      List<WebElement> allCheckBoxes = pages.getApplicationPage().getWebDriver().findElements(By.xpath(
          String.format("//*[@id='%s']//*[starts-with(@id, '%s')]",
              jepListFieldId + JEP_FIELD_INPUT_POSTFIX,
              jepListFieldId + JEP_LIST_FIELD_ITEM_CHECKBOX_INFIX)));
      
      List<String> ret = new ArrayList<String>();
      
      // Проходим по полученному списку, и, для отмеченных чекбоксов, получаем их option-value
      for (WebElement checkbox: allCheckBoxes) {
        if (checkbox.isSelected()) {
          ret.add(checkbox.getAttribute(JEP_OPTION_VALUE_HTML_ATTR));
        }
      }
      return ret.toArray(new String[ret.size()]);
  }

  @Override
  public void selectTreeItems(String treeFieldId, String[] itemPaths) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // Ждем загрузки данных в поле (а именно, появления элементов span)
    getWait().until(presenceOfElementLocated(By.xpath(
        String.format("//*[@id='%s']//span[starts-with(@id, '%s')]",
            treeFieldId + JEP_FIELD_INPUT_POSTFIX,
            treeFieldId + JEP_TREENODE_INFIX))));
    
    for (String itemPath: itemPaths) {
      selectTreeItem(treeFieldId, itemPath);
    }
  }
  
  private void selectTreeItem(String treeFieldId, String itemPath) {
    // Проверяем корректность пути.
    if (!JepRiaUtil.isEmpty(itemPath) && !itemPath.matches(".*(?<!\\\\)//.*") &&
        !itemPath.matches(".*(?<!\\\\)/") && !itemPath.matches("/.*")) {
      // 1) Парсим путь и готовимся к его обходу.
      
      final List<String> partsOriginal = Arrays.asList(itemPath.split("(?<!\\\\)/"));
      final List<String> parts = new ArrayList<String>(partsOriginal);

      // Разэкранируем экранированные слэши
      for (int i = 0; i < parts.size(); i++) {
        if (parts.get(i).contains("\\/")) {
          parts.set(i, parts.get(i).replaceAll("\\\\/", "/"));
        }
      }

      logger.debug("TREEFIELD_SELECT: BEGIN selecting the path in the tree (splitted): " + Arrays.toString(parts.toArray(new String[parts.size()])));
      
      // 2) Обходим путь по частям.
      
      WebElement deepestLocatedTreeItem;
      
      // массив значений 'aria-posinset' узлов дерева для ускорения повторного обхода
      // (для последней части пути это значение сохранять не нужно)
      final Integer[] posinsets = new Integer[parts.size() - 1];
      
      while(true) {
        logger.debug("TREEFIELD_SELECT: Expanding necessary folders from the very root");
        
        // Начинаем обход с элемента INPUT поля JepTreeField
        deepestLocatedTreeItem = pages.getApplicationPage().getWebDriver().findElement(By.id(treeFieldId + JEP_FIELD_INPUT_POSTFIX));
        int i;
        for (i = 0; i < parts.size() - 1; i++) {
          /*Служебная строка для логирования*/String indent="";for(int j=0;j<=i;j++)indent+="  ";
          
          String part = parts.get(i);
          
          // Разворачиваем нелистовые узлы
          
          final WebElement folder;
          try {
            if (posinsets[i] == null) {
              logger.debug(indent + "TREEFIELD_SELECT: Will locate folder (for the first time) of level "+(i+1)+" named " + part);
              
              // Разворачиваем данный узел впервые
              
              folder = deepestLocatedTreeItem.findElement(By.xpath(
                  String.format(".//div[@role='treeitem' and @aria-level='%d' and .//span[@id='%s']]",
                      i + 1,
                      treeFieldId + JEP_TREENODE_INFIX + part)));
              // Сохраняем значение 'aria-posinset' для ускорения дальнейшего поиска этого элемента.
              posinsets[i] = Integer.parseInt(folder.getAttribute("aria-posinset"));
            } else {
              logger.debug(indent + "TREEFIELD_SELECT: Will locate folder (that already has been located) of level "+(i+1)+" and posinset " + posinsets[i]);
              
              // Разворачиваем узел не впервые - быстрее, по сохраненному значению aria-posinset
              
              folder = deepestLocatedTreeItem.findElement(By.xpath(
                  String.format(".//div[@role='treeitem' and @aria-level='%d' and @aria-posinset='%d']",
                      i + 1,
                      posinsets[i])));
            }
          } catch (NoSuchElementException e) {
            /*Служебная строка для логирования*/String h="";for(int j=0;j<=i;j++)h+="/"+partsOriginal.get(j);
            throw new WrongOptionException("The node '" + part + "' was not found in the tree by path '" + h + "'");
          }
          
          // Проверяем, развернут ли узел
          if ("false".equals(folder.getAttribute("aria-expanded"))) {
            logger.debug(indent + "TREEFIELD_SELECT: Expand the folder " + part);
            
            // Разворачиваем узел и начинаем обход заново, потому что обновились все элементы TreeField.
            WebElement expandButton = folder.findElement(By.xpath("./div/div/div"/*FIXME ненадежное выражение!*/));
            expandButton.click();
            
            // Ждем загрузки ветки дерева (признаком этого является обновление всего виджета, в частности, отваливания элемента folder).
            // FIXME Опасное место! А вдруг JepTreeField будет переработан так, что виджет перестанет обновляться после каждой загрузки?
            getWait().until(stalenessOf(folder));
            break;
          } else {
            logger.debug(indent + "TREEFIELD_SELECT: The folder " + part + " is expanded, continue with its descendants");
            
            // Узел развернут, продолжаем разворачивать его потомков
            deepestLocatedTreeItem = folder;
          }
        }
        
        if (i == parts.size() - 1) {
          // Все нелистовые узлы развернуты, отмечаем необходимый элемент дерева
          
          String part = parts.get(i);
          logger.debug("TREEFIELD_SELECT: All folders are expanded, finally check the item " + part);
          
          WebElement elementToCheck;
          
          final boolean toCheckLeaf;
          if (part.startsWith(">")) {
            part = part.substring(1);
            toCheckLeaf = false;
          } else {
            toCheckLeaf = true;
          }
          
          try {
            elementToCheck = deepestLocatedTreeItem.findElement(By.xpath(
                String.format(".//div[@role='treeitem' and @aria-level='%d']//span[@id='%s' and @%s='%s']",
                    i + 1,
                    treeFieldId + JEP_TREENODE_INFIX + part,
                    JEP_TREENODE_ISLEAF_HTML_ATTR,
                    toCheckLeaf ? "true" : "false")));
            
          } catch (NoSuchElementException e) {
            /*Служебная строка для логирования*/String h="";for(int j=0;j<=i;j++)h+="/"+partsOriginal.get(j);
            if (toCheckLeaf) {
              throw new WrongOptionException("The leaf node '" + part + "' was not found in the tree by path " + h + ". "
                  + "If you meant the folder node with the same name and path, insert '>' before that name in test input.");
            } else {
              throw new WrongOptionException("The folder node '" + part + "' was not found in the tree by path " + h + ". "
                  + "If you meant the leaf node with the same name and path, remove '>' from that name in test input.");
            }
          }
          
          final boolean isTargetCheckable = !(JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKABLE.equals(elementToCheck.getAttribute(JEP_TREENODE_CHECKEDSTATE_HTML_ATTR)));
          
          if (!isTargetCheckable) {
            /*Служебная строка для логирования*/String h="";for(int j=0;j<=i;j++)h+="/"+partsOriginal.get(j);
            throw new WrongOptionException("Unable to check the node '" + part + "' by path '" + h +
                "' because the checkbox for this node is missing. "
                + "Insure that checking such nodes is allowed on this JepTreeField.");
          } 
          
          elementToCheck.click();
          
          logger.debug("TREEFIELD_SELECT: END selecting the path in the tree (splitted): " + Arrays.toString(parts.toArray(new String[parts.size()])));
          break;
        }
      }
      
    } else {
      throw new IllegalArgumentException(itemPath + " is not a syntactically valid path to an option: "
          + "the path must neither contain empty elements '//', nor begin, nor end with unescaped slash '/'");
    }
  }
  
  @Override
  public boolean selectAllTreeItems(String treeFieldId, boolean selectAll) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // Cначала пробуем сделать это кнопкой "выделить все"
    WebElement selectAllCheckBox = pages.getApplicationPage().getWebDriver().
        findElement(By.id(treeFieldId + JEP_TREE_FIELD_CHECKALL_POSTFIX));
    
    final boolean ret; 
    
    if ("true".equals(selectAllCheckBox.getAttribute("aria-hidden"))) {
      // Кнопка "выделить все" скрыта, значит, ничего не производим.
      ret = false;
    } else {
      // Кнопка "выделить все" найдена.
      ret = true;
      
      WebElement selectAllCheckBoxInput = selectAllCheckBox.findElement(By.xpath("./input[@type='checkbox']"));
      
      if (selectAllCheckBoxInput.isSelected()) {
        if (!selectAll) {
          getWait().until(elementToBeClickable(selectAllCheckBox));
          selectAllCheckBox.click();
        }
      } else {
        // устанавливаем флажок
        getWait().until(elementToBeClickable(selectAllCheckBox));
        selectAllCheckBox.click();
        if (!selectAll) {
          // далее при необходимости его снимаем
          getWait().until(elementToBeClickable(selectAllCheckBox));
          selectAllCheckBox.click();
        }
      }
    }
    
    return ret;
  }
  
  @Override
  public String[] getTreeFieldNodesByFilter(String treeFieldId, TreeItemFilter filter) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // Начинаем обход с элемента INPUT поля JepTreeField
    List<String> ret = traverseTree(
        pages.getApplicationPage().getWebDriver().findElement(By.id(treeFieldId + JEP_FIELD_INPUT_POSTFIX)),
        0,
        filter);
    
    return ret.toArray(new String[ret.size()]);
  }
  
  /**
   * Метод для рекурсивного обхода дерева, составляющий список путей до узлов, проходящих через фильтр.
   * @param node узел, с которого начинается обход (включительно)
   * @param level значение aria-level для указанного rootNode (0 в случае если обход начинается с INPUT-элемента поля)
   * @return Список путей отмеченных узлов ветки с корнем в rootNode 
   */
  private List<String> traverseTree(WebElement node, int level, final TreeItemFilter filter) {
    /*Служебная строка для логирования*/String indent="";for(int j=0;j<level;j++)indent+="  ";
    
    // Результирующий список имен узлов
    List<String> ret = new ArrayList<String>();
    
    final TreeItemWebElement nodeAsTreeItem = TreeItemWebElement.fromWebElement(node);
    
    if (nodeAsTreeItem.isRootOfTree()) {
      logger.debug(indent + "TREEFIELD_TRAVERSING: BEGIN traversing from the root of the tree.");
    } else {
      logger.debug(indent + "TREEFIELD_TRAVERSING: BEGIN traversing from the tree node " + nodeAsTreeItem.getItemName());
    }
    
    
    // Анализ данного узла
    if (filter.putToResult(nodeAsTreeItem) && nodeAsTreeItem.getItemName() != null) {
      
      if (nodeAsTreeItem.isLeaf()) {
        // Для листовой опции добавляем в результат просто имя.
        ret.add(nodeAsTreeItem.getItemName());
      } else {
        // Для нелистовой опции (неважно, развернутой или неразвернутой) добавляем префикс '>'.
        ret.add(">" + nodeAsTreeItem.getItemName());
      }
    }
    
    
    // Обход узлов, дочерних для данного
    if (nodeAsTreeItem.isExpanded() && 
        filter.traverseDescendants(nodeAsTreeItem)) {
      
      // Получим список всех узлов дерева, дочерних для заданного 
      List<WebElement> childNodes = node.findElements(By.xpath(
          String.format(".//div[@role='treeitem' and @aria-level='%d']",
              level + 1)));
      
      if (!JepRiaUtil.isEmpty(childNodes)) {
        logger.debug(indent + "TREEFIELD_TRAVERSING: The element has " + childNodes.size() + " child nodes");
      } else {
        logger.debug(indent + "TREEFIELD_TRAVERSING: The element has no child nodes");
      }
      
      for (WebElement childNode: childNodes) {
        logger.debug(indent + "TREEFIELD_TRAVERSING: Will recursively traverse a node...");
        
        List<String> childNodeNames = traverseTree(childNode, level + 1, filter);
        logger.debug(indent + "TREEFIELD_TRAVERSING: Returned from recursion.");
        
        for (String childNodeName: childNodeNames) {
          ret.add((nodeAsTreeItem.getItemName() != null ? nodeAsTreeItem.getItemName() + "/" : "") + childNodeName);
        }
      }
    }
    
    logger.debug(indent + "TREEFIELD_TRAVERSING: Return from recursion...");
    return ret;
  }
  
  @Override
  public boolean isFieldVisible(String fieldId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // visiblity can be determined by "aria-hidden" attribute of the entire field element
    WebElement element = pages.getApplicationPage().getWebDriver().findElement(By.id(fieldId));
    return !"true".equals(element.getAttribute("aria-hidden"));
  }

  
  @Override
  public boolean isFieldEnabled(String fieldId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // enability can be determined by "disabled" attribute of the field's INPUT element
    WebElement element = pages.getApplicationPage().getWebDriver().findElement(By.id(fieldId + JEP_FIELD_INPUT_POSTFIX));
    return !"true".equals(element.getAttribute("disabled"));
  }

  @Override
  public boolean isFieldEditable(String fieldId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // get all div children (but not grandchildren) of the field
    List<WebElement> potentialCards = pages.getApplicationPage().getWebDriver().findElements(By.xpath(
        String.format("//*[@id='%s']/div",
            fieldId)));
    WebElement editableCard = null, viewCard = null;
    
    for (WebElement potentialCard: potentialCards) {
      if (editableCard == null) {
        try { 
          potentialCard.findElement(By.xpath(
              String.format(".//*[@%s='%s']",
                  JEP_CARD_TYPE_HTML_ATTR,
                  JEP_CARD_TYPE_VALUE_EDTB)));
          // if no excpetion has been thrown, then the 'jep-card-type=editable' was found in a child
          // of the current div, that means it itself is the editable card.
          editableCard = potentialCard;
        } catch (NoSuchElementException e) {/*continue finding*/}
      }
      if (viewCard == null) {
        try { 
          potentialCard.findElement(By.xpath(
              String.format(".//*[@%s='%s']",
                  JEP_CARD_TYPE_HTML_ATTR,
                  JEP_CARD_TYPE_VALUE_VIEW)));
          // if no excpetion has been thrown, then the 'jep-card-type=view' was found in a child
          // of the current div, that means it itself is the view card.
          viewCard = potentialCard;
        } catch (NoSuchElementException e) {/*continue finding*/}
      }
    }
    
    if (editableCard == null) {
      throw new AutomationException("No editable card found for the field " + fieldId + ". Check the presence by xpath.");
    }
    if (viewCard == null) {
      throw new AutomationException("No view card found for the field " + fieldId + ". Check the presence by xpath.");
    }
    
    return editableCard.getAttribute("aria-hidden") == null &&
        "true".equals(viewCard.getAttribute("aria-hidden"));
  }

  @Override
  public boolean isFieldAllowBlank(String fieldId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    try {
      pages.getApplicationPage().getWebDriver().findElement(By.id(fieldId + JEP_FIELD_ALLOW_BLANK_POSTFIX));
      return false;
    } catch (NoSuchElementException e) {
      return true; 
    }
  }
  
  
  
  
  
  
  
  
  
  
  
  @Override
  public List<String> getGridHeaders(String gridId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    List<String> ret = new ArrayList<String>();
    
    List<WebElement> headers = pages.getApplicationPage().getWebDriver().findElements(By.xpath(
        String.format("//thead[@id='%s']//th",
            gridId + GRID_HEADER_POSTFIX)));
    
    for (int i = 0; i < headers.size() - 1; i++) {// One extra column of a zero width to stretch the rightmost column
      ret.add(headers.get(i).getAttribute("innerHTML"));
    }
    return ret;
  }

  @Override
  public List<List<Object>> getGridDataRowwise(String gridId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    // TODO get rid of this sleeping, but somehow wait until the grid is loaded!
    sleep(500);
    
    List<List<Object>> ret = new ArrayList<List<Object>>();
    
    List<WebElement> rows = pages.getApplicationPage().getWebDriver().findElements(By.xpath(
        String.format("//tbody[@id='%s']/tr",
            gridId + GRID_BODY_POSTFIX)));
    
    for (WebElement row: rows) {
      List<Object> rowObjects = new ArrayList<Object>();
      
      for (WebElement cellDiv: row.findElements(By.xpath("./td/div"))) {
        
        try {
          WebElement cellElement = cellDiv.findElement(By.xpath("./*"));
          rowObjects.add(cellElement);
        } catch (NoSuchElementException e) {
          rowObjects.add(cellDiv.getAttribute("innerHTML"));
        }
      }
      
      ret.add(rowObjects);
    }
    
    return ret;
  }

  @Override
  public void doGridColumnSettings(String gridId, String[] columns) {
    pages.getApplicationPage().ensurePageLoaded();

    // Locate the first column header (as far as there is no difference which column to call settings menu on)
    Actions actions = new Actions(pages.getApplicationPage().getWebDriver());
    WebElement firstColHeader = pages.getApplicationPage().getWebDriver().findElement(By.xpath(
        String.format("//thead[@id='%s']/tr/th",
            gridId + GRID_HEADER_POSTFIX)));
    
    // We will move the cursor to the center (by default) of the located header at first,
    // then move it to the top-right area (8 pixels before reaching both top and right borders) by the offset calculated below:
    Dimension dim = firstColHeader.getSize();
    final int xOffset = dim.width / 2 - 8;
    final int yOffset = -(dim.height / 2 - 8);
    
    // XXX The code below inexplicably does not work unless splitted into three lines:
    actions.moveToElement(firstColHeader).build().perform();
    actions.moveByOffset(xOffset, yOffset).build().perform();
    actions.click().perform();
    //
    
    // Wait until the popup menu appears
    getWait().until(presenceOfElementLocated(By.id(JepRiaAutomationConstant.GRID_HEADER_POPUP_ID)));

    final WebElement popupMenu = pages.getApplicationPage().getWebDriver().findElement(By.id(GRID_HEADER_POPUP_ID));
    
    // Obtain the list of menu item names, as it is in popup menu.
    final List<WebElement> menuItems = popupMenu.findElements(By.xpath(
        String.format(".//*[contains(@id, '%s')]",
            GRID_HEADER_POPUP_MENU_ITEM_POSTFIX)));
    
    final List<String> menuItemNames = new ArrayList<String>();
    for (WebElement e: menuItems) {
      menuItemNames.add(e.getText());
    }
    
    // Locate the UP navigation button
    final WebElement upNavButton = popupMenu.findElement(By.xpath(
        String.format(".//*[@id='%s']",
            GRID_HEADER_POPUP_NAVIG_UP_ID)));
    
    // Reorder the coulmns as told by the array
    int i = 0;
    for (; i < columns.length; i++) {
      final String column = columns[i];
      
      int j = menuItemNames.indexOf(column);
      if (j == -1) {
        // No column with required name found in popup menu
        throw new IllegalArgumentException("The column with name '" + column + "' was not found on the grid setting popup menu.");
      }
      
      // Check if the column is being shown (otherwise, check to show it)
      WebElement menuItemCheckBox = menuItems.get(j).findElement(By.xpath(".//input[@type='checkbox']"));
      if (!menuItemCheckBox.isSelected()) {
        menuItemCheckBox.click();
      }
      
      // Assume either j > i and the column needs to be moved, or j == i and the column is already on its place.
      if (j > i) {
        // select the menu item that needs to be moved
        menuItems.get(j).click();
        // move it j - i times up
        for (int movings = 0; movings < j - i; movings++) {
          upNavButton.click();
        }
        // move corresponding menuItem and menuItemName in both arrays
        menuItems.add(i, menuItems.remove(j));
        menuItemNames.add(i, menuItemNames.remove(j));
      }
    }
    
    // Disable all remaining columns, as they are not present in the array
    for (; i < menuItems.size(); i++) {
      WebElement menuItemCheckBox = menuItems.get(i).findElement(By.xpath(".//input[@type='checkbox']"));
      if (menuItemCheckBox.isSelected()) {
        menuItemCheckBox.click();
      }
    }

    // finally, close the popup menu
    WebElement closeButton = popupMenu.findElement(By.id(JepRiaAutomationConstant.GRID_HEADER_POPUP_CLOSE_ID));
    closeButton.click();
  }

  @Override
  public void clickModuleTab(String moduleId) {
    pages.getApplicationPage().ensurePageLoaded();
    
    try{
       WebElement moduleTab = pages.getApplicationPage().getContent().moduleTabPanel.findElement(By.id(moduleId));
       getWait().until(elementToBeClickable(moduleTab));
       moduleTab.click();
    } catch(NoSuchElementException e){
      throw new AutomationException("Can't click " + moduleId + " module tab.", e);
    } 
  }
}