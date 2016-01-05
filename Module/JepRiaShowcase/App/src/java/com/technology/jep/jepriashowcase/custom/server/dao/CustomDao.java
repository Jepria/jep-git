package com.technology.jep.jepriashowcase.custom.server.dao;

import com.technology.jep.jepria.server.ejb.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Интерфейс для Custom.
 */
public interface CustomDao extends JepDataStandard {

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
