<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.LOGIN_ATTEMPTS_SESSION_ATTRIBUTE" %>
<%@ page import="static com.technology.jep.jepria.server.JepRiaServerConstant.MAX_LOGIN_ATTEMPTS" %>
<%@ page import="java.util.ResourceBundle" %>
 
<%
  ResourceBundle resourceBundle = ResourceBundle.getBundle("com.technology.jep.jepria.shared.text.LoginText", request.getLocale());
  Integer attempts = (Integer) session.getAttribute(LOGIN_ATTEMPTS_SESSION_ATTRIBUTE);
  if (attempts == null){
    attempts = 0;
  }
  if (attempts++ < MAX_LOGIN_ATTEMPTS){
    session.setAttribute(LOGIN_ATTEMPTS_SESSION_ATTRIBUTE, attempts);
    RequestDispatcher rd = application.getRequestDispatcher("/security/loginNew.jsp");
    rd.forward(request, response);
  }
%>

<html>
  <head>
    <title><%= resourceBundle.getString("loginError.title") %></title>
    <link href="security/com/technology/jep/jepcommon/styles/Default.css" rel="stylesheet" type="text/css">
  </head>
  <body>

  <table style=" width: 100%; ">
    <tr><th id="loginError.title"><%= resourceBundle.getString("loginError.title") %></th></tr>
    <tr>
      <td>

        <table style=" width: 100%; " class="errors">
          <tr>
            <td style=" width: 22px "><img src="security/com/technology/jep/jepcommon/images/warning.gif"></td>
            <td id="loginError.attemptsError"><%= resourceBundle.getString("loginError.attemptsError") %></td>
          </tr>
        </table>
      </td>
    </tr>
    </table>
    
  </body>
</html>
