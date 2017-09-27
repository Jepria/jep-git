package com.technology.jep.jepria.client.ui.main;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.async.LoadAsyncCallback;
import com.technology.jep.jepria.client.history.place.MainPlaceController;
import com.technology.jep.jepria.client.ui.ClientFactory;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * Клиентская фабрика приложения.
 */
public interface MainClientFactory<E extends MainEventBus, S extends JepMainServiceAsync> extends ClientFactory<E> {

  /**
   * Получение объекта управления Place'ами приложения.
   *
   * @return объект управления Place'ами приложения
   */
  MainPlaceController<E, S, MainClientFactory<E, S>> getPlaceController();

  /**
   * Получение Place'а по умолчанию для приложения.
   *
   * @return Place по умолчанию для приложения
   */
  Place getDefaultPlace();

  /**
   * Получение главного представления (View) приложения.
   *
   * @return главное представление (View) приложения
   */
  IsWidget getMainView();
  
  /**
   * Получение главного сервиса приложения.
   *
   * @return главный сервис приложения
   */
  S getMainService();

  /**
   * Создание главного презентера приложения.
   *
   * @return главный презентер приложения
   */
  MainModulePresenter<? extends MainView, E, S, ? extends MainClientFactory<E,S>> createMainModulePresenter();
  
  /**
   * Получение клиентской фабрики заданного модуля приложения.
   *
   * @param moduleId идентификатор модуля приложения
   * @param callback объект асинхронного обратного вызова содержащий клиентскую фабрику модуля приложения
   */
  void getPlainClientFactory(String moduleId, LoadAsyncCallback<PlainClientFactory<PlainEventBus, JepDataServiceAsync>> callback);
  
  /**
   * Получение списка идентификаторов модулей приложения.
   *
   * @return идентификаторы модулей приложения
   */
  List<String> getModuleIds();
}
