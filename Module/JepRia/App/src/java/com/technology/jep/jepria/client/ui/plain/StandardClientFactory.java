package com.technology.jep.jepria.client.ui.plain;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * Интерфейс клиентской фабрики стандартного (списочная и детальная формы) модуля.
 */
public interface StandardClientFactory<E extends PlainEventBus, S extends JepDataServiceAsync> extends PlainClientFactory<E, S> {

  /**
   * Получение представления (View) детальной формы.
   *
   * @return представление (View) детальной формы
   */
  IsWidget getDetailFormView();
  
  /**
   * Получение представления (View) списочной формы.
   *
   * @return представление (View) списочной формы
   */
  IsWidget getListFormView();
  
  /**
   * Получение представления (View) инструментальной панели.
   *
   * @return представление (View) инструментальной панели
   */
  IsWidget getToolBarView();
  
  /**
   * Получение представления (View) панели состояния.
   *
   * @return представление (View) панели состояния
   */
  IsWidget getStatusBarView();
  
  /**
   * Создание презентера детальной формы.
   *
   * @return презентер детальной формы
   */
  JepPresenter<E, ? extends StandardClientFactory<E, S>> createDetailFormPresenter(Place place);
  
  /**
   * Создание презентера списочной формы.
   *
   * @return презентер списочной формы
   */
  JepPresenter<E, ? extends StandardClientFactory<E, S>> createListFormPresenter(Place place);
  
  /**
   * Создание презентера инструментальной панели.
   *
   * @return презентер инструментальной панели
   */
  JepPresenter<E, ? extends StandardClientFactory<E, S>> createToolBarPresenter(Place place);
  
  /**
   * Создание презентера панели состояния.
   *
   * @return презентер панели состояния
   */
  JepPresenter<E, ? extends StandardClientFactory<E, S>> createStatusBarPresenter(Place place);
  
}
