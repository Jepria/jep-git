package com.technology.jep.jepria.auto.module;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ALERT_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.CONFIRM_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.CONFIRM_MESSAGE_BOX_YES_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ERROR_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_BODY_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_GLASS_MASK_ID;
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
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGOUT_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_DELETE_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_FIND_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.TOOLBAR_SAVE_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.technology.jep.jepria.auto.condition.ConditionChecker;
import com.technology.jep.jepria.auto.condition.DisplayChecker;
import com.technology.jep.jepria.auto.condition.ExpectedConditions;
import com.technology.jep.jepria.auto.exception.AutomationException;
import com.technology.jep.jepria.auto.exception.NotExpectedException;
import com.technology.jep.jepria.auto.exception.WrongOptionException;
import com.technology.jep.jepria.auto.module.page.JepRiaModulePage;
import com.technology.jep.jepria.auto.util.DragAndDropTestUtil;
import com.technology.jep.jepria.auto.util.WebDriverFactory;
import com.technology.jep.jepria.auto.util.WorkstateTransitionUtil;
import com.technology.jep.jepria.auto.widget.tree.TreeItemFilter;
import com.technology.jep.jepria.auto.widget.tree.TreeItemWebElement;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;
import com.technology.jep.jepria.shared.exceptions.UnsupportedException;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Реализация JepRiaModuleAuto
 * 
 * @param <P> интерфейс страницы, соответствующей данному модулю
 * (страница может содержать специфичные для модуля поля).
 * Если страница модуля не предполагает специфики (стандартная),
 * то вместо параметра подставляется класс {@link JepRiaModulePage}.
 */
public class JepRiaModuleAutoImpl<P extends JepRiaModulePage> implements JepRiaModuleAuto {

  protected P page;
  
  public JepRiaModuleAutoImpl(P page) {
    this.page = page;
  }
    
  private static final long WEB_DRIVER_TIMEOUT = 5;
  
  private static Logger logger = Logger.getLogger(JepRiaModuleAutoImpl.class.getName());
  
  /**
   * Метод ожидает появления любого из заданных workstat'ов в атрибуте статус бара.
   * (Необходимо, в частности, потому что после save можем оказаться как в view_list, так и в search).
   * @param expectedWorkstates ожидаемые воркстейты.
   * @return workstate, который дождались.
   */
  protected WorkstateEnum waitForStatusWorkstate(WorkstateEnum... expectedWorkstates) {
    
    Map<ConditionChecker, WorkstateEnum> map = new HashMap<ConditionChecker, WorkstateEnum>();
    for (final WorkstateEnum expectedWorkstate: expectedWorkstates) {
      if (expectedWorkstate != null) {
        map.put(new ConditionChecker() {
          @Override
          public boolean isSatisfied() {
            WorkstateEnum workstateAttrValue = page.getWorkstateFromStatusBar();
            return expectedWorkstate.equals(workstateAttrValue);
          }
        }, expectedWorkstate);
      }
    }
    
    ConditionChecker checkerSatisfied = null;
    try {
      checkerSatisfied = getWait().until(ExpectedConditions.atLeastOneOfConditionIsSatisfied(
          map.keySet().toArray(new ConditionChecker[map.keySet().size()])));
    } catch (TimeoutException e) {
      throw new RuntimeException("Timed out waiting for any workstate of '" + Arrays.toString(expectedWorkstates) + "' in StatusBar.", e);
    }

    // Если перешли на список, то ждем появления и исчезновения стеклянной маски
    WorkstateEnum finalWorkstate = map.get(checkerSatisfied);
    if(VIEW_LIST.equals(finalWorkstate)) {
      waitForListMask();
    }
    
    return finalWorkstate;
  }
  
  public void clickButton(String buttonId) {
    getWait().until(elementToBeClickable(By.id(buttonId)));
    WebDriverFactory.getDriver().findElement(By.id(buttonId)).click();
  }
  
