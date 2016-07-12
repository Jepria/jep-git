 <%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
--%>
<!DOCTYPE html>

<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:directive.page import="oracle.security.jazn.sso.app.*" />
<jsp:directive.page import="oracle.security.jazn.util.Env" />
<jsp:directive.page import="oracle.security.jazn.resources.FrameworkResourceBundle" />
<jsp:directive.page import="oracle.security.jazn.util.Resources" />

<jsp:directive.page import="java.util.ResourceBundle" />
<jsp:directive.page import="javax.servlet.ServletContext" />

<jsp:directive.page import="com.technology.jep.jepcommon.JepConstant" />

<jsp:declaration>
 private static final String MAX_LOGIN_ATTEMPTS = "max-login-attempts";
 private int _maxLoginAttempts = 3;
 private int _sessionTimeout;
 /*
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
 */
</jsp:declaration>
<jsp:scriptlet>
  FrameworkResourceBundle _bundle = FrameworkResourceBundle.getResourceBundle(request.getLocales());
  ResourceBundle resourceBundle = ResourceBundle.getBundle(JepConstant.JEP_RESOURCE_BUNDLE_NAME, request.getLocale());
/*
  Integer attempts = (Integer)session.getAttribute(MAX_LOGIN_ATTEMPTS);
  int loginAttempts = 0;
  if (null != attempts){
    loginAttempts = attempts.intValue();
    if(loginAttempts >= _maxLoginAttempts){
      RequestDispatcher rd = application.getRequestDispatcher("/WEB-INF/loginerror.jsp");
      rd.forward(request, response);   
    }
  }
  session.setMaxInactiveInterval(_sessionTimeout);
*/  
  String dir = _bundle.isLocaleRTL() ? "rtl" : "ltr"; 
</jsp:scriptlet>

<html lang="<%=_bundle.getResourceLocale().getLanguage()%>"    dir="<%=dir%>">
<head>
  <meta charset="UTF-8" />
  
  <title><%= resourceBundle.getString("login.title") %></title>
  <link href="resources/com/technology/jep/jepcommon/styles/Default.css" rel="stylesheet" type="text/css">
  
    <script language="javascript">
  
      //Функция очистки указанных элиментов документа.
      function clearErrors(){
/*    
        var ArgumentsCount = clearErrors.arguments.length;

        for (var i = 0; i < ArgumentsCount; i++)
        document.getElementById(clearErrors.arguments[i]).innerHTML = '';
*/    
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
        if(!checkMandatoryStringField('username', 'loginError')) errorsCount++;        
        if(!checkMandatoryStringField('password', 'passwordError')) errorsCount++;        
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
      lForm.btnSubmit.click();
        } //Если нет - сообщим об этом пользователю.
        else trace(document.getElementsByName('action.incorrectInputData')[0].value);
      }

    </script>
  
</head>

<body id="cas">

  <form:form method="post" id="fm1" commandName="${commandName}" htmlEscape="true">

      <table style=" width: 100%; ">
        <tr>
          <th id="login.title"><%= resourceBundle.getString("login.title") %></th>
        </tr>
      </table>

      <table style=" width: 100%; ">
        <tr style=" height: 30px ">
          <th style=" width: 5px ">&nbsp;
          <th style=" width: 25px "><a style="cursor: pointer;" onclick="authorization()"><img id="login.registration" src="resources/com/technology/jep/jepcommon/images/authorization.gif" title='<%= resourceBundle.getString("login.registration") %>' /></a>
          <th style=" width: 100%; ">&nbsp;
        <tr><td colspan="3">  
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
          <td style=" text-align: left; ">
        <c:choose>
        <c:when test="${not empty sessionScope.openIdLocalId}">
          <strong>${sessionScope.openIdLocalId}</strong>
          <input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
        </c:when>
        <c:otherwise>
          <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
          <form:input cssClass="required" cssErrorClass="error" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" />
        </c:otherwise>
        </c:choose>

        <%--
        NOTE: Certain browsers will offer the option of caching passwords for a user.  There is a non-standard attribute,
        "autocomplete" that when set to "off" will tell certain browsers not to prompt to cache credentials.  For more
        information, see the following web page:
        http://www.technofundo.com/tech/web/ie_autocomplete.html
        --%>
      </td>
      
      
      
          <td style=" text-align: left; " class="error" id="loginError"></td>
        </tr>

        <tr>
          <td style=" text-align: right; " id="login.password"><%= resourceBundle.getString("login.password") %></td>
    <td style=" text-align: left; ">
      <form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
    </td>
          <td style=" text-align: left; " class="error" id="passwordError"></td>
        </tr>
        <tr>
          <td style=" text-align: right; "></td>
    <td style=" text-align: left; ">
    <section>
      <input type="hidden" name="lt" value="${loginTicket}" />
      <input type="hidden" name="execution" value="${flowExecutionKey}" />
      <input type="hidden" name="_eventId" value="submit" />
      <input type="hidden" name="url" value="<%=System.getenv("OAS_INSTANCE_ADDRESS")%>/Navigation/welcomeInput.do" /> <!-- TODO Разобраться, почему без этого в хроме бесконечный редирект  -->

      <input name="btnSubmit" tabindex="3" type="submit" />
    </section>
    </td>
          <td style=" text-align: right; "></td>
        </tr>

      </table>

    <form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" />

    
      <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
    
  </form:form>
  </body>
</html>

