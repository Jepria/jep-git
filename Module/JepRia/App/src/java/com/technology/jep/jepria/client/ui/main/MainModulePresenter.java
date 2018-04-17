package com.technology.jep.jepria.client.ui.main;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.APPLICATION_SLOT;
import static com.technology.jep.jepria.client.JepRiaClientConstant.ENTRY_MODULE_NAME_REQUEST_PARAMETER;
import static com.technology.jep.jepria.client.JepRiaClientConstant.ENTRY_STATE_NAME_REQUEST_PARAMETER;
import static com.technology.jep.jepria.shared.JepRiaConstant.JEP_USER_NAME_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.JEP_USER_ROLES_FIELD_NAME;
import static com.technology.jep.jepria.shared.field.JepFieldNames.OPERATOR_ID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.async.LoadAsyncCallback;
import com.technology.jep.jepria.client.entrance.Entrance;
import com.technology.jep.jepria.client.history.place.PlainPlaceController;
import com.technology.jep.jepria.client.history.scope.JepScope;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.security.ClientSecurity;
import com.technology.jep.jepria.client.ui.JepActivity;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.event.EnterModuleEvent;
import com.technology.jep.jepria.client.ui.eventbus.event.ExitScopeEvent;
import com.technology.jep.jepria.client.ui.eventbus.event.UpdateScopeEvent;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.main.event.EnterFromHistoryEvent;
import com.technology.jep.jepria.client.ui.eventbus.main.event.SetMainViewBodyEvent;
import com.technology.jep.jepria.client.ui.eventbus.main.event.SetMainViewEvent;
import com.technology.jep.jepria.client.ui.eventbus.main.event.StartEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.shared.dto.JepDto;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * Базовый класс презентера главного модуля.<br/>
 * <br/>
 * Пример презентера главного модуля прикладного приложения:
 * <pre>
 * ...
 * public class &lt;Application Name&gt;MainModulePresenter&lt;E extends MainEventBus, S extends JepMainServiceAsync&gt; 
 *   extends MainModulePresenter&lt;MainView, E, S, MainClientFactory&lt;E, S&gt;&gt; {
 *
 *   public &lt;Application Name&gt;MainModulePresenter(MainClientFactory&lt;E, S&gt; clientFactory) {
 *     super(clientFactory);
 *
 *     addModuleProtection(&lt;MODULE NAME 1&gt;_MODULE_ID, "&lt;Role 1&gt;, &lt;Role 2&gt;");
 *     addModuleProtection(&lt;MODULE NAME 2&gt;_MODULE_ID, "&lt;Role 3&gt;, &lt;Role 4&gt;, &lt;Role 5&gt;");
 *     ...
 *     addModuleProtection(&lt;MODULE NAME N&gt;_MODULE_ID, "&lt;Role N&gt;");
 *
 *     setProtectedModuleItemsVisibility(false);
 *   }
 *
 * }
 * </pre>
 */
