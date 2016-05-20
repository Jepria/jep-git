<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=utf-8"%>

<jsp:directive.page import="oracle.security.jazn.sso.app.*" />
<jsp:directive.page import="oracle.security.jazn.util.Env" />
<jsp:directive.page import="oracle.security.jazn.resources.FrameworkResourceBundle" />
<jsp:directive.page import="oracle.security.jazn.util.Resources" />

<jsp:directive.page import="java.util.ResourceBundle" />
<jsp:directive.page import="com.technology.jep.jepria.server.security.cas.LoginRedirectFilter" />

<jsp:declaration>
 private static final String MAX_LOGIN_ATTEMPTS = "max-login-attempts";
 private int _maxLoginAttempts = 3;
 private int _sessionTimeout;
</jsp:declaration>

<jsp:scriptlet>
	FrameworkResourceBundle _bundle = FrameworkResourceBundle.getResourceBundle(request.getLocales());
	ResourceBundle resourceBundle = ResourceBundle.getBundle("com.technology.jep.jepria.shared.text.LoginText", request.getLocale());

	Integer attempts = (Integer)session.getAttribute(MAX_LOGIN_ATTEMPTS);
	int loginAttempts = 0;
	if (null != attempts){
		loginAttempts = attempts.intValue();
		if(loginAttempts >= _maxLoginAttempts){
			RequestDispatcher rd = application.getRequestDispatcher("/loginerror.jsp");
			rd.forward(request, response);   
		}
	}
	session.setMaxInactiveInterval(_sessionTimeout);
	String dir = _bundle.isLocaleRTL() ? "rtl" : "ltr"; 
</jsp:scriptlet>

