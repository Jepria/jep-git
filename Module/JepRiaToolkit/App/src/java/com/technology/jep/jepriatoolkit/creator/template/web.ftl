<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<web-app>
  <servlet>
    <servlet-name>remoteLoggerServiceImpl</servlet-name>
    <servlet-class>com.allen_sauer.gwt.log.server.RemoteLoggerServiceImpl</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>remoteLoggerServiceImpl</servlet-name>
    <url-pattern>/${moduleName}/gwt-log</url-pattern>
  </servlet-mapping>
  <servlet>
    <servlet-name>MainServiceServlet</servlet-name>
    <servlet-class>com.technology.jep.jepria.server.service.JepMainServiceServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>MainServiceServlet</servlet-name>
    <url-pattern>/${moduleName}/MainService</url-pattern>
  </servlet-mapping>
  <#list forms as form>
  <servlet>
    <servlet-name>${form.formName}Servlet</servlet-name>
    <servlet-class>${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.service.${form.formName}ServiceImpl</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>${form.formName}Servlet</servlet-name>
    <url-pattern>/${moduleName}/${form.formName}Service</url-pattern>
  </servlet-mapping>
  <#if form.hasLobFields>
  <servlet>
    <servlet-name>uploadServlet</servlet-name>
    <servlet-class>${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.service.UploadServiceImpl</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>uploadServlet</servlet-name>
    <url-pattern>/${moduleName}/upload</url-pattern>
  </servlet-mapping>
  <servlet>
    <servlet-name>downloadServlet</servlet-name>
    <servlet-class>${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.service.DownloadServiceImpl</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>downloadServlet</servlet-name>
    <url-pattern>/${moduleName}/download</url-pattern>
  </servlet-mapping>
  </#if>
  <#if form.isExcelAvailable>
  <servlet>
    <servlet-name>ShowExcelServlet</servlet-name>
    <servlet-class>${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.service.ShowExcelServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>ShowExcelServlet</servlet-name>
    <url-pattern>/${moduleName}/showExcel</url-pattern>
  </servlet-mapping>
  </#if>
  </#list>
  <welcome-file-list>
    <welcome-file>${moduleName}.jsp</welcome-file>
  </welcome-file-list>
  <#list securityRoles as roleName>
  <security-role>
    <role-name>${roleName}</role-name>
  </security-role>
  </#list>
  <security-constraint>
    <web-resource-collection>
      <web-resource-name>The application entry point</web-resource-name>
      <url-pattern>/*</url-pattern>
    </web-resource-collection>
    <#if securityRoles?has_content>
    <auth-constraint>
    <#list securityRoles as roleName>
      <role-name>${roleName}</role-name>
    </#list> 
    </auth-constraint>
    </#if>
  </security-constraint>
  
  <login-config>
    <auth-method>FORM</auth-method>
    <form-login-config>
      <form-login-page>/WEB-INF/security/Login.jsp</form-login-page>
      <form-error-page>/WEB-INF/security/LoginError.jsp</form-error-page>
    </form-login-config>
  </login-config>
</web-app>
