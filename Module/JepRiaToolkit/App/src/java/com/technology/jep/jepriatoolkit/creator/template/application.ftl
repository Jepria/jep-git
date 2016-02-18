<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<application>
  <display-name>${moduleName}</display-name>
  <module>
    <web>
      <web-uri>${moduleName?lower_case}.war</web-uri>
      <context-root>${moduleName}</context-root>
    </web>
  </module>
  <module>
    <ejb>${moduleName?lower_case}-ejb.jar</ejb>
  </module>
  <module>
    <ejb>jepria-ejb.jar</ejb>
  </module>
</application>
