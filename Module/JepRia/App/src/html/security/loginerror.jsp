<%@ page contentType="text/html;charset=utf-8"%>
<jsp:directive.page import="oracle.security.jazn.sso.app.*" />
<jsp:directive.page import="oracle.security.jazn.util.Env" />
<jsp:directive.page import="oracle.security.jazn.resources.FrameworkResourceBundle" />
<jsp:directive.page import="oracle.security.jazn.util.Resources" />
<jsp:directive.page import="java.util.ResourceBundle" />

<jsp:directive.page import="com.technology.jep.jepcommon.JepConstant" />

<jsp:declaration>
 private static final String MAX_LOGIN_ATTEMPTS = "max-login-attempts";
 private int _maxLoginAttempts = 3;
 private int _sessionTimeout;
 private SSOServletConfig _config;
  
 public void jspInit()
 {
    _config = new SSOServletConfig(application);
    if (null != _config)
    {
       Integer var = (Integer)_config.getProperty(Env.JSSO_PROPERTY_LOGIN_MAX_ATTEMPTS);
       _maxLoginAttempts = var.intValue();

       var = (Integer)_config.getProperty(Env.JSSO_PROPERTY_SESSION_TIMEOUT);
       _sessionTimeout = var.intValue();
    }
 }
</jsp:declaration>
<jsp:scriptlet>
	FrameworkResourceBundle _bundle = FrameworkResourceBundle.getResourceBundle(request.getLocales());
	ResourceBundle resourceBundle = ResourceBundle.getBundle(JepConstant.JEP_RESOURCE_BUNDLE_NAME, request.getLocale());

	Integer attempts = (Integer) session.getAttribute(MAX_LOGIN_ATTEMPTS);
	if (null == attempts){
		attempts = new Integer(0);
	}
	synchronized (session){
		attempts = new Integer(attempts.intValue() + 1);
		session.setAttribute(MAX_LOGIN_ATTEMPTS, attempts);
	}
	int a = attempts.intValue();
	if (a < _maxLoginAttempts){
		RequestDispatcher rd = application.getRequestDispatcher("/WEB-INF/login.jsp");
		rd.forward(request, response);
	}
</jsp:scriptlet>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="<%=_bundle.getResourceLocale().getLanguage()%>">
  <head>
    <title><%= resourceBundle.getString("loginError.title") %></title>
    <link href="resources/com/technology/jep/jepcommon/styles/Default.css" rel="stylesheet" type="text/css">
    <script language="javascript">
      //Приходится реализовывать отличным от JEP подходом, т.к. не удалось передать разумным/надежным методом язык/mtSID struts/JEP в приложение JavaSSO.

      //Функция локализации формы сообщения об ошибке аутентификации.
      function localize(){

        //Если фрейма Navigation нет, то выходим из функции - оставляем локализацию на основе ресурсных файлов JavaSSO.
        if(top.navigation == null) return;

        //Если мы дошли до сюда, значит фрейм Navigation существует - произведем локализацию на основе ресурсных файлов Navigation.
        document.getElementById('loginError.title').innerText = top.navigation.document.getElementsByName('loginError.title')[0].value;
        document.getElementById('loginError.attemptsError').innerText = top.navigation.document.getElementsByName('loginError.attemptsError')[0].value;
      }
      
    </script>
  </head>
  <body>

	<table style=" width: 100%; ">
		<tr><th id="loginError.title"><%= resourceBundle.getString("loginError.title") %></th></tr>
		<tr>
			<td>

				<table style=" width: 100%; " class="errors">
					<tr>
						<td style=" width: 22px "><img src="resources/com/technology/jep/jepcommon/images/warning.gif"></td>
						<td id="loginError.attemptsError"><%= resourceBundle.getString("loginError.attemptsError") %></td>
					</tr>
				</table>
			</td>
		</tr>
    </table>

    <script language="javascript">
	<!--
		localize(); 
	//-->
    </script>
  </body>
</html>
