package com.technology.jep.jepriashowcase.feature.server.service;

import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DESCRIPTION;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME_EN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.FailedLoginException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.rpc.ServletMockProviderAdapter;
import com.technology.jep.jepcommon.security.JepPrincipal;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.load.FindConfig;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.test.TestServiceUtil;

@GwtModule("com.technology.jep.jepriashowcase.feature.Feature")
public class FeatureServiceTest extends GwtTest {
  
  private static Logger logger = Logger.getLogger(FeatureServiceTest.class.getName());


  FeatureServiceImpl service;

  /**
   * Servlet API mock helpers from gwt-test-utils
   **/
  private MockServletConfig mockConfig;
  private MockHttpServletRequest mockRequest;

  @BeforeClass
  public static void setUpClass() throws Exception {
    try {
      InitialContext ic = TestServiceUtil.prepareInitialContextForJdbc();
      TestServiceUtil.prepareDataSource(ic, "jdbc:oracle:thin:@//srvt14.d.t:1521/RFINFOT1", "information", "information", "java:/comp/env/jdbc/RFInfoDS");
      TestServiceUtil.prepareDataSource(ic, "jdbc:oracle:thin:@//srvt14.d.t:1521/RFINFOT1", "itm", "itm", "java:/comp/env/jdbc/ITMDS");
    } catch (NamingException ex) {
        logger.error("Lookup error", ex);
    }
  }  

  @Before
  public void before() throws FailedLoginException {
    prepareMockServlet();

    try {
      service = new FeatureServiceImpl();
      service.init();
    } catch (ServletException ex) {
      logger.error("Service init error", ex);
      fail(ex.getMessage());
    }
  }
  
  private void prepareMockServlet() {
    // create the ServletConfig object using gwt-test-utils web mock helper
    MyMockServletContext context = new MyMockServletContext();
    context.setContextPath("JepRiaShowcase");
    this.mockConfig = new MockServletConfig(context);

    // same thing for HttpServletRequest
    this.mockRequest = new MockHttpServletRequest();
    this.mockRequest.addHeader("User-Agent", "mocked-user-agent");


    this.mockRequest.setUserPrincipal(new JepPrincipal("NagornyyS"));
    
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

  @After
  public void tearDown() throws SQLException {
//    logout();
    service = null;
  }

  @Test
  public void testCreate() throws ApplicationException {
    JepRecord testRecord = new JepRecord();
    testRecord.set(FEATURE_NAME, "FEATURE_NAME");
    testRecord.set(FEATURE_NAME_EN, "FEATURE_NAME_EN");
    testRecord.set(DESCRIPTION, "DESCRIPTION");   // Clob
    FindConfig createConfig = new FindConfig(testRecord);
    JepRecord resultRecord = service.create(createConfig);
    
    assertEquals(testRecord, resultRecord);
//    fail("!!!");
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
