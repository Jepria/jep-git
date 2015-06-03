package com.technology.jep.jepria.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.dto.JepDto;

/**
 * Общий предок интерфейсов сервисов главного модуля.
 */
@RemoteServiceRelativePath("MainService")
public interface JepMainService extends RemoteService {

	/**
	 * Получение данных о текущем (вошедшем через SSO) пользователе.
	 * 
	 * @return данныe о текущем (вошедшем через SSO) пользователе
	 */
	JepDto getUserData();
	
	/**
	 * Выход (logout) текущего пользователя из SSO.
	 */
	void logout();
}
