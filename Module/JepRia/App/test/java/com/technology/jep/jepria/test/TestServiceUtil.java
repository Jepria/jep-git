package com.technology.jep.jepria.test;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import org.apache.log4j.Logger;

public class TestServiceUtil {
  private static Logger logger = Logger.getLogger(TestServiceUtil.class.getName());

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

  public static void prepareDataSource(InitialContext ic, String jdbcUrl, String dbUsername, String dbPassword, String jndiName) throws SQLException, NamingException {
    OracleConnectionPoolDataSource dsPool = new OracleConnectionPoolDataSource();
    dsPool.setURL(jdbcUrl);
    dsPool.setUser(dbUsername);
    dsPool.setPassword(dbPassword);
    
    ic.bind(jndiName, dsPool);
    
    logger.info("DataSource '" + jndiName + "' has created");
  }
}
