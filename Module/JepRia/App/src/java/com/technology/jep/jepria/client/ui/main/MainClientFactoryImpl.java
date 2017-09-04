package com.technology.jep.jepria.client.ui.main;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ModuleItem;
import com.technology.jep.jepria.client.history.place.JepSearchPlace;
import com.technology.jep.jepria.client.history.place.MainPlaceController;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.ClientFactoryImpl;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.shared.service.JepMainService;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

/**
 * Базовый класс реализации для клиентской фабрики приложения.<br/>
 * <br/>
 * Пример реализации клиентской фабрики прикладного приложения:
 * <pre>
 * ...
 * public class &lt;Application Name&gt;ClientFactoryImpl&lt;E extends MainEventBus, S extends JepMainServiceAsync&gt;
 *   extends JepMainClientFactoryImpl&lt;E&gt;
 *     implements JepMainClientFactory&lt;E&gt; {
 *
 *   static public JepMainClientFactory&lt;MainEventBus&gt; getInstance() {
 *     if(instance == null) {
 *       instance = new &lt;Application Name&gt;ClientFactoryImpl&lt;MainEventBus, JepMainServiceAsync&gt;();
 *     }
 *     return instance;
 *   }
 * 
 *   private &lt;Application Name&gt;ClientFactoryImpl() {
 *     super(new String[] {
 *       &lt;MODULE NAME 1&gt;_MODULE_ID
 *       , &lt;MODULE NAME 2&gt;_MODULE_ID
 *       ...
 *       , &lt;MODULE NAME N&gt;_MODULE_ID
 *     },
 *     new String[] {
 *       &lt;application Name&gt;Text.submodule_&lt;module Name 1&gt;_title()
 *       , &lt;application Name&gt;Text.submodule_&lt;module Name 2&gt;_title()
 *       ...
 *       , &lt;application Name&gt;Text.submodule_&lt;module Name N&gt;_title()
 *     });
 *     
 *     eventBus = new &lt;Application Name&gt;MainEventBus(this);
 *     placeController = new MainPlaceController((MainEventBus)eventBus, this);
 *     
 *     initActivityMappers(this);
 *   }
 *   
 *   public Activity createMainModulePresenter() {
 *     return new &lt;Application Name&gt;MainModulePresenter(this);
 *   }
 *   
 *   public void getPlainClientFactory(String moduleId, final LoadAsyncCallback&lt;JepBaseClientFactory&lt;PlainEventBus&gt;&gt; callback) {
 *     if(&lt;MODULE NAME 1&gt;_MODULE_ID.equals(moduleId)) {
 *       GWT.runAsync(new LoadPlainClientFactory(callback) {
 *         public JepBaseClientFactory&lt;PlainEventBus&gt; getPlainClientFactory() {
 *           return &lt;Module Name 1&gt;ClientFactoryImpl.getInstance();
 *         }
 *       });
 *     } else if(&lt;MODULE NAME 2&gt;_MODULE_ID.equals(moduleId)) {
 *       GWT.runAsync(new LoadPlainClientFactory(callback) {
 *         public JepBaseClientFactory&lt;PlainEventBus&gt; getPlainClientFactory() {
 *           return &lt;Module Name 2&gt;ClientFactoryImpl.getInstance();
 *         }
 *       });
 *       ...
 *     } else if(&lt;MODULE NAME N&gt;_MODULE_ID.equals(moduleId)) {
 *       GWT.runAsync(new LoadPlainClientFactory(callback) {
 *         public JepBaseClientFactory&lt;PlainEventBus&gt; getPlainClientFactory() {
 *           return &lt;Module Name N&gt;ClientFactoryImpl.getInstance();
 *         }
 *       });
 *     }
 *     
 *   }
 * 
 * }
 * </pre>
 */
