package com.technology.jep.jepria.auto.model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

import com.technology.jep.jepria.auto.model.User;
import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.dao.DaoSupport;
import com.technology.jep.jepria.server.dao.ResultSetMapper;
import com.technology.jep.jepria.shared.dto.JepDto;
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
  
  public static final String USER_LOGIN = "login";
  public static final String USER_PASSWORD = "password";
  public static final String USER_OPERATOR_ID = "operator_id";
  public static final String USER_OPERATOR_NAME = "operator_name";
  
  /**
   * Получает пользователя для теста. 
   * @param roleShortNameList Список ролей через запятую.
   * @return Пользователь. {@link com.technology.jep.jepria.auto.model.User}
   */
  @Override
  public User createUser(String roleShortNameList) throws Exception {
    
    List<JepDto> list = getOperator(roleShortNameList);
    if(list.size() == 0){
      throw new ApplicationException("Create operator don't return any data.", null);
    }else if(list.size() > 1){
      throw new ApplicationException("Create operator returns more than one record in cursor.", null);
    }
    
    JepDto userData = list.get(0);
    return new User(
        (Integer) userData.get(USER_OPERATOR_ID),
        (String) userData.get(USER_OPERATOR_NAME),
        roleShortNameList,
        (String) userData.get(USER_LOGIN),
        (String) userData.get(USER_PASSWORD));
    
  }
  
  /**
   * Получает пользователя для теста. <br/>
   * Используется функция pkg_jepriashowcasetest.getOperator. 
   * 
   * TODO: функция возвращает курсор, хотя нам нужна всего одна строчка. 
   * Из-за этого приходится фиктивно пробегать по курсору, а затем делать проверки, что пришла ровно одна строчка. 
   * Передалать на продецуду с OUT параметрами.
   * 
   * @param roleShortNameList Список ролей через запятую.
   * @return Список JepDto, в котором хранятся данные пользователя.
   * @throws Exception
   */
  protected List<JepDto> getOperator(String roleShortNameList) throws Exception {

    OracleDataSource ods = new OracleDataSource();
    ods.setURL(dbURL);
    ods.setUser(dbUser);
    ods.setPassword(dbPassword);
    Connection conn = ods.getConnection();

    List<JepDto> result = new ArrayList<JepDto>();
    ResultSet resultSet = null;
    Throwable caught = null;
    
    try {

      String query = 
          "begin ? := pkg_jepriashowcasetest.getOperator("
            + " roleShortNameList => ?"
            + ");"
          + " end;";
      CallableStatement callableStatement = conn.prepareCall(query);

      callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
      DaoSupport.setInputParamsToStatement(callableStatement, 2, roleShortNameList);
      
      // Выполнение запроса.
      callableStatement.execute();
  
      //Получим набор.
      resultSet = (ResultSet) callableStatement.getObject(1);
      
      ResultSetMapper<JepDto> mapper = new ResultSetMapper<JepDto>() {
        public void map(ResultSet rs, JepDto dto) throws SQLException {
          dto.set(USER_OPERATOR_ID, getInteger(rs, USER_OPERATOR_ID));
          dto.set(USER_OPERATOR_NAME, rs.getString(USER_OPERATOR_NAME));
          dto.set(USER_LOGIN, rs.getString(USER_LOGIN));
          dto.set(USER_PASSWORD, rs.getString(USER_PASSWORD));
        }
      };
      
      while (resultSet.next()) {
        JepDto resultModel = new JepDto();
        
        mapper.map(resultSet, resultModel);
        
        result.add(resultModel);
      }
      
    } catch (Throwable th) {
      conn.rollback();
      throw new ApplicationException(th.getMessage(), th);
    } finally {
      try {
        if (resultSet != null) {
          resultSet.close();
        }
        
        conn.commit();
      } catch (SQLException e) {
        throw new ApplicationException(e.getMessage(), e);
      }
    }
    
    return result;
  }
}
