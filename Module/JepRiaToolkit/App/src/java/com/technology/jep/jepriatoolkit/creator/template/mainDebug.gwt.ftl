<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<module rename-to="${moduleName}">
  <inherits name="com.google.gwt.activity.Activity"/>
  <inherits name="com.google.gwt.place.Place"/>
  <inherits name="com.google.gwt.http.HTTP"/>
  <inherits name="com.technology.jep.jepria.JepRia"/>
  <inherits name="com.sensei.themes.mytheme.Mytheme"/>
  <#list forms as form>
  <inherits name="${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.${form.formName}" />
  </#list>
  <entry-point class="${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.entrance.${moduleName}EntryPoint" />
  
  <set-property name="user.agent" value="safari,gecko1_8"/>
  
  <!-- Нужно исключительно только для сервера Jetty используемого при автоматизированном Gwt-тестировани. -->
  <!-- Jetty берет определение сервлетов и путей именно здесь, а не из web.xml как обычный сервер. -->
  <servlet class="com.technology.jep.jepria.server.service.JepMainServiceServletTest" path="/MainService"/>
  <source path="client"/>
  <source path="shared"/>
  
  <inherits name="com.allen_sauer.gwt.log.gwt-log-RemoteLogger"/>
  <extend-property name="log_level" values="DEBUG"/>
  <set-property name="log_DivLogger" value="ENABLED"/>
  <set-property name="log_RemoteLogger" value="ENABLED"/>
</module>
