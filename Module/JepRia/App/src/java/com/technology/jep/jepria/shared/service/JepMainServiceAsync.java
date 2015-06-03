package com.technology.jep.jepria.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.dto.JepDto;

/**
 * Общий предок интерфейсов асинхронных сервисов главного модуля.
 */
public interface JepMainServiceAsync {
	/**
	 * Получение данных о текущем (вошедшем через SSO) пользователе.
	 * 
	 * @param callback данныe о текущем (вошедшем через SSO) пользователе
	 */
	void getUserData(AsyncCallback<JepDto> callback);
	
	/**
	 * Выход (logout) текущего пользователя из SSO.
	 * 
	 * @param callback пустой обратный вызов (для сигнализации, что асинхронный метод отработал)
	 */
	void logout(AsyncCallback<Void> callback);
	
}
