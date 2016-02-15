package com.technology.jep.jepria.server.dao.transaction.handler;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;

public interface StartTransactionHandler {
	void handle(String dataSourceJndiName) throws ApplicationException;
}
