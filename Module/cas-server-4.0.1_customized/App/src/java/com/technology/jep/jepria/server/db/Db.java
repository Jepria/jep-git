package com.technology.jep.jepria.server.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Переработан (из аналога в JepRia) для использования в JepLoginModule
 * Обёртка для доступа к БД
 * Реализуются сервисы для получения и аккуратного закрытия JDBC ресурсов.
 */
public class Db {
    static private final Logger logger = LoggerFactory.getLogger(Db.class);
  
  /**
   * JNDI-имя источника данных модуля.
   */
  private static final String DATA_SOURCE_JNDI_NAME = "jdbc/RFInfoDS";
  
  // Группа параметров, задающая соединение через URL
  public static final String DATA_SOURCE_NAME_CONNECTION_PARAMETER = "dataSource";
  
  // Группа параметров, задающая соединение через URL
  public static final String JDBC_URL_CONNECTION_PARAMETER = "connectionUrl";
  public static final String DB_USERNAME_CONNECTION_PARAMETER = "dbUser";
  public static final String DB_PASSWORD_CONNECTION_PARAMETER = "dbPassword";

  private static Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
  private static String dataSourceJndiName = null;

  private Connection connection;
  private Map<String, String> connectionOptions = null;
  
  // Здесь по ключу - текст sql лежит открытый курсор.
  private ConcurrentHashMap<String, CallableStatement> statementsMap = new ConcurrentHashMap<String, CallableStatement>();

  static private int connectionCount = 0; // TODO убрать после отладки

  public Db(Map<String, String> connectionOptions) {
    if(connectionOptions != null && connectionOptions.size() > 0) {
      this.connectionOptions = connectionOptions;
    } else {
      dataSourceJndiName = DATA_SOURCE_JNDI_NAME;
    }
  }

  public Db(String dataSourceJndiName) {
    Db.dataSourceJndiName = dataSourceJndiName;
  }
  
  /**
   * Подготавливает Statement
   */
  public CallableStatement prepare(String sql) throws SQLException {
    CallableStatement cs = (CallableStatement) statementsMap.get(sql);
    if (cs == null) {
      try {
        cs = getConnection().prepareCall(sql);
      } catch(Throwable th) {
        throw new SystemException("PrepareCall Error for query '" + sql + "'", th);
      }

      statementsMap.put(sql, cs);
    }
    return cs;
  }
  
  private Connection getConnection() {
    if(isConnectByDefaultDataSource()) {
      if(this.isClosed()) {
        this.connect();
      }
    } else {
      try {
        if(connection == null || connection.isClosed()) {
          connection = createConnection(connectionOptions);
        }
      } catch (SQLException ex) {
        throw new SystemException("Check Db connection error", ex);
      }
    }
    
    return connection;
  }

