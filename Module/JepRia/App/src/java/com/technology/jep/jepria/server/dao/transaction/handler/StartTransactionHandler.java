package com.technology.jep.jepria.server.dao.transaction.handler;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Интерфейс обработчика начала транзакции.
 */
public interface StartTransactionHandler {
	/**
	 * Метод, предваряющий транзакцию.<br/>
	 * Требование к кастомной реализации: необходимо вызвать {@link CallContext#begin(String, String)},
	 * чтобы в <code>ThreadLocal</code> был размещён объект {@link Db}.
	 * @param dataSourceJndiName JNDI-имя источника данных
	 * @param moduleName имя модуля для передачи в DB.
	 * @throws ApplicationException в случае ошибки
	 */
	void handle(String dataSourceJndiName, String moduleName) throws ApplicationException;
}
