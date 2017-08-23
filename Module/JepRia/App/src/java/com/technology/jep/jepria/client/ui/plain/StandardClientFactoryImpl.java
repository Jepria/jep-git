package com.technology.jep.jepria.client.ui.plain;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormActivityMapper;
import com.technology.jep.jepria.client.ui.form.list.ListFormActivityMapper;
import com.technology.jep.jepria.client.ui.statusbar.StatusBarActivityMapper;
import com.technology.jep.jepria.client.ui.statusbar.StatusBarPresenter;
import com.technology.jep.jepria.client.ui.statusbar.StatusBarView;
import com.technology.jep.jepria.client.ui.statusbar.StatusBarViewImpl;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarActivityMapper;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarPresenter;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarView;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarViewImpl;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * Реализация клиентской фабрики стандартного (списочная и детальная формы) модуля.
 */
abstract public class StandardClientFactoryImpl<E extends PlainEventBus, S extends JepDataServiceAsync> 
  extends PlainClientFactoryImpl<E, S> implements StandardClientFactory<E, S> {

  protected final String moduleId;
  
  /**
   * Представление инструментальной панели.
   */
  protected IsWidget toolBarView = null;

  /**
   * Представление панели состояния.
   */
  protected IsWidget statusBarView = null;

  /**
   * Создает клиентскую фабрику модуля с заданным определением данных модуля.
   *
   * @param recordDefinition определение данных модуля
   * @deprecated Use {@link #StandardClientFactoryImpl(String, JepRecordDefinition)} instead.
   */
  @Deprecated
  public StandardClientFactoryImpl(JepRecordDefinition recordDefinition) {
    this(null, recordDefinition);
  }
  
  /**
   * Создает клиентскую фабрику модуля с заданным определением данных модуля и ID модуля.
   *
   * @param moduleId ID модуля
   * @param recordDefinition определение данных модуля
   */
  public StandardClientFactoryImpl(String moduleId, JepRecordDefinition recordDefinition) {
    super(recordDefinition);
    this.moduleId = moduleId;
    initActivityMappers(this);
  }

  /**
   * Получение представления (View) стандартного модуля.<br/>
   * Если объект еще не создан, то метод создает его и возвращает созданный объект. 
   *
   * @return представление (View) стандартного модуля
   */
  public IsWidget getModuleView() {
    if(moduleView == null) {
      moduleView = new StandardModuleViewImpl();
    }
    return moduleView;
  }

  /**
   * Получение представления (View) инструментальной панели.
   *
   * @return представление (View) инструментальной панели
   */
  public IsWidget getToolBarView() {
    if(toolBarView == null) {
      toolBarView = new ToolBarViewImpl();
    }
    return toolBarView;
  }

  /**
   * Получение представления (View) панели состояния.
   *
   * @return представление (View) панели состояния
   */
  public IsWidget getStatusBarView() {
    if(statusBarView == null) {
      statusBarView = new StatusBarViewImpl(moduleId);
    }
    return statusBarView;
  }

  /**
   * Создание презентера инструментальной панели.
   *
   * @return презентер инструментальной панели
   */
  public JepPresenter<?,?> createToolBarPresenter(Place place) {
    return new ToolBarPresenter<ToolBarView, E, S, StandardClientFactory<E,S>>(place, this);
  }
  
  /**
   * Создание презентера панели состояния.
   *
   * @return презентер панели состояния
   */
  public JepPresenter<?,?> createStatusBarPresenter(Place place) {
    return new StatusBarPresenter<StatusBarView, E, S, StandardClientFactory<E,S>>(place, this);
  }
  
  /**
   * Иннициализация ActivityMapper'ов и ActivityManager'ов.<br/>
   * Необходимо для возможности соответствующих презентеров (Activity в понятиях GWT) прослушивать, подписываться и обрабатывать события,
   * с которыми работает EventBus.
   *
   * @param clientFactory клиентская фабрика модуля
   */
  protected void initActivityMappers(PlainClientFactory<E, S> clientFactory) {
    super.initActivityMappers(clientFactory);
    
    /*
     * Создадим ActivityMapper и ActivityManager для детальной формы.
     */
    ActivityManager detailFormActivityManager = new ActivityManager(
      new DetailFormActivityMapper((StandardClientFactory<E, S>)clientFactory)
      , clientFactory.getEventBus()
    );

    // Необходимо для предотвращения де-регистрации в EventBus и сбором garbage collection (смотри описание метода в JavaDoc GWT).
    detailFormActivityManager.setDisplay(new AcceptsOneWidget() {
      public void setWidget(IsWidget widget) {}
    });
    
    /*
     * Создадим ActivityMapper и ActivityManager для списочной формы.
     */
    ActivityManager listFormActivityManager = new ActivityManager(
      new ListFormActivityMapper((StandardClientFactory<E, S>)clientFactory)
      , clientFactory.getEventBus()
    );

    // Необходимо для предотвращения де-регистрации в EventBus и сбором garbage collection (смотри описание метода в JavaDoc GWT).
    listFormActivityManager.setDisplay(new AcceptsOneWidget() {
      public void setWidget(IsWidget widget) {}
    });
    
    /*
     * Создадим ActivityMapper и ActivityManager для инструментальной панели.
     */
    ActivityManager toolBarActivityManager = new ActivityManager(
      new ToolBarActivityMapper((StandardClientFactory<E, S>)clientFactory)
      , clientFactory.getEventBus()
    );

    // Необходимо для предотвращения де-регистрации в EventBus и сбором garbage collection (смотри описание метода в JavaDoc GWT).
    toolBarActivityManager.setDisplay(new AcceptsOneWidget() {
      public void setWidget(IsWidget widget) {}
    });
    
    /*
     * Создадим ActivityMapper и ActivityManager для панели состояния.
     */
    ActivityManager statusBarActivityManager = new ActivityManager(
      new StatusBarActivityMapper((StandardClientFactory<E, S>)clientFactory)
      , clientFactory.getEventBus()
    );

    // Необходимо для предотвращения де-регистрации в EventBus и сбором garbage collection (смотри описание метода в JavaDoc GWT).
    statusBarActivityManager.setDisplay(new AcceptsOneWidget() {
      public void setWidget(IsWidget widget) {}
    });
    
  }
  
  @Override
  public JepPresenter<?,?> createPlainModulePresenter(Place place) {
    return new StandardModulePresenter<StandardModuleView, E, S, StandardClientFactory<E,S>>(moduleId, place, this);
  }
  
}