<html lang="<%=_bundle.getResourceLocale().getLanguage()%>"    dir="<%=dir%>">
  <head>
    <script src="security/javascript/jquery-1.10.2.js"></script>
  
    <title><%= resourceBundle.getString("login.title") %></title>
    <link href="security/com/technology/jep/jepcommon/styles/Default.css" rel="stylesheet" type="text/css">

    <script language="javascript">
      //Приходится реализовывать отличным от JEP подходом, т.к. не удалось передать разумным/надежным методом язык/mtSID struts/JEP в приложение JavaSSO.

      //Код клавиши Enter (возвращается в event.keyCode при нажатии Enter).
      var VK_ENTER = 13;

      //Функция локализации формы аутентификации.
      function localize(){
        //Если фрейма Navigation нет, то выходим из функции - оставляем локализацию на основе ресурсных файлов JavaSSO.
        if(top.navigation == null) return;

        //Если мы дошли до сюда, значит фрейм Navigation существует - произведем локализацию на основе ресурсных файлов Navigation.
        document.getElementById('login.title').innerHTML = top.navigation.document.getElementsByName('login.title')[0].value;
        document.getElementById('login.registration').title = top.navigation.document.getElementsByName('login.registration')[0].value;
        document.getElementById('login.login').innerHTML = top.navigation.document.getElementsByName('login.login')[0].value;
        document.getElementById('login.password').innerHTML = top.navigation.document.getElementsByName('login.password')[0].value;

        document.getElementsByName('checkForm.mandatoryField')[0].value = top.navigation.document.getElementsByName('checkForm.mandatoryField')[0].value;
        document.getElementsByName('action.incorrectInputData')[0].value = top.navigation.document.getElementsByName('action.incorrectInputData')[0].value;
        <% if(loginAttempts > 0){ %>
          document.getElementById('login.error').innerHTML = top.navigation.document.getElementsByName('login.error')[0].value;
        <% } %>
      }
      
      //Функция проверяет былали нажата клавиша Enter.
      function isEnter(e){
        if(e.keyCode == VK_ENTER) return true;

        return false;
      }

      //Функция очистки указанных элиментов документа.
      function clearErrors(){
        var ArgumentsCount = clearErrors.arguments.length;

        for (var i = 0; i < ArgumentsCount; i++)
        document.getElementById(clearErrors.arguments[i]).innerHTML = '';
      }

      //Функция проверки обязательного символьного поля.
      function checkMandatoryStringField(FieldName, ErrorObjectID, ErrorMessage){
        var Value = document.getElementsByName(FieldName)[0].value;
      
        if(Value.replace(/ /g,'').length == 0){
        if(checkMandatoryStringField.arguments.length >= 3)document.getElementById(ErrorObjectID).innerHTML = ErrorMessage;
        else document.getElementById(ErrorObjectID).innerHTML = document.getElementsByName('checkForm.mandatoryField')[0].value;
        return false;
        } 
        else return true;
      }

      //Функция проверки данных формы аутентификации.
      function validateAuthorizationForm(){
        var errorsCount = 0;//Количесво ошибок.
        //Предварительно очисти все сообщения об ошибках.
        clearErrors(
        'loginError'                
        ,'passwordError'
        );                
        //Проверим поля на корректность введенной информации.        
        if(!checkMandatoryStringField('j_username', 'loginError')) errorsCount++;        
        if(!checkMandatoryStringField('j_password', 'passwordError')) errorsCount++;        
        //Проверим - появились ли ошибки.
        if(errorsCount == 0)return true;
        else return false;
      }

      //Произвести авторизацию.
      function authorization(pForm){
        var lForm;//Указатель на форму.
        
        //Если конкретно указана форма, то работаем с ней.
        if(authorization.arguments.length >= 1)lForm = pForm; 
        //Иначе - считаем, что форма в документе одна.
        else lForm = document.forms[0];

        //Если данные формы корректны, то отправим изменения на сервер.
        if(validateAuthorizationForm()) {
			lForm.submit();
        } //Если нет - сообщим об этом пользователю.
        else trace(document.getElementsByName('action.incorrectInputData')[0].value);
      }

    </script>
  </head>

  <body>
    <form action="j_security_check" id="loginForm" name="loginForm" autocomplete="Off" method="post">

      <table style=" width: 100%; ">
        <tr>
          <th id="login.title"><%= resourceBundle.getString("login.title") %></th>
        </tr>
      </table>

      <table style=" width: 100%; ">
        <tr style=" height: 30px ">
          <th style=" width: 5px ">&nbsp;
          <th style=" width: 25px "><a style="cursor: pointer;" onclick="authorization()"><img id="login.registration" src="security/com/technology/jep/jepcommon/images/authorization.gif" title='<%= resourceBundle.getString("login.registration") %>' /></a>
          <th style=" width: 100%; ">&nbsp;
        <tr><td colspan="3"><% if(loginAttempts > 0){ %>

          <table style=" width: 100%; " class="errors">
            <tr>
              <td style=" width: 22px; "><img src="com/technology/jep/jepcommon/images/warning.gif"></td>
              <td id="login.error"><%= resourceBundle.getString("login.error") %></td>
            </tr>
          </table>

        <% } %>		
      </table>
	  
      <br/><br/><br/><br/><br/>
	  
      <table style=" width: 100%; ">
        <colgroup>
          <col style=" width: 50%; ">
          <col style=" width: 235px; ">          
          <col style=" width: 50%; ">
        </colgroup>
        <tr>
          <td><br></td>
          <td><br></td>
          <td><br></td>
        </tr>        

        <tr>
          <td style=" text-align: right; " id="login.login"><%= resourceBundle.getString("login.login") %></td>
          <td style=" text-align: left; ">&nbsp;<input type="text" id="j_username" name="j_username" maxlength="50" style=" width: 220px " /></td>
          <td style=" text-align: left; " class="error" id="loginError"></td>
        </tr>

        <tr>
          <td style=" text-align: right; " id="login.password"><%= resourceBundle.getString("login.password") %></td>
          <td style=" text-align: left; ">&nbsp;<input type="password" id="j_password" name="j_password" maxlength="50" style=" width: 220px " /></td>
          <td style=" text-align: left; " class="error" id="passwordError"></td>
        </tr>

      </table>

      <input type="hidden" name="checkForm.mandatoryField" value='<%= resourceBundle.getString("checkForm.mandatoryField") %>'/>
      <input type="hidden" name="action.incorrectInputData" value='<%= resourceBundle.getString("action.incorrectInputData") %>'/>
      <input type="submit" style="position: absolute; left: -9999px; width: 1px; height: 1px;"/>
    </form>
    <script language="javascript">
	<!--
		document.loginForm.j_username.focus(); 
		localize(); 
	//-->
    </script>
  </body>
</html>
