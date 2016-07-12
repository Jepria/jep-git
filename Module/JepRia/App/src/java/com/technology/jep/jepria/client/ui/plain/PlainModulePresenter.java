package com.technology.jep.jepria.client.ui.plain;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.technology.jep.jepria.client.history.place.PlainPlaceController;
import com.technology.jep.jepria.client.history.scope.JepScope;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.event.EnterModuleEvent;
import com.technology.jep.jepria.client.ui.eventbus.event.ExitScopeEvent;
import com.technology.jep.jepria.client.ui.eventbus.event.UpdateScopeEvent;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.PrepareReportEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SearchEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SetCurrentRecordEvent;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.report.JepReportParameters;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.jepria.shared.service.report.JepReportServiceAsync;

public class PlainModulePresenter<V extends PlainModuleView, E extends PlainEventBus, S extends JepDataServiceAsync, 
    F extends PlainClientFactory<E, S>>
  extends JepPresenter<E, F>
    implements
      EnterModuleEvent.Handler,
      ExitScopeEvent.Handler,
      SearchEvent.Handler,
      SetCurrentRecordEvent.Handler,
      UpdateScopeEvent.Handler,
      PrepareReportEvent.Handler{
  
  protected String moduleId;
  
  protected V view;
  protected S service;
  
  protected MainEventBus mainEventBus;
  
  protected PlainPlaceController placeController;
  
  protected JepRecord currentRecord;

  public PlainModulePresenter(String moduleId, Place place, F clientFactory) {
    super(place, clientFactory);
    
    view = (V)clientFactory.getModuleView();
    service = (S)clientFactory.getService();
    mainEventBus = (MainEventBus) clientFactory.getMainClientFactory().getEventBus();

    this.moduleId = moduleId;
    placeController = clientFactory.getPlaceController();
  }

  public void start(AcceptsOneWidget container, EventBus eventBus) {
    // Подписка activity-презентера на события EventBus.
    eventBus.addHandler(EnterModuleEvent.TYPE, this);
    eventBus.addHandler(SearchEvent.TYPE, this);
    eventBus.addHandler(SetCurrentRecordEvent.TYPE, this);
    eventBus.addHandler(PrepareReportEvent.TYPE, this);

    // События, транслируемые главному модулю.
    eventBus.addHandler(UpdateScopeEvent.TYPE, this);
    eventBus.addHandler(ExitScopeEvent.TYPE, this);
    
    // "Привязка" элементов представления к функционалу презентера.
    bind();
    // Переведем презентер модуля в заданный режим.
    changeWorkstate(place);
  }

  /**
   * Метод используется для перекрытия потомками с целью "привязки" элементов представления к функционалу презентера.
   */
  protected void bind() {
    view.setPresenter(this);
  }

  public void onUpdateScope(UpdateScopeEvent event) {
    mainEventBus.updateScope(event); // Переадресация обработки главному модулю.
  }
  
  public void onExitScope(ExitScopeEvent event) {
    if(JepScopeStack.instance.size() > 1) {
      JepScopeStack.instance.pop();

      mainEventBus.exitScope(event); // Переадресация обработки главному модулю.
    } else { // Не должы сюда попадать, поскольку кнопка up на верхнем уровне не должна быть доступна
      Log.error(".onExitScope(): scope == null, Не должы сюда попадать");
    }
  }

  /**
   * Обработчик события перехода на модуль.
   *
   * @param event событие перехода на модуль
   */
  public void onEnterModule(EnterModuleEvent event) {
    Log.trace(this.getClass() + ".onEnterModule: moduleId = " + event.getModuleId());
    // Установим главный виджет(-контейнер) приложения.
    setMainView();
    // Выполним действия по настройке перехода на вышележащий уровень иерархии (на родительский уровень).
    eventBus.adjustExitScope();
    // Заполним модуль данными.
    fillData();
  }
  
  /**
   * Установка главного виджета(-контейнера) приложения.<br/>
   * В методе используется вызов вида : <code>mainEventBus.setMainView(clientFactory.getMainClientFactory().getMainView());</code> <br/>
   * При этом, при передаче <code>null</code> в качестве главного виджета приложения, текущий главный виджет удаляется с RootPanel'и.<br/>
   * Т.о., перегрузкой данного метода можно установить, при заходе на модуль приложения, любой главный виджет приложения или скрыть текущий.
   */
  protected void setMainView() {
    mainEventBus.setMainView(clientFactory.getMainClientFactory().getMainView());
  }
  
  /**
   * Подготавливает (получает) данные для отображения в модуле соответствующие текущему состоянию модуля 
   * {@link #_workstate} .
   */
  protected void fillData() {
    JepScope scope = JepScopeStack.instance.peek();
    
    if(VIEW_LIST.equals(_workstate) || SELECTED.equals(_workstate)) {
      fillList(scope);
    } else if(VIEW_DETAILS.equals(_workstate) || EDIT.equals(_workstate)) {
      fillFields(scope);
    }
  }
  
  /**
   * Обработчик события поиска.<br/>
   * 
   * Особенности:<br/>
   * Сохраняет поисковый шаблон в History.
   *
   * @param event событие поиска
   */
  public void onSearch(SearchEvent event) {
    PagingConfig pagingConfig = event.getPagingConfig();    
    JepRecord templateRecord = pagingConfig.getTemplateRecord();
    JepScope scope = JepScopeStack.instance.peek();
    scope.setTemplateProperties(templateRecord.getProperties());
  }  
  
  protected void onChangeWorkstate(WorkstateEnum workstate) {
    // Если переходим в режим поиска, то ... 
    if(SEARCH.equals(workstate)) {
      // ... сбросим поисковый шаблон в History .
      JepScope scope = JepScopeStack.instance.peek();
      scope.setTemplateProperties(null);
    }
  }
  
  /**
   * Заполнение списка списочной формы на основе данных History (на основе шаблона поиска).
   *
   * @param scope заданный уровень иерархии (History)
   */
  protected void fillList(JepScope scope) {
    // Создание шаблонной записи поиска.
    JepRecord templateRecord = new JepRecord();
    // Заполнение шаблонной записи поиска.
    templateRecord.setProperties(scope.getTemplateProperties());
    
    eventBus.search(new PagingConfig(templateRecord));
  }

  /**
   * Заполнение полей детальной формы на основе данных History (на основе первичного ключа).
   *
   * @param scope заданный уровень иерархии (History)
   */
  protected void fillFields(JepScope scope) {
    // Создание шаблонной записи поиска.
    JepRecord templateRecord = new JepRecord();
    // Заполнение шаблонной записи поиска.
    templateRecord.setProperties(scope.getPrimaryKey());
    
    eventBus.doGetRecord(new PagingConfig(templateRecord));
  }

  /**
   * Обработчик события установки (выбора) текущей записи.<br/>
   * 
   * Особенности:<br/>
   * Сохраняет текущую запись в качестве первичного ключа в History.
   *
   * @param event событие установки (выбора) текущей записи
   */
  public void onSetCurrentRecord(SetCurrentRecordEvent event) {
    JepScope scope = JepScopeStack.instance.peek();
    currentRecord = event.getCurrentRecord();
    if(currentRecord != null) {
      scope.setPrimaryKey(clientFactory.getRecordDefinition().buildPrimaryKeyMap(currentRecord));
    }
  }

  @Override
  public void onPrepareReport(PrepareReportEvent event) {
    JepReportParameters reportParameters = event.getReportParameters();
    String reportServlet =  event.getReportServlet();
    
    final String reportServletPath = reportServlet == null ? 
      "servlets/pdf" : 
      (reportServlet.startsWith("/") ? reportServlet.substring(1) : reportServlet);
    
    buildReportParametersFromRecord(reportParameters, currentRecord);
    
    JepClientUtil.showLoadingPanel(null, JepTexts.loadingPanel_reportPreparing());
    ((JepReportServiceAsync) clientFactory.getService()).prepareReport(reportParameters, new AsyncCallback<Void>() {
      public void onFailure(final Throwable th) {
        JepClientUtil.hideLoadingPanel();
        messageBox.showError(th);
      }
    
      public void onSuccess(Void result) {
        JepClientUtil.hideLoadingPanel();
        Window.open(GWT.getHostPageBaseURL() + reportServletPath, "_blank", "");
      }
    });    
  }
  
  protected void buildReportParametersFromRecord(JepReportParameters reportParameters, JepRecord record) {
    if(reportParameters.getParameterMap().size() == 0 && record != null) {
      reportParameters.setProperties(record.getProperties());
    }
  }
  
}
