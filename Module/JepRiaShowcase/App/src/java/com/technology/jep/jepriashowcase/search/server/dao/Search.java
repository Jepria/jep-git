package com.technology.jep.jepriashowcase.search.server.dao;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Интерфейс для Search EJB.
 */
public interface Search extends JepDataStandard {

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
