<!DOCTYPE html>
<%@page import="com.technology.jep.jepria.server.util.JepServerUtil"%>
<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.LOGIN_ATTEMPTS_SESSION_ATTRIBUTE" %>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.MAX_LOGIN_ATTEMPTS" %>
<%@ page import="java.util.ResourceBundle" %>

<% 
  ResourceBundle resourceBundle = ResourceBundle.getBundle("com.technology.jep.jepria.shared.text.LoginText", request.getLocale());
  Integer loginAttempts = (Integer) session.getAttribute(LOGIN_ATTEMPTS_SESSION_ATTRIBUTE);
  if (loginAttempts == null){
    response.sendRedirect("/SSO/SSO.jsp?em=Login&locale=" + request.getParameter("locale") + "&enterModule=" + JepServerUtil.getApplicationName(application));
  }
  else if (loginAttempts >= MAX_LOGIN_ATTEMPTS){
     RequestDispatcher rd = application.getRequestDispatcher("/security/loginerrorNew.jsp");
     rd.forward(request, response);
  }
  else if (loginAttempts > 0) {
    response.sendRedirect("/SSO/SSO.jsp?em=Login&locale=" + request.getParameter("locale") +  "&enterModule=" + JepServerUtil.getApplicationName(application) +
	  		"&isError=1");
  }
%>