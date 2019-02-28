package com.technology.jep.jepria.server.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.dao.OracleCallableStatementWrapper;
import com.technology.jep.jepria.shared.exceptions.SystemException;

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
   * Объект соединения.
   * Не должен быть доступен извне.
   */
  private Connection connection;
  
  /**
   * JNDI-имя источника данных.
   */
  private String dataSourceJndiName;
  
  /**
   * Хэш-таблица statement'ов по SQL-тексту.
   * Необходима для хранения открытых курсоров.
   */
  private Map<String, CallableStatement> statementsMap = new ConcurrentHashMap<String, CallableStatement>();

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
    this.dataSourceJndiName = dataSourceJndiName;
  }

  /**
   * Извлекает statement из хэш-таблицы или создаёт при отсутствии.
   * @param sql SQL-шаблон
   * @return объект statement
   */
  public CallableStatement prepare(String sql) {
    CallableStatement cs = (CallableStatement) statementsMap.get(sql);
    if (cs == null) {
      try {
        cs = OracleCallableStatementWrapper.wrap(getConnection().prepareCall(sql));
      } catch(SQLException th) {
        throw new SystemException("PrepareCall Error for query '" + sql + "'", th);
      }

      statementsMap.put(sql, cs);
    }
    return cs;
  }

  /**
   * Метод закрывает все ресурсы, связанные с данным Db: connection и все курсоры из statementsMap.
   * Должен вызываться в конце сессии пользователя.
   */
  public void closeAll() {
    logger.trace("closeAll()");
    
    Iterator<String> it = statementsMap.keySet().iterator();
    while (it.hasNext()) {
      CallableStatement cs = (CallableStatement) statementsMap.get(it.next());
      try {
        cs.close();
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
    statementsMap.clear();
    try {
      if(connection != null) {
        connection.close();
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    connection = null;
  }

  /**
   * Закрытие statement.<br/>
   * При возникновении исключения <code>SQLException</code> оно лишь логгируется.
   * @param sql SQL-код
   * @return <code>true</code>, если соединение существовало, <code>false</code> в противном случае
   */
  public boolean closeStatement(String sql) {
    logger.trace("closeStatement()");
    
    Statement st = statementsMap.get(sql);
    boolean existsFlag = st != null;
    try {
      /*
       * TODO: Если statement не удалось найти, вероятно, это свидетельствует об ошибке
       * в вызывающем коде. Возможно, в данной ситуации нужно выбрасывать IllegalArgumentException,
       * а тип метода изменить на void.
       */
      if(existsFlag) {
        st.close();
      }
    } catch (SQLException e) {
      logger.error(e);
    }
    statementsMap.remove(sql);
    return existsFlag;
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
   * Откат (rollback) транзакции.<br/>
   * В случае возникновения ошибки исключение не выбрасывается, а лишь записывается в лог.
   * Откат сам по себе происходит в результате некоей ошибки. Если вызов отката
   * привёл ещё к одному исключению, не остаётся уже ничего, кроме как закрыть соединение.
   */
  public void rollback() {
    logger.trace("rollback()");
    try {
      /*
       * Соединение может ещё не быть инициализировано, если вызвавшая rollback ошибка
       * возникла до выполнения первого запроса.
       */
      if (connection != null) {
        connection.rollback();
      }
    } catch (SQLException ex) {
      logger.error(ex);
    }
  }
  
  /**
   * Получение соединения JDBC.<br/>
   * Если соединение закрыто или не создано, оно создаётся.
   * @return соединение JDBC
   */
  private Connection getConnection() {
    if(this.isClosed()) {
      connection = createConnection(dataSourceJndiName);
    }
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

  /**
   * Проверяет, содержит ли данный объект открытое подключение JDBC.
   * @return <code>true</code>, если соединение не создано или закрыто, <code>false</code> в противном случае
   */
  private boolean isClosed() {
    try {
      return connection == null || connection.isClosed();
    } catch (SQLException ex) {
      throw new SystemException("Check Db connection error", ex);
    }
  }

};