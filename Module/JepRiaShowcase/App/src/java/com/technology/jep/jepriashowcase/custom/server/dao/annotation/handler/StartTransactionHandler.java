package com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;

public interface StartTransactionHandler {
	void handle(String dataSourceJndiName, String resourceBundleName) throws ApplicationException;
}
