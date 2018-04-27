package com.technology.jep.jepcommon.security.authorization;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.technology.jep.jepria.server.db.Db;

/**
 * Авторизация по хэш-паролю.
 */
public class AuthorizationByPasswordHash extends LoginAuthorization {
	  
	/**
	 * Хэш пароля
	 */
	private String hash;

	AuthorizationByPasswordHash(Db db, String login, String hash) {
		super(db, login);
		this.hash = hash;
	}
	 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer logon() throws SQLException {
		logger.trace("logon(Db db, " + login + ", " + hash + ")");
		    
	    Integer result = null;
	    String sqlQuery = 
	      " begin" 
	      + "  ? := pkg_Operator.Login("
	        + " operatorLogin => ?"
	        + ", password => ?" 
	        + ", passwordHash => ?" 
	      + ");" 
	      + "  ? := pkg_Operator.GetCurrentUserID;" 
	      + " end;";
		try {
			CallableStatement callableStatement = db.prepare(sqlQuery);
			// Установим Логин.
			callableStatement.setString(2, login);
			// Установим Пароль.
			callableStatement.setNull(3, Types.VARCHAR);
			// Установим Хэш.
			callableStatement.setString(4, hash);

			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(5, Types.INTEGER);

			callableStatement.execute();

			result = callableStatement.getInt(5);
			if (callableStatement.wasNull())
				result = null;

		} finally {
			db.closeStatement(sqlQuery);
		}

		return result;
	}
}
