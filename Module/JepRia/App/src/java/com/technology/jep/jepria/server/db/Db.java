package com.technology.jep.jepria.server.db;

import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;
import com.technology.jep.jepria.shared.exceptions.SystemException;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Обёртка для доступа к БД.
 * Реализуются сервисы для получения и аккуратного закрытия JDBC ресурсов.
 */
public class Db {
  private static Logger logger = Logger.getLogger(Db.class.getName());

  /**
   * Хэш-таблица источников данных по их JNDI-именам.
   */
  private static Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<String, DataSource>();

  /**
   * JNDI-имя источника данных.
   */
  private String dataSourceJndiName;

  /**
   * Хэш-таблица statement'ов по SQL-тексту.
   * Необходима для хранения открытых курсоров.
   */
//  private Map<String, CallableStatement> statementsMap = new ConcurrentHashMap<String, CallableStatement>();

  /**
   * Создаёт объект соединения с базой с автоматическим коммитом.
   * @param dataSourceJndiName JNDI-имя источника данных
   */
  public Db(String dataSourceJndiName) {
    this(dataSourceJndiName, true);
  }

  /**
   * Создаёт объект соединения с базой.
   * @param dataSourceJndiName JNDI-имя источника данных
   * @param autoCommit если true, все изменения автоматически коммитятся
   */
  public Db(String dataSourceJndiName, boolean autoCommit) {
    logger.info("Db(" + "dataSourceJndiName" + ", " + autoCommit + ") BEGIN");
    this.dataSourceJndiName = dataSourceJndiName;
    logger.info("Db() END");
  }

  /**
   * Создаёт CallableStatement для шаблону запроса по dataSourceJndiName экземпляра.
   * @param sql SQL-шаблон
   * @return объект statement
   */
  public CallableStatement prepare(Connection connection, String sql) {
    try {
      logger.info("prepare(" + sql + ") BEGIN");
      CallableStatement cs = connection.prepareCall(sql);
      return cs;
    } catch(SQLException th) {
      throw new SystemException("PrepareCall Error for query '" + sql + "'", th);
    } finally {
      logger.info("prepare(" + sql + ") END");
    }
  }


  /**
   * Метод закрывает все ресурсы, связанные с данным Db: connection и все курсоры из statementsMap.
   * Должен вызываться в конце сессии пользователя.
   * @deprecated соединения должны закрываться в коде, их создающем
   */
  public void closeAll() {
    throw new NotImplementedYetException();
  }

  /**
   * Фиксация (commit) транзакции.
   * @throws SQLException в случае возникновения ошибки взаимодействия с базой
   */
  public void commit() throws SQLException {
    logger.trace("commit()");
    getConnection().commit();
  }

  /**
   * Получение соединения JDBC.<br/>
   * Если соединение закрыто или не создано, оно создаётся.
   * @return соединение JDBC
   */
  public Connection getConnection() {
    Connection connection = createConnection(dataSourceJndiName);
    return connection;
  }

  /**
   * Фабричный метод, создающий соединение JDBC.
   * Поиск источника данных JDBC осуществляется по JNDI. Найденный источник заносится
   * в хэш-таблицу и при повторном вызове берётся из неё, поэтому метод имеет
   * модификатор <code>synchronized</code>.
   * @param dataSourceJndiName JNDI-имя источника данных
   * @return соединение JDBC
   */
  private static Connection createConnection(String dataSourceJndiName) {
    logger.trace("BEGIN createConnection(" + dataSourceJndiName + ")");
    
    try {
      DataSource dataSource = dataSourceMap.get(dataSourceJndiName);
      if (dataSource == null) {
        InitialContext ic = new InitialContext();
        try {
          dataSource = (DataSource) ic.lookup(dataSourceJndiName);  // Для oc4j и weblogic
        } catch(NamingException nex) { // Теперь пробуем в другом контексте (для Tomcat)
          logger.trace("Failed lookup for '" + dataSourceJndiName + "', try now '" + "java:/comp/env/" + dataSourceJndiName + "'");
          dataSourceJndiName = "java:/comp/env/" + dataSourceJndiName;
          dataSource = (DataSource) ic.lookup(dataSourceJndiName);
        }
        logger.trace("Successfull lookup for " + dataSourceJndiName);
        
        dataSourceMap.put(dataSourceJndiName, dataSource);
      }
      Connection con = dataSource.getConnection();
      con.setAutoCommit(false);
      return con;
    } catch (NamingException ex) {
      logger.error(ex);
      throw new SystemException("DataSource '" + dataSourceJndiName + "' not found", ex);
    } catch (SQLException ex) {
      logger.error(ex);
      throw new SystemException("Connection creation error for '" + dataSourceJndiName + "' dataSource", ex);
    } finally {
      logger.trace("END createConnection(" + dataSourceJndiName + ")");
    }
  }

};