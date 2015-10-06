package com.technology.jep.jepriashowcase.custom.shared.service;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.service.data.JepDataService;

/**
 * Интерфейс gwt-сервиса.
 */
@RemoteServiceRelativePath("CustomService")
public interface CustomService  extends JepDataService {

	/**
	 * Получение имени пользователя по его идентификатору.
	 * 
	 * @param operatorId идентификатор пользователя
	 * @return имя пользователя
	 * @throws ApplicationException
	 */
	String getOperatorName(
		Integer operatorId)
		throws ApplicationException;

}
