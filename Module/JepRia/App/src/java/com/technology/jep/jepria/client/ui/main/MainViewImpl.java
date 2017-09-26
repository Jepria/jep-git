package com.technology.jep.jepria.client.ui.main;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.main.widget.MainTabPanel;
import com.technology.jep.jepria.client.widget.event.JepListener;

public abstract class MainViewImpl implements MainView {
  
  protected MainTabPanel tabPanel;
  protected MainModulePresenter<MainView, ?, ?, ?> presenter;
  
  public MainViewImpl() {
    tabPanel = new MainTabPanel();
    tabPanel.setModuleItems(getModuleConfigurations());
  }
  
  /**
   * Метод должен быть переопределен в наследниках и возвращать список конфигураций модулей-вкладок.
   */
  protected abstract List<ModuleConfiguration> getModuleConfigurations();

  @Override
  public void setPresenter(MainModulePresenter<MainView, ?, ?, ?> presenter) {
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
   * Отображать вкладки для выбранных модулей
   * 
   * @param moduleIds      список отображаемых модулей
   */
  @Override
  public void showModuleTabs(String[] moduleIds) {
    tabPanel.showModuleTabs(moduleIds);
  }
  
}