  @Override
  public void doSearch(Map<String, String> template) {
    find();
    fillFields(template);
    
    setWorkstate(VIEW_LIST);
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
    WorkstateEnum currentWorkstate = getWorkstateFromStatusBar();
    
    //TODO: является ли переход в currentWorkstate ошибкой?
    //если да, то необходимо дописать else
    if(!currentWorkstate.equals(workstateTo))  {
      String toolbarButtonId = WorkstateTransitionUtil.getToolbarButtonId(currentWorkstate, workstateTo);
      if(toolbarButtonId != null) {
        getWait().until(elementToBeClickable(By.id(toolbarButtonId)));
        clickButton(toolbarButtonId);
        waitForStatusWorkstate(workstateTo);
        
      } else {
        throw new UnsupportedException("Wrong transition: " + currentWorkstate + "->" + workstateTo);
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
    doSearch(template);
    selectItem(0, gridId);
    setWorkstate(EDIT);
  }
  
  @Override
  public WorkstateEnum deleteSelectedRow() {
    assert SELECTED.equals(getWorkstateFromStatusBar());
    return deleteAndConfirm();
  }
  
  @Override
  public WorkstateEnum deleteDetail() {
    assert EDIT.equals(getWorkstateFromStatusBar()) || VIEW_DETAILS.equals(getWorkstateFromStatusBar());
    return deleteAndConfirm();
  }
  
  /**
   * Метод кликает кнопку тулбара "Удалить", подтверждает и ждёт возвращения на список.
   * @return workstate, в котором находится приложение после удаления. 
   */
  private WorkstateEnum deleteAndConfirm() {
    getWait().until(elementToBeClickable(By.id(TOOLBAR_DELETE_BUTTON_ID)));
    
    clickButton(TOOLBAR_DELETE_BUTTON_ID);
    
    assert checkMessageBox(CONFIRM_MESSAGEBOX_ID);
    
    clickButton(CONFIRM_MESSAGE_BOX_YES_BUTTON_ID);
    
    return waitForStatusWorkstate(VIEW_LIST, SEARCH);
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
  public void selectItems(int index, String gridId) {
    assert getWorkstateFromStatusBar() == VIEW_LIST || getWorkstateFromStatusBar() == SELECTED;
    By gridBodyBy = null;
    
    if(gridId == null){
      gridBodyBy = By.xpath(String.format("//tbody[contains(@id, '%s')]", GRID_BODY_POSTFIX));
    }else{
      gridBodyBy = By.id(gridId+GRID_BODY_POSTFIX);
    }
    WebElement gridBody = findElementAndWait(gridBodyBy);
    
    List<WebElement> gridRows = gridBody.findElements(By.xpath("./tr"));
    
    if (gridRows.size() <= index) {
      throw new IndexOutOfBoundsException("Failed to select the item with index "+index+" in the list, as it contains "+gridRows.size()+" items only.");
    }
    
    getWait().until(elementToBeClickable(gridRows.get(index)));
    
    Actions actions = new Actions(WebDriverFactory.getDriver());
    actions.keyDown(Keys.LEFT_CONTROL)
        .click(gridRows.get(index))
        .keyUp(Keys.LEFT_CONTROL)
        .build()
        .perform();
  }
  
  @Override
  public void selectItem(int index, String gridId) {
    assert getWorkstateFromStatusBar() == VIEW_LIST || getWorkstateFromStatusBar() == SELECTED;
    
    By gridBodyBy = null;
    if(gridId == null){
      gridBodyBy = By.xpath(String.format("//tbody[contains(@id, '%s')]", GRID_BODY_POSTFIX));
    }else{
      gridBodyBy = By.id(gridId+GRID_BODY_POSTFIX);
    }
    WebElement gridBody = findElementAndWait(gridBodyBy);
    
    List<WebElement> gridRows = gridBody.findElements(By.xpath("./tr"));
    
    if (gridRows.size() <= index) {
      throw new IndexOutOfBoundsException("Failed to select the item with index "+index+" in the list, as it contains "+gridRows.size()+" items only.");
    }
    
    getWait().until(elementToBeClickable(gridRows.get(index)));
    
    gridRows.get(index).click();
    waitForStatusWorkstate(SELECTED);
  }

  @Override
  public void sortByColumn(String gridId, Integer columnId){
    
    assert getWorkstateFromStatusBar() == VIEW_LIST || getWorkstateFromStatusBar() == SELECTED;
    
    List<WebElement> columnList = findElementsAndWait(By.xpath(
        String.format("//thead[@id='%s']/tr/th",
            gridId + GRID_HEADER_POSTFIX)));
    new Actions(WebDriverFactory.getDriver()).click(columnList.get(columnId)).build().perform();
    new Actions(WebDriverFactory.getDriver()).moveByOffset(0, 0).build().perform();
    
    waitForListMask();
  }
  
  @Override
  public String getGridRowCount(){
    //class="gwt-TextBox jepRia-FontStyle jepRia-PagingBar-pageNumber"
    WebElement rowCountInput = findElementsAndWait(By.xpath(
        String.format("//input[@class='%s']",
            "gwt-TextBox jepRia-FontStyle jepRia-PagingBar-pageNumber"))).get(1);
    getWait().until(elementToBeClickable(rowCountInput));
    
    return rowCountInput.getAttribute("value");
  }
  
  @Override
  public void setGridRowCount(String rowCount){
    //class="gwt-TextBox jepRia-FontStyle jepRia-PagingBar-pageNumber"
    WebElement rowCountInput = findElementsAndWait(By.xpath(
        String.format("//input[@class='%s']",
            "gwt-TextBox jepRia-FontStyle jepRia-PagingBar-pageNumber"))).get(1);
    getWait().until(elementToBeClickable(rowCountInput));
    
    String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE; 
    rowCountInput.sendKeys(del + rowCount + Keys.ENTER);
  }
  
  private void fillFields(Map<String, String> fieldMap) {
    for(String fieldName: fieldMap.keySet()) {
      setFieldValue(fieldName, fieldMap.get(fieldName));
    }
  }

  @Override
  public void setFieldValue(String fieldId, String value) {
    WebElement fieldInput = findElementAndWait(By.id(fieldId + JEP_FIELD_INPUT_POSTFIX)); 
    getWait().until(elementToBeClickable(fieldInput));
    
    String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE; 
    fieldInput.sendKeys(del + value);
  }

  @Override
  public String getFieldValue(String fieldId) {
    WebElement fieldInput = findElementAndWait(By.id(fieldId + JEP_FIELD_INPUT_POSTFIX));
    return fieldInput.getAttribute("value");
  }
  
  @Override
  public void setLargeFieldValue(String fieldId, String pathToFile) {
    WebElement fieldInput = findElementAndWait(By.id(fieldId + JEP_FIELD_INPUT_POSTFIX)); 
    getWait().until(elementToBeClickable(fieldInput));
    
    fieldInput.sendKeys(pathToFile);
  }
  
  @Override
  public String[] getDualListFieldValues(String jepDualListFieldId) {
    // Получаем список всех опций внутри INPUT'а заданного поля (INPUT это правый список)
    List<WebElement> options = findElementsAndWait(By.xpath(
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
    selectComboBoxMenuItem(comboBoxFieldId, menuItem, false, menuItem.length());
  }
  
  @Override
  public void selectComboBoxMenuItemWithCharByCharReloadingOptions(String comboBoxFieldId, String menuItem, int minInputLength) {
    // 1 is the minimal possible value
    selectComboBoxMenuItem(comboBoxFieldId, menuItem, true, Math.max(1, minInputLength));
  }
  
  //TODO унифицировать простые и сложные поля через get/set-FieldValue() - Нужна иерархия полей ! 
  private void selectComboBoxMenuItem(String comboBoxFieldId, String menuItem,
      final boolean charByCharReloadingOptions, final int minInputLength) { // TODO Поддержать локализацию
    
    // Подготовка: в случае, если на данный JepComboBoxField навешена загрузка опций по первому использованию,
    // то нажимаем на кнопку 'развернуть' для того, чтобы загрузка произошла, и ждём окончания загрузки опций.
    final WebElement dropDownButton = findElementAndWait(By.id(comboBoxFieldId + JEP_COMBO_BOX_FIELD_DROPDOWN_BTN_POSTFIX)); 
    getWait().until(elementToBeClickable(dropDownButton));
    dropDownButton.click();
    
    // Отслеживаем появление suggestBoxPopup, то есть когда список опций загрузится.
    final By comboBoxPopupPanelBy = By.id(comboBoxFieldId + JEP_COMBO_BOX_FIELD_POPUP_POSTFIX);
    getWait().until(presenceOfElementLocated(comboBoxPopupPanelBy));
    
    
    // Непосредственно поиск и выбор элемента в списке.
    // Очистим поле ввода.
    final WebElement fieldInput = findElementAndWait(By.id(comboBoxFieldId + JEP_FIELD_INPUT_POSTFIX)); 
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
    
    // Индекс буквы, которая была введена в поле поиска опции последней.
    int ind = minInputLength;
    WebElement comboBoxMenuItem;
     
    // Продолжаем набирать оставшийся текст по одной букве, ожидая подгрузки списка, до тех пор пока либо не закончатся буквы,
    // либо опция не будет найдена в списке (в случае с простой однократной загрузкой списка опций - выход из цикла происходит сразу).
    while (true) {
      try {
        // Попытаемся найти в выпадающем списке искомую опцию по точному совпадению.
        
        // Важно! лоцировать comboBoxPopupPanel нужно именно в каждой итерации цикла, несмотря на то, что элемент вроде не изменяется внутри цикла.
        comboBoxMenuItem = WebDriverFactory.getDriver().findElement(By.xpath(
            String.format("//*[@id='%s']//*[starts-with(@id, '%s') and @%s='%s']",
                comboBoxFieldId + JEP_COMBO_BOX_FIELD_POPUP_POSTFIX,
                comboBoxFieldId + JEP_COMBO_BOX_FIELD_MENU_ITEM_INFIX,
                JEP_OPTION_VALUE_HTML_ATTR,
                menuItem)));
        // Исключения не было - значит, опция найдена в списке на данном шаге.
        break;
      } catch (NoSuchElementException e) {
        // Если опций по точному совпадению не найдено,
        // попытаемся найти в выпадающем списке искомую опцию по совпадению начала.
        List<WebElement> comboBoxMenuItems = WebDriverFactory.getDriver().findElements(By.xpath(
            String.format("//*[@id='%s']//*[starts-with(@id, '%s') and starts-with(@%s, '%s')]",
                comboBoxFieldId + JEP_COMBO_BOX_FIELD_POPUP_POSTFIX,
                comboBoxFieldId + JEP_COMBO_BOX_FIELD_MENU_ITEM_INFIX,
                JEP_OPTION_VALUE_HTML_ATTR,
                menuItem)));
        // В случае, если нашли единственную опцию с общим началом,
        // то можем принят решение, найдена ли нужная опция.
        if (comboBoxMenuItems != null && comboBoxMenuItems.size() == 1) {
          
          comboBoxMenuItem = comboBoxMenuItems.get(0);
          //Либо ввод завершен, либо искомая опция является префиксом единственной найденой
          if(ind == menuItem.length() || comboBoxMenuItem.getText().startsWith(menuItem)){
            //Опция найдена, выходим из цикла
            break;
          }else{
            //Префикс не совпал, опция не найдена
            throw new WrongOptionException("Wrong combobox option: [" + menuItem + "]");
          }
        }
        
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
    // Очищаем правый список (переносим все влево)
    final WebElement moveAllLeftButton = findElementAndWait(
        By.id(dualListFieldId + JEP_DUAL_LIST_FIELD_MOVEALLLEFT_BTN_POSTFIX));
    getWait().until(elementToBeClickable(moveAllLeftButton));
    moveAllLeftButton.click();
    
    // Определяем кнопку "выбрать опцию" (переместить вправо)
    final WebElement moveRightButton = findElementAndWait(
        By.id(dualListFieldId + JEP_DUAL_LIST_FIELD_MOVERIGHT_BTN_POSTFIX));
    
    WebElement option;
    // Последовательно выбираем все опции из необходимых и перемещаем в правую часть
    for (String menuItem: menuItems) {
      if (!JepRiaUtil.isEmpty(menuItem)) {
        try {
          option = findElementAndWait(By.xpath(
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
    
    getWait().until(elementToBeClickable(By.id(TOOLBAR_SAVE_BUTTON_ID)));
    
    page.saveButton.click();
    
    WebDriver wd = WebDriverFactory.getDriver();
    
    ConditionChecker alertChecker = new DisplayChecker(wd, ALERT_MESSAGEBOX_ID);
    ConditionChecker errorChecker = new DisplayChecker(wd, ERROR_MESSAGEBOX_ID);
    ConditionChecker statusChecker = new ConditionChecker() {
      @Override
      public boolean isSatisfied() {
        return !getWorkstateFromStatusBar().equals(CREATE) && 
            !getWorkstateFromStatusBar().equals(EDIT);
      }
    };
    ConditionChecker conditionChecker = new WebDriverWait(wd, WEB_DRIVER_TIMEOUT).until(
        ExpectedConditions.atLeastOneOfConditionIsSatisfied(alertChecker, errorChecker, statusChecker)
    );    
    
    if (conditionChecker == statusChecker) {
      result = SaveResultEnum.SUCCESS;
      
    } else if (conditionChecker == alertChecker) {
      result = SaveResultEnum.ALERT_MESSAGE_BOX;
    } else if (conditionChecker == errorChecker) {
      result = SaveResultEnum.ERROR_MESSAGE_BOX;
    } else {
      throw new NotExpectedException("Save error");
    }

    return result;
  }

  public boolean checkMessageBox(String messageBoxId) {
    return isDisplayed(messageBoxId);
  }

  /**
   * Метод присваивает внутренней переменной новое значение состояния и ждет загрузки списка, если
   * новое состояние - {@link WorkstateEnum#VIEW_LIST}.<br>
   * <i>Никаких UI-действий не производится. Для выполнения
   * собственно переходов между состояниями с помощью кнопок тулбара см. {@link #setWorkstate(WorkstateEnum)}.</i>.
   */
  public static void waitForListMask() {
    // Если переходим в состояние просмотра списка, то, вдобавок ко всему,
    // дождемся появления и исчезновения стеклянной маски, появляющейся на списке во время загрузки.
    WebElement gridGlassMask = null;
    try {
      // Поскольку загрузка списка может произойти быстро настолько, что стеклянная маска не успеет
      // слоцироваться, подождем ее появления 1 секунду
      gridGlassMask = WebDriverFactory.getWait(1).until(presenceOfElementLocated(By.id(GRID_GLASS_MASK_ID)));
    } catch (TimeoutException e) {
      // Если загрузка списка произошла слишком быстро, и стеклянная маска не успела лоцироваться,
      // то пропускаем шаг ожидания исчезновения стеклянной маски.
    }
    
    if(gridGlassMask != null) {
      getWait().until(stalenessOf(gridGlassMask));
    }
  }

  private boolean isDisplayed(String id) {
    try {
      WebDriverFactory.getDriver().findElement(By.id(id));
      return true;
    } catch(NoSuchElementException ex) {
      return false;
    }
  }

  @Override
  public void edit(String idFieldName, String id) {
    throw new NotImplementedYetException();
  }

  @Override
  public void setCheckBoxFieldValue(String checkBoxFieldId, boolean checked) {
    WebElement fieldInput = findElementAndWait(By.id(checkBoxFieldId + JEP_FIELD_INPUT_POSTFIX)); 
    
    if (fieldInput.isSelected() != checked) {
      getWait().until(elementToBeClickable(fieldInput));
      fieldInput.click();
    }
  }

  @Override
  public void changeCheckBoxFieldValue(String checkBoxFieldId) {
    WebElement fieldInput = findElementAndWait(By.id(checkBoxFieldId + JEP_FIELD_INPUT_POSTFIX)); 
    
    getWait().until(elementToBeClickable(fieldInput));
    fieldInput.click();
  }

  @Override
  public boolean getCheckBoxFieldValue(String checkBoxFieldId) {
    WebElement fieldInput = findElementAndWait(By.id(checkBoxFieldId + JEP_FIELD_INPUT_POSTFIX));
    return fieldInput.isSelected();
  }

  @Override
  public void selectAllListMenuItems(String listFieldId, boolean selectAll) {
    // Cначала пробуем сделать это кнопкой "выделить все"
    WebElement selectAllCheckBox = findElementAndWait(
        By.id(listFieldId + JEP_LIST_FIELD_CHECKALL_POSTFIX));
    
    if ("true".equals(selectAllCheckBox.getAttribute("aria-hidden"))) {
      // Кнопка "выделить все" скрыта, значит, нужно снимать выделение с каждого элемента.
      
      // Получаем список всех чекбоксов внутри INPUT'а заданного поля 
      List<WebElement> allCheckBoxes = findElementsAndWait(By.xpath(
          String.format("//*[@id='%s']//*[starts-with(@id, '%s')]",
              listFieldId + JEP_FIELD_INPUT_POSTFIX,
              listFieldId + JEP_LIST_FIELD_ITEM_CHECKBOX_INFIX)));
      // Кликаем на необходимые
      for (WebElement option: allCheckBoxes ) {
        if (option.isSelected() && !selectAll || !option.isSelected() && selectAll) {
          ((JavascriptExecutor) WebDriverFactory.getDriver()).executeScript("arguments[0].click();", option);
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
    // Снимаем все флажки
    selectAllListMenuItems(listFieldId, false);
    
    
    WebElement option;
    // Последовательно отмечаем все опции из необходимых
    for (String menuItem: menuItems) {
      if (!JepRiaUtil.isEmpty(menuItem)) {
        try {
          // Ищем чекбокс с соответствующим искомому значению option-value.
          option = WebDriverFactory.getDriver().findElement(By.xpath(
              String.format("//*[@id='%s']//*[starts-with(@id, '%s') and @%s='%s']",
                  listFieldId + JEP_FIELD_INPUT_POSTFIX,
                  listFieldId + JEP_LIST_FIELD_ITEM_CHECKBOX_INFIX,
                  JEP_OPTION_VALUE_HTML_ATTR,
                  menuItem)));
          ((JavascriptExecutor) WebDriverFactory.getDriver()).executeScript("arguments[0].click();", option);
        
        } catch (NoSuchElementException e) {
          throw new WrongOptionException("Wrong list option: [" + menuItem + "]");
        }
      }
    }
  }

  @Override
  public String[] getListFieldValues(String jepListFieldId) {
    // Получаем список всех чекбоксов внутри INPUT'а заданного поля 
    List<WebElement> allCheckBoxes = findElementsAndWait(By.xpath(
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
        deepestLocatedTreeItem = findElementAndWait(By.id(treeFieldId + JEP_FIELD_INPUT_POSTFIX));
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
            //getWait().until(stalenessOf(folder));
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
    // Cначала пробуем сделать это кнопкой "выделить все"
    WebElement selectAllCheckBox = findElementAndWait(By.id(treeFieldId + JEP_TREE_FIELD_CHECKALL_POSTFIX));
    
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
    // Начинаем обход с элемента INPUT поля JepTreeField
    List<String> ret = traverseTree(
        findElementAndWait(By.id(treeFieldId + JEP_FIELD_INPUT_POSTFIX)),
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
    // visiblity can be determined by "aria-hidden" attribute of the entire field element
    WebElement element = findElementAndWait(By.id(fieldId));
    return !"true".equals(element.getAttribute("aria-hidden"));
  }

  
  @Override
  public boolean isFieldEnabled(String fieldId) {
    // enability can be determined by "disabled" attribute of the field's INPUT element
    WebElement element = findElementAndWait(By.id(fieldId + JEP_FIELD_INPUT_POSTFIX));
    return !"true".equals(element.getAttribute("disabled"));
  }

  @Override
  public boolean isFieldEditable(String fieldId) {
    // get all div children (but not grandchildren) of the field
    List<WebElement> potentialCards = findElementsAndWait(By.xpath(
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
    //TODO сначала проверить презенс самого поля с помощью getWait
    try {
      WebDriverFactory.getDriver().findElement(By.id(fieldId + JEP_FIELD_ALLOW_BLANK_POSTFIX));
      return false;
    } catch (NoSuchElementException e) {
      return true; 
    }
  }
  
  @Override
  public void dragAndDropGridRowBeforeTarget(int draggableRowIndex, int targetRowIndex, String gridId){
    assert getWorkstateFromStatusBar() == VIEW_LIST || getWorkstateFromStatusBar() == SELECTED;
    
    WebDriver driver = WebDriverFactory.getDriver();
    selectItem(draggableRowIndex, gridId);
    WebElement draggableRow = getGridRowElement(draggableRowIndex, gridId);
    WebElement targetRow = getGridRowElement(targetRowIndex, gridId);
    Dimension sourceElementSize = draggableRow.getSize();
    Point sourceLocation = draggableRow.getLocation();
    int sourceX = sourceLocation.getX() + sourceElementSize.getWidth() / 2;
    int sourceY = sourceLocation.getY() + sourceElementSize.getHeight() / 2;
    Dimension targetElementSize = targetRow.getSize();
    Point targetLocation = targetRow.getLocation();
    int targetX = targetLocation.getX() + targetElementSize.getWidth() / 2;
    int targetY = targetLocation.getY() + targetElementSize.getHeight() * 15 / 100;
    DragAndDropTestUtil.html5_DragAndDrop(driver, draggableRow, targetRow, sourceX, sourceY, targetX, targetY);
    
    waitForStatusWorkstate(VIEW_LIST, SELECTED);
  }
  
  @Override
  public void dragAndDropGridRowAfterTarget(int draggableRowIndex, int targetRowIndex, String gridId){
    assert getWorkstateFromStatusBar() == VIEW_LIST || getWorkstateFromStatusBar() == SELECTED;
    
    WebDriver driver = WebDriverFactory.getDriver();
    selectItem(draggableRowIndex, gridId);
    WebElement draggableRow = getGridRowElement(draggableRowIndex, gridId);
    WebElement targetRow = getGridRowElement(targetRowIndex, gridId);
    Dimension sourceElementSize = draggableRow.getSize();
    Point sourceLocation = draggableRow.getLocation();
    int sourceX = sourceLocation.getX() + sourceElementSize.getWidth() / 2;
    int sourceY = sourceLocation.getY() + sourceElementSize.getHeight() / 2;
    Dimension targetElementSize = targetRow.getSize();
    Point targetLocation = targetRow.getLocation();
    int targetX = targetLocation.getX() + targetElementSize.getWidth() / 2;
    int targetY = targetLocation.getY() + targetElementSize.getHeight() * 85 / 100;
    DragAndDropTestUtil.html5_DragAndDrop(driver, draggableRow, targetRow, sourceX, sourceY, targetX, targetY);
    
    waitForStatusWorkstate(VIEW_LIST, SELECTED);
  }

  @Override
  public void dragAndDropGridRowsBeforeTarget(
      List<Integer> draggableRowIndexList, int targetRowIndex, String gridId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void dragAndDropGridRowsAfterTarget(List<Integer> draggableRowIndexList, int targetRowIndex, String gridId){
    //TODO сделать множественное выделение в списке
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void dragAndDropGridRow(int draggableRowIndex, int targetRowIndex, String gridId){
    assert getWorkstateFromStatusBar() == VIEW_LIST || getWorkstateFromStatusBar() == SELECTED;
    
    WebDriver driver = WebDriverFactory.getDriver();
    selectItem(draggableRowIndex, gridId);
    WebElement draggableRow = getGridRowElement(draggableRowIndex, gridId);
    WebElement targetRow = getGridRowElement(targetRowIndex, gridId);
    Dimension sourceElementSize = draggableRow.getSize();
    Point sourceLocation = draggableRow.getLocation();
    int sourceX = sourceLocation.getX() + sourceElementSize.getWidth() / 2;
    int sourceY = sourceLocation.getY() + sourceElementSize.getHeight() / 2;
    Dimension targetElementSize = targetRow.getSize();
    Point targetLocation = targetRow.getLocation();
    int targetX = targetLocation.getX() + targetElementSize.getWidth() / 2;
    int targetY = targetLocation.getY() + targetElementSize.getHeight() / 2;
    DragAndDropTestUtil.html5_DragAndDrop(driver, draggableRow, targetRow, sourceX, sourceY, targetX, targetY);
    waitForStatusWorkstate(VIEW_LIST, SELECTED);
  }
  
  @Override
  public void dragAndDropGridRows(List<Integer> draggableRowIndexList, int targetRowIndex, String gridId){
    //TODO сделать множественное выделение в списке
    throw new UnsupportedOperationException();
  }
  
  @Override
  public List<String> getGridHeaders(String gridId) {
    List<String> ret = new ArrayList<String>();
    
    List<WebElement> headers = findElementsAndWait(By.xpath(
        String.format("//thead[@id='%s']//th",
            gridId + GRID_HEADER_POSTFIX)));
    
    for (int i = 0; i < headers.size() - 1; i++) {// One extra column of a zero width to stretch the rightmost column
      ret.add(headers.get(i).getAttribute("innerHTML"));
    }
    return ret;
  }

  @Override
  public boolean isGridEmpty(String gridId) {
    return getGridBody(gridId) == null;
  }
  
  private WebElement getGridBody(String gridId) {
    try {
      return WebDriverFactory.getDriver().findElement(By.xpath(
          String.format("//tbody[@id='%s']",
              gridId + GRID_BODY_POSTFIX)));
    } catch (NoSuchElementException e) {
      return null;
    }
  }
  
  @Override
  public WebElement getGridRowElement(int rowIndex, String gridId){
    WebElement gridBody = getGridBody(gridId);
    // Убедимся, что грид присутствует на форме
    if (gridBody == null) {
      // Грид отсутствует - значит, курсор пуст.
      return null;
    }
    return gridBody.findElements(By.xpath(
        String.format("./tr",
            gridId + GRID_BODY_POSTFIX))).get(rowIndex);
  }
  
  @Override
  public List<List<Object>> getGridDataRowwise(String gridId) {
    List<List<Object>> ret = new ArrayList<List<Object>>();
    
    WebElement gridBody = getGridBody(gridId);
    // Убедимся, что грид присутствует на форме
    if (gridBody == null) {
      // Грид отсутствует - значит, курсор пуст.
      return ret;
    }
    
    List<WebElement> rows = gridBody.findElements(By.xpath(
        String.format("./tr",
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
    // Locate the first column header (as far as there is no difference which column to call settings menu on)
    Actions actions = new Actions(WebDriverFactory.getDriver());
    WebElement firstColHeader = findElementAndWait(By.xpath(
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

    final WebElement popupMenu = findElementAndWait(By.id(GRID_HEADER_POPUP_ID));
    
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
  public void ensureModuleLoaded() {
    page.ensurePageLoaded();
  }
  
  /**
   * Ищет элемент на текущей странице и, в том случае, если его нет, ожидает его появления
   * @param by локатор искомого элемента
   * @return Элемент
   * @throws NoSuchElementException Если по истечении таймаута элемент все же не найден.
   */
  protected static WebElement findElementAndWait(By by) throws NoSuchElementException {
    try {
      return getWait().until(presenceOfElementLocated(by));
    } catch (TimeoutException e) {
      throw new NoSuchElementException("No element found by locator ["+by+"]", e);
    }
  }
  
  /**
   * Ищет элементы на текущей странице и, в том случае, если их нет, ожидает их появления
   * @param by локатор искомого элемента
   * @return Элемент
   * @throws NoSuchElementException Если по истечении таймаута элементы все же не найдены.
   */
  protected static List<WebElement> findElementsAndWait(By by) throws NoSuchElementException {
    try {
      return getWait().until(presenceOfAllElementsLocatedBy(by));
    } catch (TimeoutException e) {
      throw new NoSuchElementException("No element found by locator ["+by+"]", e);
    }
  }

  @Override
  public WorkstateEnum getWorkstateFromStatusBar() {
    return page.getWorkstateFromStatusBar();
  }
    
}