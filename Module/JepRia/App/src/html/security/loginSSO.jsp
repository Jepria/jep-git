<!DOCTYPE html>
<%@page import="com.technology.jep.jepria.server.util.JepServerUtil"%>
<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.LOGIN_ATTEMPTS_SESSION_ATTRIBUTE"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.MAX_LOGIN_ATTEMPTS"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.SSO_MODULE_URL"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_LOCALE"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_SOO_IS_ERROR"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_SOO_IS_BLOCKED"%>

<%
  String locale = request.getParameter(HTTP_REQUEST_PARAMETER_LOCALE);
  //Если в запросе есть локаль, то берем ее, и записываем в сессию.
  if(locale == null) {
    //если в запросе нет локали, пытаемся считать ее из сессии.
    locale = (String) session.getAttribute(HTTP_REQUEST_PARAMETER_LOCALE);
  } else {
    session.setAttribute(HTTP_REQUEST_PARAMETER_LOCALE, locale);
  }
  
  String ssoLoginWithBaseParameters = SSO_MODULE_URL + 
      "&" + HTTP_REQUEST_PARAMETER_LOCALE + "=" + locale + 
      "&enterModule=" + JepServerUtil.getApplicationName(application); 
  
  boolean isError = Boolean.TRUE.equals(request.getAttribute(HTTP_REQUEST_PARAMETER_SOO_IS_ERROR));
  Integer loginAttempts = (Integer) session.getAttribute(LOGIN_ATTEMPTS_SESSION_ATTRIBUTE);
  if (loginAttempts == null || !isError) {
    response.sendRedirect(ssoLoginWithBaseParameters);
  } else if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
    response.sendRedirect(ssoLoginWithBaseParameters + "&" + HTTP_REQUEST_PARAMETER_SOO_IS_BLOCKED + "=1");
  } else {
    response.sendRedirect(ssoLoginWithBaseParameters + "&" + HTTP_REQUEST_PARAMETER_SOO_IS_ERROR + "=1");
  }
%>