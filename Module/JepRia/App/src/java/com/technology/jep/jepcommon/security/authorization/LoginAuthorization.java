package com.technology.jep.jepcommon.security.authorization;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.technology.jep.jepria.server.db.Db;

/**
 * Авторизация по логину.
 */
public class LoginAuthorization extends AuthorizationProvider {

  /**
   * Подключение к БД
   */
  protected Db db;
  
  /**
   * Логин учетной записи
   */
  protected String login;

  LoginAuthorization(Db db, String login) {
    this.db = db;
    this.login = login;
  }
    
  /**
   * {@inheritDoc}
   */
  @Override
  public Integer logon() throws SQLException {
      logger.trace("logon(Db db, " + login + ")");
      
      Integer result = null;
      String sqlQuery = 
        " begin" 
        + "  ? := pkg_Operator.Login(" 
            + "operatorLogin => ?" 
        + "  );" 
        + "  ? := pkg_Operator.GetCurrentUserID;" 
        + " end;";
      try {
        CallableStatement callableStatement = db.prepare(sqlQuery);
        // Установим Логин.
        callableStatement.setString(2, login); 

        callableStatement.registerOutParameter(1, Types.VARCHAR);
        callableStatement.registerOutParameter(3, Types.INTEGER);

        callableStatement.execute();

        result = callableStatement.getInt(3);
        if(callableStatement.wasNull()) result = null;

      } finally {
        db.closeStatement(sqlQuery);
      }

      return result;
  }
}
