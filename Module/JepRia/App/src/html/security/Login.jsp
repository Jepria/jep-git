<!DOCTYPE html>
<%@page import="com.technology.jep.jepria.server.util.JepServerUtil"%>
<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.LOGIN_ATTEMPTS_SESSION_ATTRIBUTE"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.MAX_LOGIN_ATTEMPTS"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.SSO_PROTECTED_URL"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.REQUEST_PARAMETER_ENTER_MODULE"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.REQUEST_PARAMETER_QUERY_STRING"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_LOCALE"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_SSO_IS_ERROR"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_SSO_IS_BLOCKED"%>
<%@ page import="com.technology.jep.jepria.server.security.SecurityFactory" %>
<%@ page import="static com.technology.jep.jepria.server.util.JepServerUtil.getLocale"%>
<%@ page import="java.util.ResourceBundle" %>

<%
if (response.getStatus() == 403) {
  // Обработка ошибки 403 при входе в модуль
    
  if ("POST".equals(request.getMethod())) {
    // Если пользователь сознательно согласился на перелогин
    SecurityFactory.getSecurityModule(request).logout(request, response, null);
    
    String originalQueryString = (String)request.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
    String originalRequestUrl = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI)
        + (originalQueryString == null ? "" : "?" + originalQueryString);
    
    response.sendRedirect(originalRequestUrl);
  } else {
 	// Если пользователю нужно предложить сознательный перелогин
%>
	<HTML>
	    <BODY>
	    	<form id="reloginForm" action="" method="post">
	    	  <%
	    	  ResourceBundle jepRiaText = ResourceBundle.getBundle("com.technology.jep.jepria.shared.text.JepRiaText", getLocale(request)); 
	    	  String textError = String.format(jepRiaText.getString("entrance.error403.text_error"),
	    	      SecurityFactory.getSecurityModule(request).getUsername());
	    	  String textRelogin = jepRiaText.getString("entrance.error403.text_relogin");
	    	  %>
	    	  <%= textError %> 
	    	  <a href="#" onclick="document.getElementById('reloginForm').submit();"><%= textRelogin %></a>
	      	</form> 
	    </BODY>
	</HTML>
<% 
  }
} else {
  String locale = request.getParameter(HTTP_REQUEST_PARAMETER_LOCALE);
  //Если в запросе есть локаль, то берем ее, и записываем в сессию.
  if(locale == null) {
    //если в запросе нет локали, пытаемся считать ее из сессии.
    locale = (String) session.getAttribute(HTTP_REQUEST_PARAMETER_LOCALE);
  } else {
    session.setAttribute(HTTP_REQUEST_PARAMETER_LOCALE, locale);
  }
  
  String ssoLoginWithBaseParameters = SSO_PROTECTED_URL + "?" + 
      (locale == null ? "" : "&" + HTTP_REQUEST_PARAMETER_LOCALE + "=" + locale) + 
      "&" + REQUEST_PARAMETER_ENTER_MODULE + "=" + JepServerUtil.getApplicationName(application) +
      "&" + REQUEST_PARAMETER_QUERY_STRING + "=" + request.getQueryString(); 
  
  boolean isError = Boolean.TRUE.equals(request.getAttribute(HTTP_REQUEST_PARAMETER_SSO_IS_ERROR));
  Integer loginAttempts = (Integer) session.getAttribute(LOGIN_ATTEMPTS_SESSION_ATTRIBUTE);
  if (loginAttempts != null && loginAttempts >= MAX_LOGIN_ATTEMPTS) {
    response.sendRedirect(ssoLoginWithBaseParameters + "&" + HTTP_REQUEST_PARAMETER_SSO_IS_BLOCKED + "=1");
  } else if (isError) {
    response.sendRedirect(ssoLoginWithBaseParameters + "&" + HTTP_REQUEST_PARAMETER_SSO_IS_ERROR + "=1");
  } else {
    response.sendRedirect(ssoLoginWithBaseParameters);
  }
}
%>