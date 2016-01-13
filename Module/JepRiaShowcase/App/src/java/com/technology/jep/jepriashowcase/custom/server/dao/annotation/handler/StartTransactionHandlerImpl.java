package com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepriashowcase.custom.server.dao.XCallContext;

public class StartTransactionHandlerImpl implements StartTransactionHandler {

	@Override
	public void handle(String dataSourceJndiName, String resourceBundleName) throws ApplicationException {
		XCallContext.begin(dataSourceJndiName, resourceBundleName, true);
	}
}
