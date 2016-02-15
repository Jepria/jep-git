package com.technology.jep.jepria.server.dao.transaction.handler;

public interface EndTransactionHandler {
	void handle(Throwable caught) throws Exception;
}
