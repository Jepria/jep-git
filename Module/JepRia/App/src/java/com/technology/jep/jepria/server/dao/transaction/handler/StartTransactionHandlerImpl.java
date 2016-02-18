package com.technology.jep.jepria.server.dao.transaction.handler;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

public class StartTransactionHandlerImpl implements StartTransactionHandler {

	@Override
	public void handle(String dataSourceJndiName) throws ApplicationException {
		CallContext.begin(dataSourceJndiName);
	}
}
