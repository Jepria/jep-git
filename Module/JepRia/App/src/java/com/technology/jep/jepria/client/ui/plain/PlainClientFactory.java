package com.technology.jep.jepria.client.ui.plain;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.history.place.PlainPlaceController;
import com.technology.jep.jepria.client.ui.ClientFactory;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactoryImpl;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * Интерфейс клиентской фабрики простого модуля.
 */
public interface PlainClientFactory<E extends PlainEventBus, S extends JepDataServiceAsync> extends ClientFactory<E> {

	/**
	 * Получение клиентской фабрики главного модуля.
	 *
	 * @return клиентская фабрика главного модуля
	 */
	MainClientFactoryImpl<MainEventBus, JepMainServiceAsync> getMainClientFactory();
	
	/**
	 * Получение объекта управления Place'ами модуля.
	 *
	 * @return объект управления Place'ами модуля
	 */
	PlainPlaceController getPlaceController();
	
	/**
	 * Получение представления (View) модуля.
	 *
	 * @return представление (View) модуля
	 */
	IsWidget getModuleView();
	
	/**
	 * Создание презентера модуля.
	 *
	 * @return презентер модуля
	 */
	JepPresenter createPlainModulePresenter(Place place);
	
	/**
	 * Получение сервиса работы с данными.
	 *
	 * @return сервис работы с данными
	 */
	S getService();
	
	/**
	 * Получение определения данных модуля.
	 *
	 * @return определение данных модуля
	 */
	JepRecordDefinition getRecordDefinition();
}
