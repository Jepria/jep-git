package com.technology.jep.jepriashowcase.custom.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

public interface CustomServiceAsync  extends JepDataServiceAsync {

	/**
	 * Получение имени пользователя по его идентификатору.
	 * 
	 * @param operatorId идентификатор пользователя
	 * @param callback объект обратного вызова содержащий имя пользователя
	 */
	void getOperatorName(
		Integer operatorId
		, AsyncCallback<String> callback);
	
}
