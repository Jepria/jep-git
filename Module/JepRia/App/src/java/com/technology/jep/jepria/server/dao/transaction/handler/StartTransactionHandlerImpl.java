package com.technology.jep.jepria.server.dao.transaction.handler;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Стандартная реализация обработчика старта транзакции.<br/>
 */
public class StartTransactionHandlerImpl implements StartTransactionHandler {

	/**
	 * Стандартная реализация обработки начала транзакции.<br/>
	 * Единственное действие &mdash; вызов {@link CallContext#begin(String, String)}.
	 */
	@Override
	public void handle(String dataSourceJndiName, String moduleName) throws ApplicationException {
		CallContext.begin(dataSourceJndiName, moduleName);
	}
}
