<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.SSO_PROTECTED_URL"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.REQUEST_PARAMETER_ENTER_MODULE"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.REQUEST_PARAMETER_QUERY_STRING"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_LOCALE"%>
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
  
  // Нельзя передавать символ & как значение параметра запроса, поэтому заменяем его на %26
  String forwardQueryString = (String)request.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
  if (forwardQueryString != null) {
	forwardQueryString = forwardQueryString.replaceAll("&", "%26");
  }
  
  String ssoLoginWithBaseParameters = SSO_PROTECTED_URL + "?" + 
      (locale == null ? "" : "&" + HTTP_REQUEST_PARAMETER_LOCALE + "=" + locale) + 
      "&" + REQUEST_PARAMETER_ENTER_MODULE + "=" + (String)request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI) +
      (forwardQueryString == null ? "" : "&" + REQUEST_PARAMETER_QUERY_STRING + "=" + forwardQueryString); 
  
  response.sendRedirect(ssoLoginWithBaseParameters);
}
%>