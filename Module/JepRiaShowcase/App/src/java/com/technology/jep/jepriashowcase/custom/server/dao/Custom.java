package com.technology.jep.jepriashowcase.custom.server.dao;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Интерфейс для Custom EJB.
 */
public interface Custom extends JepDataStandard {

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

	void transaction() throws ApplicationException;
	
}
