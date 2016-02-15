package com.technology.jep.jepria.server.dao.transaction.annotation;

import com.technology.jep.jepria.server.dao.transaction.handler.EndTransactionHandler;

public @interface After {
	Class<? extends EndTransactionHandler> endTransactionHandler();
}