public abstract class MainModulePresenter<V extends MainView, E extends MainEventBus, S extends JepMainServiceAsync, 
    F extends MainClientFactory<E, S>>
  extends JepActivity<E, F>
    implements
      StartEvent.Handler,
      UpdateScopeEvent.Handler,
      ExitScopeEvent.Handler,
      EnterFromHistoryEvent.Handler,
      EnterModuleEvent.Handler,
      SetMainViewBodyEvent.Handler,
      SetMainViewEvent.Handler {

  /**
   * Карта соответствия <moduleId, множество ролей>
   */
  protected Map<String, List<String>> accessMap = new HashMap<String, List<String>>();
  
  private boolean isProtectedModuleVisible = false;

  protected V view;
  protected S service;
  
  /**
   * Создает презентер главного модуля.
   * 
   * @param clientFactory клиентская фабрика главного модуля
   */
  public MainModulePresenter(F clientFactory) {
    super(clientFactory);
    
    view = (V)clientFactory.getMainView();
    service = (S)clientFactory.getMainService();
  }

  public void start(AcceptsOneWidget container, EventBus eventBus) {
    // Подписка activity-презентера на события EventBus.
    eventBus.addHandler(StartEvent.TYPE, this);
    
    eventBus.addHandler(UpdateScopeEvent.TYPE, this);
    eventBus.addHandler(ExitScopeEvent.TYPE, this);
    eventBus.addHandler(EnterFromHistoryEvent.TYPE, this);
    eventBus.addHandler(EnterModuleEvent.TYPE, this);
    eventBus.addHandler(SetMainViewBodyEvent.TYPE, this);
    eventBus.addHandler(SetMainViewEvent.TYPE, this);
    
    bind();
  }

  protected void bind() {

    Entrance.setService(service);
    
    // Определим реакцию на событие выхода из приложения.
    view.addExitListener(new JepListener() {
      public void handleEvent(JepEvent event) {
        Entrance.logout();
      }
    });
    
    for (String moduleId: clientFactory.getModuleIds()) {
      bindModule(moduleId);
    }
  }
  
  private void bindModule(final String moduleId) {
    view.addEnterModuleListener(moduleId, new JepListener() {
      public void handleEvent(JepEvent event) {
        eventBus.enterModule(moduleId, true);
      }
    });
  }
  
  /**
   * Возвращает состояние по умолчанию при входе в приложение. <br/>
   * Имя модуля передается для использования в потомках при переопределении.
   * @param entryModuleName Имя модуля, в который осуществлен вход.
   * @return Состояние по умолчанию при входе в приложение.
   */
  protected WorkstateEnum getDefaultWorkState(String entryModuleName) {
    return WorkstateEnum.SEARCH;
  }
  
  /**
   * Обработчик события старта Main-модуля (приложения).
   *
   * @param event событие старта Main-модуля (приложения)
   */
  public void onStart(StartEvent event) {
    JepScope scope = JepScopeStack.instance.peek();
    if (scope == null) {
      String entryModuleName = Location.getParameter(ENTRY_MODULE_NAME_REQUEST_PARAMETER);
      entryModuleName = entryModuleName == null ? clientFactory.getModuleIds().get(0) : entryModuleName.trim();

      String entryStateName = Location.getParameter(ENTRY_STATE_NAME_REQUEST_PARAMETER);
      WorkstateEnum entryModuleState = entryStateName == null ? getDefaultWorkState(entryModuleName) : WorkstateEnum.fromString(entryStateName.trim());

      JepScope startScope = new JepScope(new String[] {entryModuleName}, new WorkstateEnum[] {entryModuleState});
      
      if (clientFactory.getModuleIds().contains(startScope.getActiveModuleId())) {
        JepScopeStack.instance.push(startScope);
      } else {  // Кривой Url, устанавливаем умолчательное состояние.
        JepScopeStack.instance.setDefaultState();
      }
    }
    
    loadUserDataAndEnterScope();
  }
  
  protected void loadUserDataAndEnterScope() {
    service.getUserData(new JepAsyncCallback<JepDto>() {
      public void onFailure(Throwable caught) {
        Log.error(caught.getLocalizedMessage(), caught);
        clientFactory.getExceptionManager().handleException(caught);
      }
      
      public void onSuccess(JepDto userData) {
        Log.trace("MainModulePresenter.getUserData().onSuccess(): userData = " + userData);
        ClientSecurity.instance.setOperatorId((Integer) userData.get(OPERATOR_ID));
        ClientSecurity.instance.setRoles((List<String>) userData.get(JEP_USER_ROLES_FIELD_NAME));
        enterScope((String)userData.get(JEP_USER_NAME_FIELD_NAME));
      }
    });
  }

  protected void enterScope(String username) {
    ClientSecurity.instance.setUsername(username);
    view.setUsername(username);
    JepScopeStack.instance.setUserEntered();
    
    JepScope scope = JepScopeStack.instance.peek();
    eventBus.enterModule(scope.getActiveModuleId());
  }
  
  /**
   * Обработчик обновления области видимости данных (набора закладок и контекста).
   * 
   * @param updateScopeEvent событие обновления области видимости
   */
  public void onUpdateScope(UpdateScopeEvent updateScopeEvent) {
    JepScope scope = updateScopeEvent.getScope();

    view.showModuleTabs(getModules(scope));
    view.selectModuleItem(scope.getActiveModuleId());
  }
  
  public void onExitScope(ExitScopeEvent exitScopeEvent) {
    JepScopeStack.instance.setExitScope(true); // TODO Сделать это более естественным образом
    try {
      JepScope scope = JepScopeStack.instance.peek();
      eventBus.enterModule(scope.getActiveModuleId(), true);
    } catch(Throwable th) {
      th.printStackTrace();
      messageBox.showError(th);
    } finally {
      JepScopeStack.instance.setExitScope(false); // TODO Сделать это более естественным образом
    }
  }

  private String[] getModules(JepScope scope) {
    return isProtectedModuleVisible ? scope.getModuleIds() : getAccessibleModules(scope).toArray(new String[0]);
  }

  protected Set<String> getAccessibleModules(JepScope scope) {
    Set<String> accessibleModules = new HashSet<String>();
    String[] modules = scope.getModuleIds();
    for (int i = 0; i < modules.length; i++) {
      String moduleId = modules[i];
      if (ClientSecurity.instance.isUserHaveRoles(accessMap.get(moduleId))) {
        accessibleModules.add(moduleId);
      }
    }
    
    return accessibleModules;
  }

  private Set<String> getAccessibleModules() {
    return getAccessibleModules(JepScopeStack.instance.peek());
  }

  public void setProtectedModuleItemsVisibility(boolean isProtectedModuleVisible) {
    this.isProtectedModuleVisible = isProtectedModuleVisible;
  }
  
  /**
   * Обработчик входа в модуль из History (например: при использовании кнопок Back/Forward браузера).
   *
   * @param event событие входа в модуль
   */
  public void onEnterFromHistory(EnterFromHistoryEvent event) {
    // Получим состояние, в которое нужно перевести приложение.
    JepScope scope = JepScopeStack.instance.peek();
    // Получим идентификатор модуля, на который переходим.
    String moduleId = scope.getActiveModuleId();
    
    // Если у пользователя существуют права на отображение запрошенного модуля, то ...
    if (checkAccess(moduleId)) {
      // Отобразим закладки модулей исходя из текущего состояния.
      view.showModuleTabs(getModules(scope));
      // Сделаем активной закладку запрошенного модуля.
      view.selectModuleItem(moduleId);
      // Запустим (загрузим и переключимся на) запрошенный модуль в запрошенном состоянии 
      // с указанием, что запуск происходит в обработчике History.
      startModule(moduleId, event.getPlace(), true);
    }
  }
  
  /**
   * Обработчик входа в модуль из приложения (например: при использовании закладок/Tab'ов MainView).
   *
   * @param event событие входа в модуль
   */
  public void onEnterModule(EnterModuleEvent event) {
    // Получим идентификатор модуля, в который нужно зайти.
    String moduleId = event.getModuleId();
    
    // Если у пользователя существуют права на отображение запрошенного модуля, то ...
    if (checkAccess(moduleId)) {
      // Получим ТЕКУЩЕЕ (до переключения) состояние приложения.
      JepScope scope = JepScopeStack.instance.peek();
      // Если ТЕКУЩИЙ активный модуль - главный, а мы переключаемся на НЕ главный (на подчиненный), то ...
      if (scope.isMainActive() && !scope.isMain(moduleId)) {
        // Запомним внешний ключ дочернего модуля равным первичному ключу главного модуля.
        scope.setForeignKey(scope.getPrimaryKey());
        // Установим шаблон поиска дочернего модуля равным первичному ключу главного модуля.
        scope.setTemplateProperties(scope.getPrimaryKey());
        
      // Если ТЕКУЩИЙ активный модуль - НЕ главный (подчиненный), а мы переключаемся на главный, то ...
      } else if (!scope.isMainActive() && scope.isMain(moduleId)) {
        // Восстановим первичный ключ главного модуля равным внешнему ключу дочернего модуля.
        scope.setPrimaryKey(scope.getForeignKey());
        // Сбросим неактуальный для главного модуля внешний ключ.
        scope.setForeignKey(new HashMap<String, Object>());
        // Сбросим неактуальный для главного модуля шаблон поиска.
        scope.setTemplateProperties(new HashMap<String, Object>());
      }
      // Сделаем текущим модуль, на который необходимо перейти.
      scope.setActiveModuleId(moduleId);
      
      // Получим состояние, в котором должен находиться модуль.
      Place place = event.getPlace();
      // Если требуемое состояние не установлено.
      if (place == null) {
        // Если заходим в модуль, то НЕ меняем его состояние (оставляем текущее состояние).
        // Если состояние не задано, то scope.getCurrentWorkstate() возвращает состояние модуля, 
        // в котором он остался на момент выхода. 
        // Если это первый вход в модуль, то возвращает детальную форму в режиме поиска для главного модуля
        // и списочную фому для дочернего модуля.
        WorkstateEnum workstate = scope.getCurrentWorkstate();
        place = JepClientUtil.workstateToPlace(workstate);
      }
      
      // Отобразим закладки модулей исходя из текущего состояния.
      view.showModuleTabs(getModules(scope));
      // Сделаем активной закладку запрошенного модуля.
      view.selectModuleItem(moduleId);
      // Запустим (загрузим и переключимся на) запрошенный модуль в запрошенном состоянии 
      // с указанием, что запуск происходит НЕ в обработчике History.
      startModule(moduleId, place, false);
    }
  }
  
  /**
   * Проверяет наличие прав у пользователя на отображение модуля.
   *
   * @param moduleId идентификатор модуля
   * @return true - у пользователя есть права на отображение модуля, false - в противном случае
   */
  private boolean checkAccess(String moduleId) {
    boolean result = getAccessibleModules().contains(moduleId);
    if (Boolean.FALSE.equals(result)) {
      // Если нет доступа к модулю, то скрываем панель загрузки и сообщаем об ошибке.
      JepClientUtil.hideLoadingPanel();
      messageBox.alert(JepTexts.errors_security_title(), JepTexts.errors_security_enterModule() + moduleId);
    }
    return result;
  }
  
  /**
   * Загружает модуль и отправляет события иннициализации модулю перед отображением.
   *
   * @param moduleId идентификатор модуля
   * @param place Place, на который нужно перейти после загрузки модуля
   * @param isFromHistory признак запуска модуля из обработчика History (при использовании кнопок Back/Forward браузера)
   */
  protected void startModule(final String moduleId, final Place place, final boolean isFromHistory) {
    clientFactory.getPlainClientFactory(moduleId, 
      new LoadAsyncCallback<PlainClientFactory<PlainEventBus, JepDataServiceAsync>>() {
        public void onSuccessLoad(PlainClientFactory<PlainEventBus, JepDataServiceAsync> plainClientFactory) {

          PlainPlaceController<?, ?, ?> plainPlaceController = (PlainPlaceController<?, ?, ?>)plainClientFactory.getPlaceController();
          
          plainPlaceController.setWriteHistory(!isFromHistory);
          plainPlaceController.goTo(place); // Синхронные вызовы: настройка состояния и запись истории (в зависимости от выше выставленного параметра).
          
          JepClientUtil.hideLoadingPanel();
          
          PlainEventBus plainEventBus = plainClientFactory.getEventBus();
          
          plainEventBus.fireEvent(new EnterModuleEvent(moduleId));
          // TODO: проблема в том, что в недрах обработчиков EnterModuleEvent вызывается plainPlaceController.goTo(place), поэтому (пока) включение
          // истории осуществляем после обработчиков/отправки события EnterModuleEvent.
          plainPlaceController.setWriteHistory(true);
        }
      }
    );
  }

  public void onSetMainView(SetMainViewEvent event) {
    IsWidget mainView = event.getMainView();
    Widget newWidget = mainView == null ? null : mainView.asWidget();

    Log.trace(this.getClass() + ".onSetMainView: MainView is " + (newWidget == null ? "null" : "not null"));
    
    RootPanel jepRiaAppSlot = RootPanel.get(APPLICATION_SLOT);
    RootPanel appSlotPanel = jepRiaAppSlot == null ? RootPanel.get() : jepRiaAppSlot;
    
    int widgetCount = appSlotPanel.getWidgetCount();
    
    // Если передана "команда" удалить все - newWidget == null - удалим все виджеты с RootPanel'и.
    if(newWidget == null) {
      clearRootPanel(appSlotPanel);
    } 
    // Если передан непустой новый виджет и на RootPanel'и один виджет, то сравним : если он является отличным от переданного, то заменим его новым;
    // если это тот же самый виджет, то оставим все без изменения.
    else if(widgetCount == 1) {
      Widget currentWidget = appSlotPanel.getWidget(0);
      if(currentWidget != newWidget) {
        appSlotPanel.remove(0);
        appSlotPanel.add(newWidget);
      }
    }
    // Иначе: если передан непустой новый виджет, а на RootPanel'и несколько виджетов (или ничего), то удалим все виджеты и установим один новый.
    else {
      clearRootPanel(appSlotPanel);
      appSlotPanel.add(newWidget);
    }
  }
  
  private void clearRootPanel(RootPanel rootPanel) {
    int count = rootPanel.getWidgetCount();
    
    for(int index = 0; index < count; index++) {
      rootPanel.remove(index);
    }
  }
  
  public void onSetMainViewBody(SetMainViewBodyEvent event) {
    Log.trace(this.getClass() + ".onSetMainViewBody: MainViewBody is " + (event.getBody() == null ? "null" : "not null"));
    view.setBody(event.getBody());
  }
  
  /**
   * Определяет роли пользователя, необходимые для доступа к модулю. <br/>
   * Оставлен для обратной совместимости, необходимо использовать {@link #addModuleProtection(String, List)}.
   *
   * @param moduleId защищаемый модуль
   * @param strRoles список ролей через запятую, наличие которых необходимо для доступа к модулю
   */
  @Deprecated
  protected void addModuleProtection(String moduleId, String strRoles) {
    accessMap.put(moduleId, ClientSecurity.getRoles(strRoles));
  }
  
  /**
   * Определяет роли пользователя, необходимые для доступа к модулю.
   *
   * @param moduleId защищаемый модуль
   * @param roles список ролей, наличие которых необходимо для доступа к модулю
   */
  protected void addModuleProtection(String moduleId, List<String> roles) {
    accessMap.put(moduleId, roles);
  }
}
