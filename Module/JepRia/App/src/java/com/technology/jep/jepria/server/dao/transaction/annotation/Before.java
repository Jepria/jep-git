package com.technology.jep.jepria.server.dao.transaction.annotation;

import com.technology.jep.jepria.server.dao.transaction.handler.StartTransactionHandler;

public @interface Before {
	Class<? extends StartTransactionHandler> startTransactionHandler();
}
