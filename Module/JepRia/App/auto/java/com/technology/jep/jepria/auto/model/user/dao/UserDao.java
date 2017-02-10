package com.technology.jep.jepria.auto.model.user.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import oracle.jdbc.pool.OracleDataSource;

import com.technology.jep.jepria.auto.model.user.User;
import com.technology.jep.jepria.server.dao.DaoSupport;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Класс получает из базы пользователя с нужными правами для теста. 
 */
public class UserDao implements UserData {
  
  /**
   * Конструктор. Создает UserDao.
   * 
   * @param dbUrl - URL, по которому подключаемся к DB.
   * @param dbUser - Пользователь, под которым подключаемся к DB.
   * @param dbPassword - Пароль, под которым подключаемся к DB.
   */
  public UserDao(String dbUrl, String dbUser, String dbPassword) {
    
    this.dbUrl = dbUrl;
    this.dbUser = dbUser;
    this.dbPassword = dbPassword;
  }

  private String dbUrl;
  private String dbUser;
  private String dbPassword;
  
  @Override
  public User createUser(String login, List<String> roleSNameList) throws Exception {
    
    Integer operatorId = getTestOperatorId(login, roleSNameList);
    
    if(operatorId == null){
      throw new ApplicationException("Can't create operator. OperatorId is null", null);
    }
    
    return User.fromDB(
        (Integer) operatorId,
        login,
        roleSNameList,
        login,
        login);
  }
  
  /**
   * Получает пользователя для теста. <br/>
   * После создания пароль и логин пользователя совпадают.
   * Имя оператора начинается с логина (подробнее см. реализацию DB).
   * 
   * @param login - Логин.
   * @param roleSNameList - Список ролей.
   * @return Id созданного оператора.
   * @throws Exception
   */
  private Integer getTestOperatorId(String login, List<String> roleSNameList) throws Exception {

    OracleDataSource ods = new OracleDataSource();
    ods.setURL(dbUrl);
    ods.setUser(dbUser);
    ods.setPassword(dbPassword);
    Connection conn = ods.getConnection();

    Integer result = null;
    try {

      StringBuilder roleSNameListSubQuery = new StringBuilder();
      int roleSNameCount = (roleSNameList == null) ? 0 :roleSNameList.size(); 
      if(roleSNameCount > 0) {
        
        //Генерируем вопросы для параметризованного вызова cmn_string_table_t
        //Количество вопросов равно количеству ролей.
        StringBuilder subQuery = new StringBuilder();
        for(int i = 0; i < roleSNameList.size(); i++) {
          subQuery.append("?,");
        }
        
        if(subQuery.length() > 0) {
          roleSNameListSubQuery.append(", roleSNameList => cmn_string_table_t( "); 
          roleSNameListSubQuery.append(subQuery.substring(0, subQuery.length() - 1)); //удаляет последнюю запятую.
          roleSNameListSubQuery.append(" ) ");
        }
      }
          
      String query = 
          "begin ? := pkg_AccessOperatorTest.getTestOperatorId("
              + "login => ?"
              + roleSNameListSubQuery.toString()
            + ");"
          + " end;";
      
      CallableStatement callableStatement = conn.prepareCall(query);

      //Устанавливаем тип выходного параметра
      callableStatement.registerOutParameter(1, Types.INTEGER);
      
      //Устанавливаем логин
      callableStatement.setString(2, login);
      
      if(roleSNameCount > 0) {
        //Устанавливаем роли
        DaoSupport.setInputParamsToStatement(callableStatement, 3, 
            roleSNameList.toArray(new Object[roleSNameCount]));
      }
      
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
}
