package com.technology.jep.jepria.client.ui.main;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ModuleItem;
import com.technology.jep.jepria.client.ui.JepActivity;
import com.technology.jep.jepria.client.ui.main.widget.MainTabPanel;
import com.technology.jep.jepria.client.widget.event.JepListener;

public class MainViewImpl implements MainView {
  
  protected MainTabPanel tabPanel;
  protected JepActivity presenter;
  
  public MainViewImpl() {
    tabPanel = new MainTabPanel();
  }

  @Override
  public void setPresenter(JepActivity presenter) {
    this.presenter = presenter;
  }
  
  @Override
  public void setWidget(Widget widget) {
    tabPanel = (MainTabPanel)widget;
  }

  @Override
  public Widget asWidget() {
    return tabPanel;
  }

  /**
   * Установка/удаление виджета в центральную область.
   * 
   * @param newWidget устанавливаемый виджет
   */
  @Override
  public void setBody(Widget newWidget) {
    tabPanel.setBody(newWidget);
  }
  
  /**
   * Добавление слушателя на кнопку выхода
   *
   * @param listener        слушатель
   */
  @Override
  public void addExitListener(JepListener listener) {
    tabPanel.addExitListener(listener);
  }

  /**
   * Добавление слушателя при выборе модуля
   * 
   * @param moduleId        идентификатор модуля
   * @param listener        слушатель
   */
  @Override
  public void addEnterModuleListener(String moduleId, final JepListener listener) {
    tabPanel.addEnterModuleListener(moduleId, listener);
  }

  /**
   * Отображение имени пользователя
   * 
   * @param username        имя пользователя
   */
  @Override
  public void setUsername(String username) {
    tabPanel.setUsername(username);
  }

  /**
   * Активизация указанного модуля
   * 
   * @param moduleId        идентификатор модуля
   */
  @Override
  public void selectModuleItem(String moduleId) {
    tabPanel.selectModuleItem(moduleId);
  }

  /**
   * Инициализация вкладок
   * 
   * @param moduleIds        список идентификатор модулей
   * @param moduleItemTitles    список наименований модулей
   */
  @Override
  public void setModuleItems(ModuleItem[] moduleItems) {
    tabPanel.setModuleItems(moduleItems);
  }
  
  /**
   * Отображать вкладки для выбранных модулей
   * 
   * @param moduleIds      список отображаемых модулей
   */
  @Override
  public void showModuleTabs(String[] moduleIds) {
    tabPanel.showModuleTabs(moduleIds);
  }
  
}
