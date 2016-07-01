package com.technology.jep.jepriashowcase.arsenic.server.dao;
 
import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
 
public interface Arsenic extends JepDataStandard {
	/**
	 * Метод, имитирующий длительную загрузку данных на сервере. Фактически, просто спит указанное количество времени.
	 * @param msec требуемая задержка в миллисекундах.
	 * @throws ApplicationException
	 */
	void durableFetch(long msec) throws ApplicationException;
}
