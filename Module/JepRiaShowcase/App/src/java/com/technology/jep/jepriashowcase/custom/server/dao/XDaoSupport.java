package com.technology.jep.jepriashowcase.custom.server.dao;

import java.math.BigDecimal;
import java.sql.*;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.dao.DaoSupport;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

class XDaoSupport {
	protected static Logger logger = Logger.getLogger(XDaoSupport.class.getName());	
	
	public static void execute(
			String query
			, String dataSourceJndiName
			, String resourceBundleName
			, Object... params)
			throws ApplicationException {
		
		logger.trace("execute1(..., " + dataSourceJndiName + ", ...)");

		try {
			if (!XCallContext.isTransaction()) {
				XCallContext.begin(dataSourceJndiName, resourceBundleName);
			}
			XDb db = XCallContext.getDb();
			
			CallableStatement callableStatement = db.prepare(query);
			
			DaoSupport.setInputParamsToStatement(callableStatement, 1, params);

			// Выполнение запроса		
			callableStatement.execute();

		} catch (Throwable th) {
			throw new ApplicationException(th.getMessage(), th);
		} finally {
			// Вызов XCallContext.end() в случае транзакции берёт на себя коннектор
			if (!XCallContext.isTransaction()) {
				XCallContext.end();
			}
		}
	}
	
	public static <T> T execute(
			String query
			, String dataSourceJndiName
			, String resourceBundleName
			, Class<T> resultTypeClass
			, Object... params) 
			throws ApplicationException {
		
		logger.trace("execute2(..., " + dataSourceJndiName + ", ...)");

		T result = null;

		try {
			if (!XCallContext.isTransaction()) {
				XCallContext.begin(dataSourceJndiName, resourceBundleName);
			}
			XDb db = XCallContext.getDb();
			
			CallableStatement callableStatement = db.prepare(query);
			
			DaoSupport.setInputParamsToStatement(
					callableStatement,
					resultTypeClass.isArray() ? 1 : 2,
					params);
			
			setOutputParamsToStatement(
					callableStatement,
					resultTypeClass,
					params);
				
			callableStatement.execute();

			result = getResult(callableStatement, resultTypeClass, params);

		} catch (Throwable th) {
			throw new ApplicationException(th.getMessage(), th);
		} finally {
			// Вызов XCallContext.end() в случае транзакции берёт на себя коннектор
			if (!XCallContext.isTransaction()) {
				XCallContext.end();
			}
		}
		
		return result;
	}

	private static <T> void setOutputParamsToStatement(
			CallableStatement callableStatement,
			Class<T> resultTypeClass,
			Object[] params) throws SQLException, ApplicationException {
		if(resultTypeClass.isArray()) {
			Object[] outputParamTypes = (Object[]) params[0];
			for(int i = 0; i < outputParamTypes.length; i++) {
				registerParameter(callableStatement, i + params.length, (Class<T>) outputParamTypes[i]);
			}
		} else {
			registerParameter(callableStatement, 1, resultTypeClass);
		}
	}

	private static <T> void registerParameter(
			CallableStatement callableStatement,
			int paramNumber,
			Class<T> resultTypeClass) throws SQLException, ApplicationException {
		if (resultTypeClass.equals(Integer.class)) {
			callableStatement.registerOutParameter(paramNumber, Types.INTEGER);
		} else if (resultTypeClass.equals(String.class)) {
			callableStatement.registerOutParameter(paramNumber, Types.VARCHAR);
		} else if (resultTypeClass.equals(Timestamp.class)) {
			callableStatement.registerOutParameter(paramNumber, Types.TIMESTAMP);
		} else if (resultTypeClass.equals(BigDecimal.class)) {
//			callableStatement.registerOutParameter(paramNumber, Types.DOUBLE);
			callableStatement.registerOutParameter(paramNumber, Types.NUMERIC);
		} else {
			throw new ApplicationException("Unknown result type", null);
		}
	}

	private static <T> T getResult(
			CallableStatement callableStatement,
			Class<T> resultTypeClass,
			Object[] params) throws SQLException {
		T result = null;
		
		if(resultTypeClass.isArray()) {
			Object[] outputParamTypes = (Object[]) params[0];
			Object[] results = new Object[outputParamTypes.length];
			for(int i = 0; i < outputParamTypes.length; i++) {
				results[i] = callableStatement.getObject(i + params.length);
				if (callableStatement.wasNull()) results[i] = null;
			}
			result = (T) results;
		} else {
			result = (T)callableStatement.getObject(1);
			if (callableStatement.wasNull())result = null;
		}
		
		return result;
	}
}
