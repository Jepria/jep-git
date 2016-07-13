package com.technology.jep.jepria.server.util;

import static org.junit.Assert.assertTrue;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.junit.Test;
import org.mockito.Mockito;

public class JepServerUtilTest {

  @Test
  public void getApplicationNameTestStandardContextRoot() {
    ServletContext contextMock = Mockito.mock(ServletContext.class);
    Mockito.when(contextMock.getContextPath()).thenReturn("/SomeApplication");
    
    assertTrue("SomeApplication".equals(JepServerUtil.getApplicationName(contextMock)));
  }

  @Test
  public void getApplicationNameTestLongContextRoot() {
    ServletContext contextMock = Mockito.mock(ServletContext.class);
    Mockito.when(contextMock.getContextPath()).thenReturn("long/context/root/SomeApplication");
    
    assertTrue("SomeApplication".equals(JepServerUtil.getApplicationName(contextMock)));
  }

  @Test
  public void getModuleNameTest() {
    ServletContext contextMock = Mockito.mock(ServletContext.class);
    Mockito.when(contextMock.getContextPath()).thenReturn("info/SomeApplication");
    ServletConfig configMock = Mockito.mock(ServletConfig.class);
    Mockito.when(configMock.getServletContext()).thenReturn(contextMock);
    Mockito.when(configMock.getServletName()).thenReturn("SomeModuleServlet");
    
    assertTrue("SomeApplication.SomeModule".equals(JepServerUtil.getModuleName(configMock)));
  }
  
}