abstract public class MainClientFactoryImpl<E extends MainEventBus, S extends JepMainServiceAsync> 
  extends ClientFactoryImpl<E> implements MainClientFactory<E, S> {
  
  /**
   * Поле для реализации singleton'а клиентской фабрики приложения.
   */
  public static MainClientFactoryImpl<MainEventBus, JepMainServiceAsync> instance = null;
  
  /**
   * Главное представление приложения.
   */
  protected IsWidget mainView = null;

  /**
   * Главный сервис приложения.
   */
  protected S mainService = null;

  /**
   * Идентификаторы модулей приложения.
   */
  private ModuleItem[] moduleItems;
  
  /**
   * Создает клиентскую фабрику главного модуля приложения.
   * 
   * Пример использования:<pre>
   * 
   * Было:
   * MainClientFactoryImplExt() {
   *   super(
   *     new String[]{
   *       id1,
   *       id2,
   *       id3
   *     },
   *     new String[]{
   *       name1, 
   *       name2, 
   *       name3
   *     },
   *   );
   * }
   * 
   * Стало:
   * MainClientFactoryImplExt() {
   *   super(
   *     new ModuleItem(id1, name1),
   *     new ModuleItem(id2, name2),
   *     new ModuleItem(id3, name3)
   *   );
   * }</pre>
   *
   * @param moduleItems идентификаторы модулей приложения (вместе с наименованиями)
   */
  public MainClientFactoryImpl(ModuleItem...moduleItems) {
    
    logger.debug(this.getClass() + ".MainClientFactoryImpl() moduleIds = " + moduleItems);
    
    if(moduleItems == null) {
      throw new IllegalArgumentException(JepTexts.errors_mainClientFactory_illegalArgument_moduleIds());
    }
    
    
    this.moduleItems = moduleItems;
    
    JepScopeStack.instance.setMainClientFactory((MainClientFactory)this);
    
    initActivityMappers(this);
  }

  /**
   * Получение объекта управления Place'ами приложения.<br/>
   * Если объект еще не создан, то метод создает его и возвращает созданный объект. 
   *
   * @return объект управления Place'ами приложения
   */
  @Override
  public MainPlaceController getPlaceController() {
    if(placeController == null) {
      placeController = new MainPlaceController((MainEventBus)getEventBus(), this);
    }
    return (MainPlaceController) placeController;
  }

  /**
   * Получение главной шины событий приложения.<br/>
   * Если объект еще не создан, то метод создает его и возвращает созданный объект. 
   *
   * @return шина событий приложения
   */
  @Override
  public E getEventBus() {
    if(eventBus == null) {
      eventBus = (E) new MainEventBus(this);
    }
    return eventBus;
  }

  /**
   * Получение Place'а по умолчанию для приложения.
   *
   * @return Place по умолчанию для приложения
   */
  @Override
  public Place getDefaultPlace() {
    return new JepSearchPlace();
  }

  /**
   * Получение главного представления (View) приложения.<br/>
   * Если объект еще не создан, то метод создает его и возвращает созданный объект. 
   *
   * @return главное представление (View) приложения
   */
  @Override
  public IsWidget getMainView() {
    if(mainView == null) {
      mainView = new MainViewImpl();
    }
    return mainView;
  }
  
  /**
   * Получение главного сервиса приложения.<br/>
   * Если объект еще не создан, то метод создает его и возвращает созданный объект. 
   *
   * @return главный сервис приложения
   */
  @Override
  public S getMainService() {
    if(mainService == null) {
      mainService = (S) GWT.create(JepMainService.class);
    }
    return mainService;
  }

  /**
   * Получение идентификаторов модулей приложения.
   *
   * @return идентификаторы модулей приложения
   */
  @Override
  public ModuleItem[] getModuleItems() {
    return moduleItems; 
  }
  
  /**
   * Проверяет наличие определенного идентификатора модуля среди идентификаторов модулей приложения.
   *
   * @param moduleId проверяемый идентификатор модуля
   * @return true - если запрошенный идентификатор модуля найден, false - в противном случае.
   */
  @Override
  public boolean contains(String moduleId) {
    boolean result = false;
    
    int moduleCount = moduleItems.length;
    for(int i = 0; i < moduleCount; i++) {
      if(moduleItems[i].moduleId.equals(moduleId)) {
        result = true;
        break;
      }
    }
    
    return result;
  }

  /**
   * Перемнная для защиты от повторного вызова initActivityMappers в наследниках
   */
  private boolean initActivityMappersInvokedOnce = false;
  
  /**
   * Иннициализация ActivityMapper'ов и ActivityManager'ов.<br/>
   * Необходимо для возможности соответствующих презентеров (Activity в понятиях GWT) прослушивать, подписываться и обрабатывать события,
   * с которыми работает EventBus.
   *
   * @param clientFactory клиентская фабрика главного модуля (приложения)
   */
  protected void initActivityMappers(MainClientFactory<E, S> clientFactory) {
    // Защита от повторного вызова в наследниках
    if (initActivityMappersInvokedOnce) {
      throw new IllegalStateException(getClass().getCanonicalName() + ".initActivityMappers() must be invoked at most once. Do not invoke in descendants");
    }
    initActivityMappersInvokedOnce = true;
    
    /*
     * Создадим ActivityMapper и ActivityManager для главного модуля (приложения).
     */
    ActivityManager mainActivityManager = new MainActivityManager(
      new MainActivityMapper(clientFactory)
      , clientFactory.getEventBus()
    );

    // Необходимо для предотвращения де-регистрации в EventBus и сбором garbage collection (смотри описание метода в JavaDoc GWT).
    mainActivityManager.setDisplay(new AcceptsOneWidget() {
      public void setWidget(IsWidget widget) {}
    });
  }
  
}
