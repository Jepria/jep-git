package com.technology.jep.jepria.server.test;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jepria.sso.utils.JepPrincipal;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.rpc.ServletMockProviderAdapter;
import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.load.FindConfig;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Общая функциональность тестов сервисов JepRia
 * Поддерживает создание необходимого окружения для сервисного gwt-сервлета, в том числе создание источников данных 
 */
abstract public class JepRiaServiceTestBase<D extends JepDataStandard> extends GwtTest {
  private static Logger logger = Logger.getLogger(JepRiaServiceTestBase.class.getName());

  protected JepDataServiceServlet<D> service;
  private List<FindConfig> clearAfterTest;

  /**
   * Servlet API mock helpers from gwt-test-utils
   */
  private MockServletConfig mockConfig;
  
  private MyMockHttpServletRequest mockRequest;

  protected void beforeServiceTest(JepDataServiceServlet<D> service) {
    this.service = service; 
    prepareServletEnvironment(service);
    clearAfterTest = new ArrayList<FindConfig>();
  }

  protected void afterServiceTest() {
    clearRecords();
    //logout(); TODO Нужен logoff
    service = null;
  }
  
  protected void clearAfterTest(FindConfig createConfig) {
    clearAfterTest.add(createConfig);
  }

  protected void prepareServletEnvironment(JepDataServiceServlet<D> service) {
    prepareMockServlet();
  
    try {
      service.init();
    } catch (ServletException ex) {
      logger.error("Service init error", ex);
      fail(ex.getMessage());
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

  /**
   * Удаление записей по списку конфигураций записей
   */
  protected void clearRecords() {
    for (FindConfig recordConfig : clearAfterTest) {
      try {
        service.delete(recordConfig);
      } catch (ApplicationException ex) {
        logger.error("Record deletion error", ex);
      }
    }
  }
  
  
  /**
   * Создание записи с заданными полями в БД
   */
  protected JepRecord createRecordInDb(boolean rememberToDelete, Map<String, Object> fieldMap) {
    JepRecord featureRecord = new JepRecord();
    for(String fieldName: fieldMap.keySet()) {
      featureRecord.set(fieldName, fieldMap.get(fieldName));
    }
    
    FindConfig createConfig = new FindConfig(featureRecord);
    JepRecord resultRecord = null;
    try {
      resultRecord = service.create(createConfig);
      if(rememberToDelete) {
        clearAfterTest.add(createConfig);
      }
    } catch (ApplicationException ex) {
      fail("Create record error:" + ex.getMessage());
    }
    
    return resultRecord;
  }

  /**
   * Создание записи с заданными полями в БД с указанием последующей очистки базы
   */
  protected JepRecord createRecordInDb(Map<String, Object> fieldMap) {
    return createRecordInDb(true, fieldMap);
  }
  
  protected PagingResult<JepRecord> findById(String recordIdKey, Object recordIdValue) {
    JepRecord templateRecord = new JepRecord();
    templateRecord.set(recordIdKey, recordIdValue);
    PagingConfig pagingConfig = new PagingConfig(templateRecord);
    PagingResult<JepRecord> pagingResult = null;
    try {
      pagingResult = service.find(pagingConfig);
    } catch (ApplicationException ex) {
      fail("findById error: " + ex.getMessage());
    }
    return pagingResult;
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
