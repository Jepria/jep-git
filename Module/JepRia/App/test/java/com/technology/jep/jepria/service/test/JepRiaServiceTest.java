package com.technology.jep.jepria.service.test;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.rpc.ServletMockProviderAdapter;
import com.technology.jep.jepcommon.security.JepPrincipal;
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
abstract public class JepRiaServiceTest<D extends JepDataStandard> extends GwtTest {
  private static Logger logger = Logger.getLogger(JepRiaServiceTest.class.getName());

  protected JepDataServiceServlet<D> service;
  private List<FindConfig> toDeleteAfterTest;

  /**
   * Servlet API mock helpers from gwt-test-utils
   */
  private MockServletConfig mockConfig;
  
  private MyMockHttpServletRequest mockRequest;

  protected void beforeServiceTest(JepDataServiceServlet<D> service) {
    this.service = service; 
    prepareServletEnvironment(service);
    toDeleteAfterTest = new ArrayList<FindConfig>();
  }

  protected void afterServiceTest() {
    clearRecords();
    //logout(); TODO Нужен logoff
    service = null;
  }
  
  protected void addToClear(FindConfig createConfig) {
    toDeleteAfterTest.add(createConfig);
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
  
  /**
   * Умолчательный источник данных, по которому выполняется аутентификация и авторизация
   */
  private static final DataSourceDef DEFAULT_DATASOURCE_DEF = new DataSourceDef("java:/comp/env/jdbc/RFInfoDS", "jdbc:oracle:thin:@//srvt14.d.t:1521/RFINFOT1", "information", "information");
  
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
  
  /**
   * Проверка принадлежности совпадения значений множества полей testRecord со значениями одноимённых полей otherRecord
   * @param testRecord проверяемая запись
   * @param otherRecord 
   * @return true, если значения множества полей testRecord совпадают со значениями одноимённых полей otherRecord
   */
  protected boolean isFieldValueSubSet(JepRecord testRecord, JepRecord otherRecord) {
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

  /**
   * Удаление записей по списку конфигураций записей
   */
  protected void clearRecords() {
    for (FindConfig recordConfig : toDeleteAfterTest) {
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
  protected JepRecord createRecordInDb(boolean rememberToDelete, Map<String, String> fieldMap) {
    JepRecord featureRecord = new JepRecord();
    for(String fieldName: fieldMap.keySet()) {
      featureRecord.set(fieldName, fieldMap.get(fieldName));
    }
    
    FindConfig createConfig = new FindConfig(featureRecord);
    JepRecord resultRecord = null;
    try {
      resultRecord = service.create(createConfig);
      if(rememberToDelete) {
        toDeleteAfterTest.add(createConfig);
      }
    } catch (ApplicationException ex) {
      fail("Create Feature record error:" + ex.getMessage());
    }
    
    return resultRecord;
  }

  protected JepRecord createRecordInDb(Map<String, String> fieldMap) {
    return createRecordInDb(true, fieldMap);
  }
  
  
  /**
   * Создание записи с заданными полями
   */
  protected JepRecord createRecord(Map<String, String> fieldMap) {
    JepRecord record = new JepRecord();
    for(String fieldName: fieldMap.keySet()) {
      record.set(fieldName, fieldMap.get(fieldName));
    }
    
    return record;
  }
  

  protected JepRecord updateRecord(JepRecord record, Map<String, String> fieldMap) {
    for(String fieldName: fieldMap.keySet()) {
      record.set(fieldName, fieldMap.get(fieldName));
    }
    
    return record;
  }
  
  protected PagingResult<JepRecord> findById(JepRecord record, String recordId) {
    JepRecord templateRecord = new JepRecord();
    templateRecord.set(recordId, record.get(recordId));
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
