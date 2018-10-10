package com.technology.jep.jepria.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.technology.jep.jepria.server.security.SecurityFactory;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Абстрактный предок сервисов Jep.
 */
@SuppressWarnings("serial")
abstract public class JepServiceServlet extends RemoteServiceServlet implements RemoteService {
  protected static Logger logger = Logger.getLogger(JepServiceServlet.class.getName());  

  /**
   * Получение идентификатора пользователя.
   * 
   * @return идентификатор пользователя
   */
  protected Integer getOperatorId() {
    return SecurityFactory.getSecurityModule(getThreadLocalRequestWrapper()).getOperatorId();
  }
  
  /**
   * Проверка роли. <br/>
   * Использует {@link com.technology.jep.jepria.server.security.JepSecurityModule#isRole(String, boolean)}.
   * 
   * @param role проверяемая роль
   * @param makeError  признак: выбрасывать исключение (значение true) или нет (значение false)
   * @return true - если текущему оператору принадлежит роль role, false или исключение
   * (в зависимости от параметра makeError) в противном случае
   * @throws Exception
   */
  protected boolean isRole(String role, boolean makeError) throws ApplicationException {
    return SecurityFactory.getSecurityModule(getThreadLocalRequestWrapper()).isRole(role, makeError);
  }
  
  protected ApplicationException buildException(String message, Throwable th) {
    return new ApplicationException(message, th);
  }
  
  protected ApplicationException buildException(Throwable th) {
    return buildException(null, th);
  }
  
  /**
   * Предоставляет возможность переопределить в потомках получения HTTPRequestServlet другим способом.
   *
   * @return the HttpServletRequest object for the current call. It is stored thread-locally so that simultaneous invocations can have different request objects.
   */
  protected HttpServletRequest getThreadLocalRequestWrapper() {
      return getThreadLocalRequest();
  }
  
  /**
   * Предоставляет возможность переопределить в потомках получения HttpServletResponse другим способом.
   *
   * @return the HttpServletResponse object for the current call. It is stored thread-locally so that simultaneous invocations can have different response objects.
   */
  
  protected HttpServletResponse getThreadLocalResponseWrapper() {
      return getThreadLocalResponse();
  }
}