  /**
   * Метод закрывает все ресурсы связанные с данным Db: connection и все курсоры из statementsMap.
   * Метод должен вызываться в конце сессии пользователя
   */
  public void closeAll() {
    trace("closeAll()");
    
    Iterator<String> it = statementsMap.keySet().iterator();
    while (it.hasNext()) {
      CallableStatement cs = (CallableStatement) statementsMap.get(it.next());
      try {
        cs.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    statementsMap.clear();
//    statementsMap = null;
    try {
      if(connection != null) {
        connection.close();
        connectionCount--;
        trace("closeAll(): connectionCount = " + connectionCount);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    connection = null;
  }

  public boolean closeStatement(String sql) {
    trace("closeStatement()");
    
    Statement st = (Statement) statementsMap.get(sql);
    boolean existsFlag = st != null;
    try {
      if(existsFlag) {
        st.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    statementsMap.remove(sql);
    return existsFlag;
  }

  public void rollback() {
    trace("rollback()");
    try {
      connection.rollback();
    } catch (SQLException ex) {
    }
  }

  /**
   * Используется при работе по DataSourceJndiName
   */
  public void connect() {
    this.connection = createConnection(dataSourceJndiName);
  }

  private synchronized static Connection createConnection(Map<String, String> connectionParameters) {
    trace(Db.class + ".createConnection() BEGIN");
    
    Connection connection = null;
    String dataSourceNameParameter = connectionParameters.get(DATA_SOURCE_NAME_CONNECTION_PARAMETER);
    if(dataSourceNameParameter != null) {
      trace(Db.class + ".createConnection() dataSourceNameParameter = " + dataSourceNameParameter);
      connection = createConnection(dataSourceNameParameter);
    } else {
      String jdbcUrl = connectionParameters.get(JDBC_URL_CONNECTION_PARAMETER);
      trace(Db.class + ".createConnection() jdbcUrl = " + jdbcUrl);
      if(jdbcUrl != null) {
        trace("BEGIN createConnection(" + jdbcUrl + ")");
        
        try {
          Class.forName ("oracle.jdbc.OracleDriver");
          connection = DriverManager.getConnection(
              jdbcUrl,
              connectionParameters.get(DB_USERNAME_CONNECTION_PARAMETER),
              connectionParameters.get(DB_PASSWORD_CONNECTION_PARAMETER));
        } catch (SQLException ex) {
          handleException(ex, jdbcUrl);
        } catch (ClassNotFoundException ex) {
          handleException(ex, jdbcUrl);
        } finally {
          trace("END createConnection(" + jdbcUrl + ")");
        }
      } else if(isConnectByDefaultDataSource()) {
        connection = createConnection(dataSourceJndiName);
      }
    }
    
    trace(Db.class + ".createConnection() END");
    return connection;
  }

  private static void handleException(Exception ex, String jdbcUrl) {
    String errorMessage = "Connection creation error for '" + jdbcUrl + "'";
    logger.error(Db.class + ".createConnection(): " + errorMessage, ex);
    throw new SystemException(errorMessage, ex);
  }

  /**
   * Используется при работе по DataSourceJndiName
   */
  private synchronized static Connection createConnection(String dataSourceJndiName) {
    trace("BEGIN createConnection(" + dataSourceJndiName + ")");
    
    try {
      DataSource dataSource = dataSourceMap.get(dataSourceJndiName);
      if (dataSource == null) {
        InitialContext ic = new InitialContext();
//        dataSource = (DataSource) ic.lookup(dataSourceJndiName);  Не работает в Tomcat
        dataSource = (DataSource) ic.lookup( "java:/comp/env/" + dataSourceJndiName);
        
        dataSourceMap.put(dataSourceJndiName, dataSource);
      }
      
      Connection con = dataSource.getConnection();

      connectionCount++;
      trace("createConnection(): connectionCount = " + connectionCount);
      
      return con;
    } catch (NamingException ex) {
      error(ex);
      throw new SystemException("DataSource '" + dataSourceJndiName + "' not found", ex);
    } catch (SQLException ex) {
      error(ex);
      throw new SystemException("Connection creation error for '" + dataSourceJndiName + "' dataSource", ex);
    } finally {
      trace("END createConnection(" + dataSourceJndiName + ")");
    }
  }

  /*
   * Ниже - методы, не используемые ни в JepRia, ни в JepCommon.
   * TODO Удалять ? 
   */
  public void commit() throws SQLException {
    trace("commit()");
    getConnection().commit();
  }

  /**
   * Используется при работе по DataSourceJndiName
   * @return true, если соединение закрыто
   */
  public boolean isClosed() {
    try {
      return connection == null || connection.isClosed();
    } catch (SQLException ex) {
      throw new SystemException("Check Db connection error", ex);
    }
  }

  private static void trace(String message) {
//    System.out.println("Db: " + message);
    logger.trace("Db: " + message);
  }

  private static void error(Exception ex) {
    logger.error("Db error: ", ex);
  }

  private static boolean isConnectByDefaultDataSource() {
    return dataSourceJndiName != null;
  }
  

//  private static boolean isConnectByDataSource(Map<String, String> connectionParameters) {
//    return connectionParameters.containsKey(DATA_SOURCE_CONNECTION_PARAMETER);
//  }

}