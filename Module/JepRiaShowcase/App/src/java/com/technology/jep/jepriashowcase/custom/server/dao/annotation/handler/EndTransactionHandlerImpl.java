package com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler;

import java.sql.SQLException;

import com.technology.jep.jepriashowcase.custom.server.dao.XCallContext;

public class EndTransactionHandlerImpl implements EndTransactionHandler {

	@Override
	public void handle(boolean successful) {
		try {
			if (successful){
				XCallContext.commit();
			}
			else {
				XCallContext.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			XCallContext.end();
		}
	}

}
