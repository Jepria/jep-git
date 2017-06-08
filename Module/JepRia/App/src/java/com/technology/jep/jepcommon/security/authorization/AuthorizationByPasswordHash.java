package com.technology.jep.jepcommon.security.authorization;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.technology.jep.jepria.server.db.Db;

public class AuthorizationByPasswordHash extends LoginAuthorization {
	  
	private String hash;

	AuthorizationByPasswordHash(Db db, String login, String hash) {
		super(db, login);
		this.hash = hash;
	}
	  
	@Override
	public Integer logon() throws SQLException {
		logger.trace("logon(Db db, " + login + ", " + hash + ")");
		    
	    Integer result = null;
	    String sqlQuery = 
	      " begin" 
	      + "  ? := pkg_Operator.Login("
	        + " operatorLogin => ?"
	        + ", passwordHash => ?" 
	      + ");" 
	      + "  ? := pkg_Operator.GetCurrentUserID;" 
	      + " end;";
		try {
			CallableStatement callableStatement = db.prepare(sqlQuery);
			// Установим Логин.
			callableStatement.setString(2, login);
			// Установим Хэш.
			callableStatement.setString(3, hash);

			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(4, Types.INTEGER);

			callableStatement.execute();

			result = callableStatement.getInt(4);
			if (callableStatement.wasNull())
				result = null;

		} finally {
			db.closeStatement(sqlQuery);
		}

		return result;
	}
}
