package com.technology.jep.jepria.auto.model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import oracle.jdbc.pool.OracleDataSource;

import com.technology.jep.jepria.auto.model.User;
import com.technology.jep.jepria.server.dao.DaoSupport;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Класс получает из базы пользователя с нужными правами для теста. 
 */
public class UserDao implements UserData {
  
  /**
   * Конструктор. Создает UserDao.
   * 
   * @param dbURL URL from which connections have to be obtained.
   * @param dbUser User name with which connections have to be obtained.
   * @param dbPassword Password name with which connections have to be obtained.
   */
  public UserDao(String dbURL, String dbUser, String dbPassword) {
    
    this.dbURL = dbURL;
    this.dbUser = dbUser;
    this.dbPassword = dbPassword;
  }

  private String dbURL;
  private String dbUser;
  private String dbPassword;
  
  @Override
  public User createUser(String login, List<String> rolesNameList) throws Exception {
    
    Integer operatorId = getTestOperatorId(login, rolesNameList);
    
    if(operatorId == null){
      throw new ApplicationException("Can't create operator. OperatorId is null", null);
    }
    
    return new User(
        (Integer) operatorId,
        login,
        rolesNameList,
        login,
        login);
  }
  
  /**
   * Получает пользователя для теста. <br/>
   * После создания пароль и логин пользователя совпадают.
   * Имя оператора начинается с логина (подробнее см. реализацию DB).
   * 
   * @param login - Логин.
   * @param rolesNameList - Список ролей.
   * @return Id созданного оператора.
   * @throws Exception
   */
  private Integer getTestOperatorId(String login, List<String> rolesNameList) throws Exception {

    OracleDataSource ods = new OracleDataSource();
    ods.setURL(dbURL);
    ods.setUser(dbUser);
    ods.setPassword(dbPassword);
    Connection conn = ods.getConnection();

    Integer result = null;
    try {

      String query = 
          "begin ? := pkg_AccessOperatorTest.getTestOperatorId("
              + "login => ?"
              + ", rolesNameList => cmn_string_table_t(?) "
            + ");"
          + " end;";
      
      CallableStatement callableStatement = conn.prepareCall(query);

      callableStatement.registerOutParameter(1, Types.INTEGER);
      DaoSupport.setInputParamsToStatement(callableStatement, 2, login, 
          UserDao.prepareToCmnStringTableT(rolesNameList));
      
      // Выполнение запроса.
      callableStatement.execute();
  
      //Получим набор.
      result = (Integer) callableStatement.getObject(1);
      if (callableStatement.wasNull()) result = null;
      
    } catch (Throwable th) {
      conn.rollback();
      throw new ApplicationException(th.getMessage(), th);
    } finally {
      try {
        conn.commit();
      } catch (SQLException e) {
        throw new ApplicationException(e.getMessage(), e);
      }
    }
    
    return result;
  }
  
  /**
   * Преобразует список для передачи в качестве параметра в cmn_string_table_t.
   * @param list - Список.
   * @return Строка для работы с помощью cmn_string_table_t.
   */
  //TODO: перенести в системный класс - утилиты для DB
  private static String prepareToCmnStringTableT(List<String> list){
    
    StringBuilder result = new StringBuilder();
    for(String string : list) {
        result.append(string);
        result.append(",");
    }
    return result.length() > 0 ? result.substring(0, result.length() - 1): "";
  }
}
