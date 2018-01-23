<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ page import="org.jepria.ssoutils.SsoUiUtils"%>
<%@ page import="org.jepria.ssoutils.SsoUiConstants"%>
<%@ page import="static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_LOCALE"%>
<%@ page import="com.technology.jep.jepria.server.security.SecurityFactory" %>
<%@ page import="static com.technology.jep.jepria.server.util.JepServerUtil.getLocale"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="javax.servlet.RequestDispatcher" %>

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
  
  String ssoUiContext = SsoUiUtils.getSsoUiContext(pageContext.getServletContext());
  
  // заголовок приложения
  String appTitle = config.getServletContext().getInitParameter("app.title");
  if (appTitle == null) {
    // если не указан явно, то это название приложения
    appTitle = config.getServletContext().getInitParameter("app.name");
    if (appTitle == null) {
      // заголовок по умолчанию это имя приложения из context path
      appTitle = request.getContextPath().substring(1);
    }
  }
  
  String ssoUiUrl = SsoUiUtils.buildSsoUiUrl(ssoUiContext, request, appTitle) +
      (locale == null ? "" : "&" + HTTP_REQUEST_PARAMETER_LOCALE + "=" + locale);
  
%>
  <HTML>
    <BODY>
      <script>
        var fragmentValue = window.location.hash.substr(1);
        var fragmentParam = "";
        if (fragmentValue.length > 0) {
          var encodeFragmentFunction = <%= SsoUiUtils.encodeFragmentFunction() %>;
          var fragmentValueEncoded = encodeFragmentFunction(fragmentValue);
          fragmentParam = "&" + "<%= SsoUiConstants.REQUEST_PARAMETER_FRAGMENT %>" + "=" + fragmentValueEncoded;
        }
        window.location.replace("<%= ssoUiUrl %>" + fragmentParam);
      </script>
    </BODY>
  </HTML>
<%
}
%>