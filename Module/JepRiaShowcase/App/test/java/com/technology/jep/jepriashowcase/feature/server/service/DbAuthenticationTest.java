package com.technology.jep.jepriashowcase.feature.server.service;

import static com.technology.jep.jepria.server.JepRiaServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepria.server.security.JepSecurityConstant.GUEST_LOGIN;
import static com.technology.jep.jepria.server.security.JepSecurityConstant.GUEST_PASSWORD;
//import static org.junit.Assert.;


import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.FailedLoginException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.technology.jep.jepcommon.security.pkg_Operator;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.service.test.TestServiceUtil;

/**
 * Пример тестирования интерфейсного Db-функционала
 * TODO На самом деле здесь небольшой пример тестирования системного функционала, нужно переместить это в JepRia, а
 * TODO а тесты Db-части приложения ещё нужно разработать)  
 */
public class DbAuthenticationTest extends Assert {
  
  private static Logger logger = Logger.getLogger(DbAuthenticationTest.class.getName());
  
  @BeforeClass
  public static void setUpClass() throws Exception {
    try {
      InitialContext ic = TestServiceUtil.prepareInitialContextForJdbc();
      TestServiceUtil.prepareDataSource(ic, "jdbc:oracle:thin:@//srvt14.d.t:1521/RFINFOT1", "information", "information", "java:/comp/env/jdbc/RFInfoDS");
    } catch (NamingException ex) {
        logger.error("Lookup error", ex);
    }
  }  
  
  @Before
  public void before() throws FailedLoginException {
    logger.warn("before()");
  }

  @After
  public void after() throws SQLException {
    logger.warn("after()");
  }
  
  
  public static final String ANOTHER_LOGIN = "NagornyyS";
  public static final String ANOTHER_PASSWORD = "123";
  
  @Test
  public void CurrentUserShouldChangeOnLogon() {
    try {
      Integer guestOperatorId = pkg_Operator.logon(getDb(), GUEST_LOGIN, GUEST_PASSWORD);
      assertEquals(guestOperatorId, pkg_Operator.logon(getDb(), GUEST_LOGIN));

      logger.debug("BEFORE LOGON: pkg_Operator.logon(getDb(), GUEST_LOGIN) = " + pkg_Operator.logon(getDb(), GUEST_LOGIN));
      logger.debug("BEFORE LOGON: pkg_Operator.logon(getDb(), ANOTHER_LOGIN) = " + pkg_Operator.logon(getDb(), ANOTHER_LOGIN));

      Integer anotherOperatorId = pkg_Operator.logon(getDb(), ANOTHER_LOGIN, ANOTHER_PASSWORD);
      assertEquals(anotherOperatorId, pkg_Operator.logon(getDb(), ANOTHER_LOGIN));
      assertEquals(guestOperatorId, pkg_Operator.logon(getDb(), GUEST_LOGIN));
      assertFalse(guestOperatorId.equals(pkg_Operator.logon(getDb(), ANOTHER_LOGIN)));
//      org.junit.Assert.assertNotEquals(guestOperatorId, pkg_Operator.logon(getDb(), ANOTHER_LOGIN));
      
    } catch (SQLException ex) {
      fail(ex.getMessage());
    }
    
  }
  
  /**
   * Соединение с базой данных.
   */
  private Db db = null;
  private Db getDb() {
    if (this.db == null) {
      this.db = new Db(DATA_SOURCE_JNDI_NAME);
    }
    return this.db;
  }
}
