<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.LOGIN_ATTEMPTS_SESSION_ATTRIBUTE"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.MAX_LOGIN_ATTEMPTS"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_SSO_IS_ERROR"%>

<%
  Integer attempts = (Integer) session.getAttribute(LOGIN_ATTEMPTS_SESSION_ATTRIBUTE);
  if (attempts == null) {
    attempts = 0;
  }
  attempts++;
  session.setAttribute(LOGIN_ATTEMPTS_SESSION_ATTRIBUTE, attempts);
  
  RequestDispatcher rd = application.getRequestDispatcher("/WEB-INF/security/Login.jsp");
  request.setAttribute(HTTP_REQUEST_PARAMETER_SSO_IS_ERROR, true);
  rd.forward(request, response);
%>