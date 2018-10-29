package com.technology.jep.jepria.server.test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.shared.record.JepRecord;

public class ServerTestUtil {
  private static Logger logger = Logger.getLogger(ServerTestUtil.class.getName());
  
  /**
   * Умолчательный источник данных, по которому выполняется аутентификация и авторизация
   */
  private static final DataSourceDef DEFAULT_DATASOURCE_DEF = new DataSourceDef("java:/comp/env/jdbc/RFInfoDS", "jdbc:oracle:thin:@//db-server:1521/Oracle", "login", "password");

  public static InitialContext prepareInitialContextForJdbc() throws NamingException {
    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
    System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");            
    InitialContext ic = new InitialContext();

    ic.createSubcontext("java:");
    ic.createSubcontext("java:/comp");
    ic.createSubcontext("java:/comp/env");
    ic.createSubcontext("java:/comp/env/jdbc");
    
    return ic;
  }

  public static void prepareDataSource(InitialContext ic, String jndiName, String jdbcUrl, String dbUsername, String dbPassword) throws SQLException, NamingException {
    OracleConnectionPoolDataSource dsPool = new OracleConnectionPoolDataSource();
    dsPool.setURL(jdbcUrl);
    dsPool.setUser(dbUsername);
    dsPool.setPassword(dbPassword);
    
    ic.rebind(jndiName, dsPool);
    
    logger.info("DataSource '" + jndiName + "' has created");
  }
  
  /**
   * Создание записи с заданными полями
   */
  public static JepRecord createRecord(Map<String, Object> fieldMap) {
    JepRecord record = new JepRecord();
    for(String fieldName: fieldMap.keySet()) {
      record.set(fieldName, fieldMap.get(fieldName));
    }
    return record;
  }

  /**
   * Создание записи с единственным полем recordId
   */
  public static JepRecord createRecordWithRecordId(String recordIdKey, Object recordId) {
    JepRecord record = new JepRecord();
    record.put(recordIdKey, recordId);
    return record;
  }
  
  
  public static JepRecord updateRecord(JepRecord record, Map<String, Object> fieldMap) {
    for(String fieldName: fieldMap.keySet()) {
      record.set(fieldName, fieldMap.get(fieldName));
    }
    
    return record;
  }
  
  public static void prepareDataSources(List<DataSourceDef> dataSourceDefs) throws SQLException {
    try {
      InitialContext ic = ServerTestUtil.prepareInitialContextForJdbc();

      if(!dataSourceDefs.contains(DEFAULT_DATASOURCE_DEF)) {
        dataSourceDefs.add(DEFAULT_DATASOURCE_DEF);
      }
      
      for(DataSourceDef dataSourceDef: dataSourceDefs) {
        ServerTestUtil.prepareDataSource(
            ic,
            dataSourceDef.dataSourceName,
            dataSourceDef.jdbcUrl,
            dataSourceDef.username,
            dataSourceDef.password);
      }
    } catch (NamingException ex) {
        logger.error("DataSource create error", ex);
    }
  }
  
  /**
   * Проверка принадлежности совпадения значений множества полей testRecord со значениями одноимённых полей otherRecord
   * @param testRecord проверяемая запись
   * @param otherRecord 
   * @return true, если значения множества полей testRecord совпадают со значениями одноимённых полей otherRecord
   */
  public static boolean isFieldValueSubSet(JepRecord testRecord, JepRecord otherRecord) {
    if(otherRecord == null)
      return false;
    
    Collection<String> propertyNames = testRecord.keySet();
    for(String name: propertyNames) {
      Object property = testRecord.get(name);
      if (property == null) {
        if (otherRecord.get(name) != null)
          return false;
      } else if (!property.equals(otherRecord.get(name)))
        return false;
    }
    
    return true;
  }

}
