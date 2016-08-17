package com.technology.jep.jepria.service.test;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.junit.After;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.rpc.ServletMockProviderAdapter;
import com.technology.jep.jepcommon.security.JepPrincipal;
import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;

/**
 * Общая функциональность тестов сервисов JepRia
 * Поддерживает создание необходимого окружения для сервисного gwt-сервлета, в том числе создание источников данных 
 */
abstract public class JepRiaServiceTest<D extends JepDataStandard> extends GwtTest {
  private static Logger logger = Logger.getLogger(JepRiaServiceTest.class.getName());

  /**
   * Servlet API mock helpers from gwt-test-utils
   */
  private MockServletConfig mockConfig;
  
  private MyMockHttpServletRequest mockRequest;

  public void prepareServletEnvironment(JepDataServiceServlet<D> service) {
    prepareMockServlet();
  
    try {
      service.init();
    } catch (ServletException ex) {
      logger.error("Service init error", ex);
      fail(ex.getMessage());
    }
  }
  
  /**
   * Умолчательный источник данных, по которому выполняется аутентификация и авторизация
   */
  public static final DataSourceDef DEFAULT_DATASOURCE_DEF = new DataSourceDef("java:/comp/env/jdbc/RFInfoDS", "jdbc:oracle:thin:@//srvt14.d.t:1521/RFINFOT1", "information", "information");
  
  protected static void prepareDataSources(List<DataSourceDef> dataSourceDefs) throws SQLException {
    try {
      InitialContext ic = TestServiceUtil.prepareInitialContextForJdbc();

      if(!dataSourceDefs.contains(DEFAULT_DATASOURCE_DEF)) {
        dataSourceDefs.add(DEFAULT_DATASOURCE_DEF);
      }
      
      for(DataSourceDef dataSourceDef: dataSourceDefs) {
        TestServiceUtil.prepareDataSource(
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
  
  private void prepareMockServlet() {
    MyMockServletContext context = new MyMockServletContext();
    context.setContextPath("JepRiaShowcase");
    
    // create the ServletConfig object using gwt-test-utils web mock helper
    this.mockConfig = new MockServletConfig(context);

    // same thing for HttpServletRequest
    this.mockRequest = new MyMockHttpServletRequest();
    
    this.mockRequest.addHeader("User-Agent", "mocked-user-agent");
    this.mockRequest.setUserPrincipal(new JepPrincipal("NagornyyS"));
    
    this.mockRequest.setServletContext(context);
    
    // use the provided adapter to implement only the methods you need for your test
    setServletMockProvider(new ServletMockProviderAdapter() {

        @Override
        public ServletConfig getMockedConfig(AbstractRemoteServiceServlet remoteService) {
            return mockConfig;
        }

        @Override
        public HttpServletRequest getMockedRequest(AbstractRemoteServiceServlet rpcService, Method rpcMethod) {
            return mockRequest;
        }
    });
  }
}

class MyMockServletContext extends MockServletContext {
  private String contextPath = null;
  
  public String getContextPath() {
    return contextPath;
  }
  
  void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }
}

class MyMockHttpServletRequest extends MockHttpServletRequest {
  private ServletContext servletContext;
  
  public ServletContext getServletContext() {
    return servletContext;
  }
  
  void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }
}
