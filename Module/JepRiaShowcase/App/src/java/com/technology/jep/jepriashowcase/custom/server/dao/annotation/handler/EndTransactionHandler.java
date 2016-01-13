package com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler;

import java.sql.SQLException;

public interface EndTransactionHandler {
	void handle(boolean successful) throws SQLException;
}
