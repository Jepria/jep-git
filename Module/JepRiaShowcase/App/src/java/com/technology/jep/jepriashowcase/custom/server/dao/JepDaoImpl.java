package com.technology.jep.jepriashowcase.custom.server.dao;

import java.sql.SQLException;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;

public class JepDaoImpl {

	protected String dataSourceJndiName;
	protected String resourceBundleName;
	
	public JepDaoImpl(String dataSourceJndiName, String resourceBundleName) {
		this.dataSourceJndiName = dataSourceJndiName;
		this.resourceBundleName = resourceBundleName;
	}
	
	public void update(String sqlQuery, Object... params) throws ApplicationException {
		XDaoSupport.execute(
				sqlQuery,
				dataSourceJndiName,
				resourceBundleName,
				params);
	}

	public <T> T execute(
			String sqlQuery,
			Class<T> resultTypeClass,
			Object... params) throws ApplicationException {
		return XDaoSupport.execute(
				sqlQuery,
				dataSourceJndiName,
				resourceBundleName,
				resultTypeClass,
				params);
	}

	protected final void startTransaction() throws ApplicationException {
		XCallContext.begin(dataSourceJndiName, resourceBundleName, true);
	}

	protected final void commit() throws SQLException {
		XCallContext.commit();
	}

	protected final void rollback() {
		XCallContext.rollback();
	}

	protected final void endTransaction() {
		XCallContext.end();
	}

}
