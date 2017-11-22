package com.technology.jep.jepria.client.ui.main.widget;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.ui.main.ModuleConfiguration;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.container.JepTabLayoutPanel;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.shared.exceptions.IdNotFoundException;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class MainTabPanel extends HeaderPanel {
  
  private List<JepListener> exitListeners = new ArrayList<JepListener>();
  private Label userNameLabel = createUserNameLabel();
  private final static int BAR_HEIGHT = 22;
  private final static Unit BAR_UNIT = Unit.PX;
  private Map<String, Label> mapOfModule = new HashMap<String, Label>();
  private FlowPanel tabBar;
  private JepTabLayoutPanel tabs = new JepTabLayoutPanel(JepRiaAutomationConstant.MODULE_TAB_PANEL_ID, BAR_HEIGHT, BAR_UNIT);
  private int currentSelectedIndex;
  
  /**
   * Наименование селектора (класса стилей) обычной вкладки основной панели.
   */
  private static final String TAB_LAYOUT_PANEL_TAB_STYLE = "jepRia-TabLayoutPanelTab-common";
  
  public MainTabPanel() {
    // Компонент будет занимать всю область родителя.
    setSize("100%", "100%");
    tabs.setHeight(BAR_HEIGHT + BAR_UNIT.getType());
    
    // Получим главный компонент, состоящий из области вкладок и их содержимого.
    LayoutPanel mainPanel = (LayoutPanel) tabs.getWidget();
    
    // Найдем область вкладок, которая является первой FlowPanel в главном компоненте.
    for (int i = 0; i < mainPanel.getWidgetCount(); ++i){
      Widget widget = mainPanel.getWidget(i);
      if (widget instanceof FlowPanel){
        tabBar = (FlowPanel) widget; 
        break; // Область вкладок найдена.
      }
    }
    
    Style style = tabBar.getElement().getStyle();
    style.setBackgroundColor("#fff");
    style.setPaddingLeft(0, BAR_UNIT);
    
    HorizontalPanel entrancePanel = createEntrancePanel(userNameLabel, exitListeners);
    
    mainPanel.add(entrancePanel);
    
    mainPanel.setWidgetRightWidth(entrancePanel, 5, BAR_UNIT, 400, BAR_UNIT);
    mainPanel.setWidgetTopHeight(entrancePanel, 2, BAR_UNIT, BAR_HEIGHT, BAR_UNIT);
    
    mainPanel.getWidgetContainerElement(entrancePanel).getStyle().setTop(5, BAR_UNIT);
    
    setHeaderWidget(tabs);
    
    // Добавим слушателя события изменения размеров основного окна. 
    Window.addResizeHandler(new ResizeHandler() {
      @Override
      public void onResize(ResizeEvent event) {
        MainTabPanel.this.onResize();
      }
    });
  }

  /**
   * Создает EntrancePanel. Содержит имя пользователя и кнопку выхода.
   * @param userNameLabel Виджет с именем пользователя.
   * @param exitListeners Список обработчиков по нажатию на кнопку выхода.
   * @return EntrancePanel
   */
  public static HorizontalPanel createEntrancePanel(Label userNameLabel, final List<JepListener> exitListeners) {

    HorizontalPanel entrancePanel = new HorizontalPanel();
    entrancePanel.getElement().setId(JepRiaAutomationConstant.ENTRANCE_PANEL_ID); // TODO Передавать id в конструкторе (нужен новый класс)
    
    entrancePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    
    entrancePanel.add(userNameLabel);
    entrancePanel.setCellWidth(userNameLabel, "100%");
    
    Label splitter = new Label(" ");
    splitter.setWidth("5px");
    splitter.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    entrancePanel.add(splitter);
    
    PushButton exitButton = new PushButton(new Image(JepImages.exit()));
    exitButton.getElement().setId(JepRiaAutomationConstant.ENTRANCE_PANEL_LOGOUT_BUTTON_ID); // TODO Передавать id в конструкторе (нужен новый класс)
    
    exitButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        for(JepListener exitListener: exitListeners) {
          exitListener.handleEvent(new JepEvent());
        }
      }
    });
    
    exitButton.setStyleName(null);
    exitButton.getElement().getStyle().setCursor(Cursor.POINTER);
    exitButton.setTitle(JepTexts.button_exit_alt());
    
    entrancePanel.add(exitButton);
    entrancePanel.setCellWidth(exitButton, 15 + BAR_UNIT.getType());
    
    entrancePanel.getElement().getStyle().setWidth(100, Unit.PCT);
    return entrancePanel;
  }

  /**
   * Создает надпись с именем пользователя.
   * @return Надпись с именем пользователя.
   */
  public static Label createUserNameLabel() {
    Label userNameLabel = new Label();
    userNameLabel.getElement().setId(JepRiaAutomationConstant.LOGGED_IN_USER_ID); // TODO Передавать id в конструкторе (нужен новый класс)
    
    userNameLabel.setHeight(BAR_HEIGHT + BAR_UNIT.getType());
    userNameLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    Style style = userNameLabel.getElement().getStyle();
    style.setFontSize(16, BAR_UNIT);
    userNameLabel.getElement().getStyle().setProperty("fontFamily", "Times New Roman");
    return userNameLabel;
  }

  /**
   * Установка/удаление виджета в центральную область.
   * 
   * @param newWidget устанавливаемый виджет
   */
  public void setBody(Widget newWidget) {
    // Если передана "команда" удалить ContentWidget - newWidget == null - удаляем виджет.
    if(newWidget == null) {
      clearContentWidget();
    } 
    // Если передан непустой новый виджет, то сравним его с ContentWidget : если он является отличным от переданного, то заменим его новым;
    // если это тот же самый виджет, то оставим все без изменения.
    else {
      Widget currentWidget = getContentWidget();
      if(currentWidget != newWidget) {
        clearContentWidget();
        // Используем именно метод add(...), а не setContentWidget(...), т.к. после повторного использования последнего центральная панель
        // дополняется css свойством "display: none;" (станаовясь невидимой со всеми элементами на ней), которое затем нужно удалять 
        // "ручными манипуляциями".
        add(newWidget);
      }
    }
  }
  
  /**
   * Очистка области содержимого.
   */
  private void clearContentWidget(){
    Widget currentWidget = getContentWidget();
    if(currentWidget != null) {
      remove(currentWidget);
    }
  }
  
  /**
   * Добавление слушателя на кнопку выхода
   *
   * @param listener        слушатель
   */
  public void addExitListener(JepListener listener) {
    exitListeners.add(listener);
  }

  /**
   * Добавление слушателя при выборе модуля
   * 
   * @param moduleId        идентификатор модуля
   * @param listener        слушатель
   */
  public void addEnterModuleListener(String moduleId, final JepListener listener) {
    int index = indexOf(moduleId);
    
    tabs.addTabClickHandler(index, new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (tabs.getSelectedIndex() != currentSelectedIndex) {
          listener.handleEvent(new JepEvent());
        }
      }
    });
  }

  /**
   * Отображение имени пользователя
   * 
   * @param username        имя пользователя
   */
  public void setUsername(String username) {
    userNameLabel.setText(username);
  }

  /**
   * Активизация указанного модуля
   * 
   * @param moduleId        идентификатор модуля
   */
  public void selectModuleItem(String moduleId) {
    currentSelectedIndex = indexOf(moduleId);
    tabs.selectTab(currentSelectedIndex);
  }

  /**
   * Инициализация вкладок
   * 
   * @param moduleConfigurations        список модулей
   */
  public void setModuleItems(List<ModuleConfiguration> moduleConfigurations) {
    for (ModuleConfiguration moduleConfiguration: moduleConfigurations) {
      Label tabLabel = new Label(moduleConfiguration.title);
      tabLabel.getElement().setId(moduleConfiguration.moduleId);
      
      mapOfModule.put(moduleConfiguration.moduleId, tabLabel);
      tabs.add(new LayoutPanel(), tabLabel);
      
      tabLabel = tabs.getTabLabel(indexOf(moduleConfiguration.moduleId));
      
      Style style = tabLabel.getElement().getStyle();
      style.setColor("#15428B");
      tabLabel.getElement().addClassName(MAIN_FONT_STYLE);
      style.setFontSize(11, BAR_UNIT);
      
      tabLabel.getParent().addStyleName(TAB_LAYOUT_PANEL_TAB_STYLE);    
    }
    
  }
  
  /**
   * Отображать вкладки для выбранных модулей
   * 
   * @param moduleIds      список отображаемых модулей
   */
  public void showModuleTabs(String[] moduleIds) {
    List<String> visibleModules = !JepRiaUtil.isEmpty(moduleIds) ? new ArrayList<String>(Arrays.asList(moduleIds)) : new ArrayList<String>();
    for (Iterator<String> iter = mapOfModule.keySet().iterator(); iter.hasNext(); ){
      String moduleId = iter.next();
      int index = indexOf(moduleId);
      Style tabStyle = tabs.getTab(index).getElement().getStyle();
      if (visibleModules.contains(moduleId)){
        tabStyle.clearDisplay();
      } else {
        tabStyle.setDisplay(Display.NONE);
      }
    }
  }
  
  /**
   * Получение индекса модуля по его идентификатору.<br/>
   * Особенности:<br/>
   * Если указанный идентификатор не найден в карте модулей, то выбрасывается {@link com.technology.jep.jepria.shared.exceptions.IdNotFoundException} 
   * 
   * @param moduleId      идентификатор модуля
   * @return индекс модуля
   */
  protected Integer indexOf(String moduleId){
    for (int i = 0; i < tabs.getWidgetCount(); i++) {
      Label label = tabs.getTabLabel(i);
      if (label.getElement().getId().equalsIgnoreCase(moduleId) && mapOfModule.get(moduleId).equals(label)) {
        return i;
      }
    }
    // Выбрасываем исключение, если данный модуль отсутствует
    throw new IdNotFoundException(JepClientUtil.substitute(JepTexts.mainTabPanel_idNotFoundError(), moduleId));
  }
  
  /**
   * Получение названия модуля по его индексу
   * 
   * @param index      индекс модуля
   * @return наименование модуля
   */
  protected String getModuleNameByIndex(int index){
    Integer counter = 0;
    for (Iterator<Label> iter = mapOfModule.values().iterator(); iter.hasNext(); ){
      if (counter.equals(index)){
        return iter.next().getText();
      }
      counter++;
    }
    return null;
  }
}
