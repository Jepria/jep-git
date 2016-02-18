package com.technology.jep.jepria.server.dao.transaction.handler;

import java.sql.SQLException;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

public class EndTransactionHandlerImpl implements EndTransactionHandler {
	
	@Override
	public void handle(Throwable caught) throws ApplicationException {
		try {
			if (caught == null) {
				CallContext.commit();
			}
			else {
				CallContext.rollback();
			}
		} catch (SQLException e) {
			// Необходимо сигнализировать о последнем выброшенном исключении.
			caught = e;
		}
		finally {
			CallContext.end();
		}
		if (caught != null) {
			throw new ApplicationException(caught.getMessage(), caught);
		}
	}

}
