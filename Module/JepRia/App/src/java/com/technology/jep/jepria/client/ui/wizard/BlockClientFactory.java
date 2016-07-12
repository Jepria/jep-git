package com.technology.jep.jepria.client.ui.wizard;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.history.place.BlockPlaceController;
import com.technology.jep.jepria.client.ui.ClientFactory;
import com.technology.jep.jepria.client.ui.eventbus.JepEventBus;

@SuppressWarnings("rawtypes")
public interface BlockClientFactory<S> extends ClientFactory<JepEventBus>{

  /**
   * Получение представления (View) блока визарда.
   *
   * @return представление (View) блока визарда
   */
  BlockView getView();
  
  /**
   * Создание презентера блока визарда.
   * 
   * @param place новый плейс
   *
   * @return презентер блока визарда
   */
  BlockPresenter createPresenter(Place place);
  
  /**
   * Получение сервиса работы с данными.
   *
   * @return сервис работы с данными
   */
  S getService();
  
  /**
   * Получение объекта управления Place'ами блока визарда.
   *
   * @return объект управления Place'ами блока визарда
   */
  BlockPlaceController getPlaceController();
}
