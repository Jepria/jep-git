package com.technology.jep.jepcommon.security;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.technology.jep.jepcommon.security.authorization.AuthorizationHelper;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Обеспечивает доступ к некоторым методам пакета pkg_Operator в базе данных.
 */
public class pkg_Operator {
  private static Logger logger = Logger.getLogger(pkg_Operator.class.getName());  
  
  /**
   * Регистрация оператора в базе данных.<br/>
   * Функция осуществляет последовательный вызов функций базы данных:<br/>
   * <code>
   * pkg_Operator.Login(login, password);<br/>
   * pkg_Operator.GetCurrentUserID();<br/>
   * </code>
   * 
   * @param db        соединение с базой данных
   * @param login     логин пользователя
   * @param password  пароль пользователя
   * @return идентификатор текущего пользователя при успешной аутентификации
   * @throws SQLException при неудачной аутентификации
   */
  public static final Integer logon(Db db, String login, String password, String hash) throws SQLException {
    return AuthorizationHelper.getInstance(db, login, password, hash).logon();
  }

  /**
   * Регистрация оператора в базе данных.<br/>
   * Функция осуществляет последовательный вызов функций базы данных:<br/>
   * <code>
   * pkg_Operator.Login(login);<br/>
   * pkg_Operator.GetCurrentUserID();<br/>
   * </code>
   * 
   * @param db        соединение с базой данных
   * @param login     логин пользователя
   * @return идентификатор текущего пользователя
   * @throws SQLException при отсутствии пользователя с указанным логином
   */
  public static final Integer logon(Db db, String login) throws SQLException {
	  return AuthorizationHelper.getInstance(db, login, null, null).logon();
  }

  /**
   * Функция определяет необходимость смены пользователем пароля.
   * 
   * @param operatorId  идентификатор пользователя
   * @return true - если пользователю необходимо сменить пароль, false - в противном случае.
   * @throws SQLException
   */
  public static final boolean isChangePassword(Db db, Integer operatorId) throws SQLException {
    logger.trace("isChangePassword(Db db, " + operatorId + ")");
    
    
    Integer result = null;
    String sqlQuery = 
      " begin" 
      + "  ? := pkg_Operator.IsChangePassword(" 
        + " operatorId => ?" 
      + ");" 
      + " end;";
    try {
      CallableStatement callableStatement = db.prepare(sqlQuery);

      if(operatorId != null) callableStatement.setInt(2, operatorId);  
      else callableStatement.setNull(2, Types.INTEGER); 

      callableStatement.registerOutParameter(1, Types.INTEGER);

      callableStatement.execute();

      result = new Integer(callableStatement.getInt(1));
      if(callableStatement.wasNull())result = null;

    } finally {
      db.closeStatement(sqlQuery);
    }

    return result != null && result.intValue() == 1;
  }

  /**
   * Изменение пароля пользователя.
   * 
   * @param db                  соединение с базой данных
   * @param operatorId          идентификатор пользователя, пароль которого необходимо изменить
   * @param password            пароль пользователя
   * @param newPassword         новый пароль пользователя
   * @param newPasswordConfirm  подтверждение нового пароля пользователя
   * @throws SQLException при неудавшейся смене пароля
   */
  public static final void changePassword(
    Db db
    , Integer operatorId
    , String password
    , String newPassword
    , String newPasswordConfirm) 
    throws SQLException {
    logger.trace("changePassword()");
    
    String sqlQuery = 
      " begin" 
      + "  pkg_Operator.ChangePassword(" 
        + " operatorId => ?" 
        + ", password => ?" 
        + ", newPassword => ?" 
        + ", newPasswordConfirm => ?" 
      + ");" 
      + " end;";
    try {
      CallableStatement callableStatement = db.prepare(sqlQuery);
      
      if(operatorId != null) callableStatement.setInt(1, operatorId.intValue());
      else callableStatement.setNull(1, Types.INTEGER);

      if(password != null) callableStatement.setString(2, password);
      else callableStatement.setNull(2, Types.VARCHAR);

      if(newPassword != null) callableStatement.setString(3, newPassword);
      else callableStatement.setNull(3, Types.VARCHAR);

      if(newPasswordConfirm != null) callableStatement.setString(4, newPasswordConfirm);
      else callableStatement.setNull(4, Types.VARCHAR);

      callableStatement.execute();

    } finally {
      db.closeStatement(sqlQuery);
    }
  }

  /**
   * Получение списка ролей пользователя.
   * 
   * @param db соединение с базой данных
   * @param login логин пользователя
   * @return список ролей пользователя в виде java.util.List&lt;String&gt;
   * @throws SQLException
   */
  public static final List<String> getRoles(Db db, String login) throws SQLException {
    logger.trace("getRoles(Db db, " + login + ")");
    
    List<String> result = new ArrayList<String>();

    String sqlQuery = 
      " begin"
      + " ? := pkg_Operator.getRolesShortName("
        + " login => ?"
      + " );"
      + " end;";
    try {
      
      CallableStatement callableStatement = db.prepare(sqlQuery);

      callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

      // Установим Логин.
      if(login != null) callableStatement.setString(2, login);  
      else callableStatement.setNull(2, Types.VARCHAR); 

      // Выполнение запроса.
      callableStatement.execute();

      //Получим набор.
      ResultSet resultSet = (ResultSet) callableStatement.getObject(1);     

      while (resultSet.next()) {
        result.add(resultSet.getString("short_name"));
      }
    } finally {
      db.closeStatement(sqlQuery);
    }

    return result;
  }


  /**
   * Получение списка ролей пользователя.
   * 
   * @param db соединение с базой данных
   * @param operatorId  идентификатор пользователя
   * @return список ролей пользователя в виде java.util.List&lt;String&gt;
   * @throws SQLException
   */
  public static final List<String> getRoles(Db db, Integer operatorId) throws SQLException {
    logger.trace("getRoles(Db db, " + operatorId + ")");
    
    List<String> result = new ArrayList<String>();

    String sqlQuery = 
      " begin"
      + " ? := pkg_Operator.getRolesShortName("
        + " operatorID => ?"
      + " );"
      + " end;";
    try {
      CallableStatement callableStatement = db.prepare(sqlQuery);
      callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
  
      // Установим Логин.
      if(operatorId != null) callableStatement.setInt(2, operatorId);
      else callableStatement.setNull(2, Types.VARCHAR); 
  
      // Выполнение запроса.
      callableStatement.execute();
  
      //Получим набор.
      ResultSet resultSet = (ResultSet) callableStatement.getObject(1);     
  
      while (resultSet.next()) {
        result.add(resultSet.getString("short_name"));
      }
    } finally {
      db.closeStatement(sqlQuery);
    }
  
    return result;
  }
  
}