package com.technology.jep.jepria.client.ui.main;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
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
	private String[] moduleIds;
	
	/**
	 * Наименования модулей приложения.
	 */
	private String[] moduleItemTitles;
	
	/**
	 * Создает клиентскую фабрику главного модуля приложения.
	 *
	 * @param moduleIds идентификаторы модулей приложения
	 * @param moduleItemTitles наименования модулей приложения
	 */
	public MainClientFactoryImpl(
		String[] moduleIds,
		String[] moduleItemTitles) {
		
		logger.debug(this.getClass() + ".MainClientFactoryImpl() moduleIds = " + moduleIds);
		
		if(moduleIds == null) {
			throw new IllegalArgumentException(JepTexts.errors_mainClientFactory_illegalArgument_moduleIds());
		}
		
		if(moduleItemTitles == null || moduleItemTitles.length != moduleIds.length) {
			throw new IllegalArgumentException(JepTexts.errors_mainClientFactory_illegalArgument_moduleItemTitles());
		}
		
		this.moduleIds = moduleIds;
		this.moduleItemTitles = moduleItemTitles;
		
		JepScopeStack.instance.setMainClientFactory((MainClientFactory)this);
	}

	/**
	 * Получение объекта управления Place'ами приложения.<br/>
	 * Если объект еще не создан, то метод создает его и возвращает созданный объект. 
	 *
	 * @return объект управления Place'ами приложения
	 */
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
	public E getEventBus() {
		if(eventBus == null) {
			eventBus = new MainEventBus(this);
		}
		return (E) eventBus;
	}

	/**
	 * Получение Place'а по умолчанию для приложения.
	 *
	 * @return Place по умолчанию для приложения
	 */
	public Place getDefaultPlace() {
		return new JepSearchPlace();
	}

	/**
	 * Получение главного представления (View) приложения.<br/>
	 * Если объект еще не создан, то метод создает его и возвращает созданный объект. 
	 *
	 * @return главное представление (View) приложения
	 */
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
	public S getMainService() {
		if(mainService == null) {
			mainService = (S)GWT.create(JepMainService.class);
		}
		return mainService;
	}

	/**
	 * Установка идентификаторов модулей приложения.
	 *
	 * @param moduleIds идентификаторы модулей приложения
	 */
	public void setModuleIds(String[] moduleIds) {
		this.moduleIds = moduleIds; 
	}

	/**
	 * Получение идентификаторов модулей приложения.
	 *
	 * @return идентификаторы модулей приложения
	 */
	public String[] getModuleIds() {
		return moduleIds; 
	}
	
	/**
	 * Проверяет наличие определенного идентификатора модуля среди идентификаторов модулей приложения.
	 *
	 * @param moduleId проверяемый идентификатор модуля
	 * @return true - если запрошенный идентификатор модуля найден, false - в противном случае.
	 */
	public boolean contains(String moduleId) {
		boolean result = false;
		
		int moduleCount = moduleIds.length;
		for(int i = 0; i < moduleCount; i++) {
			if(moduleIds[i].equals(moduleId)) {
				result = true;
				break;
			}
		}
		
		return result;
	}

	/**
	 * Получение наименований модулей приложения.
	 *
	 * @return наименования модулей приложения
	 */
	public String[] getModuleItemTitles() {
		return moduleItemTitles; 
	}
	
	/**
	 * Иннициализация ActivityMapper'ов и ActivityManager'ов.<br/>
	 * Необходимо для возможности соответствующих презентеров (Activity в понятиях GWT) прослушивать, подписываться и обрабатывать события,
	 * с которыми работает EventBus.
	 *
	 * @param clientFactory клиентская фабрика главного модуля (приложения)
	 */
	protected void initActivityMappers(MainClientFactory<E, S> clientFactory) {
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
