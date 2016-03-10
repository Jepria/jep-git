<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<orion-application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" component-classification="external" deployment-version="10.1.3.1.0" schema-major-version="10" schema-minor-version="0" xsi:noNamespaceSchemaLocation="http://xmlns.oracle.com/oracleas/schema/orion-application-10_0.xsd">
  <web-module id="${moduleName?lower_case}" path="${moduleName?lower_case}.war"/>
  <library path="lib"/>
  <library path="${moduleName?lower_case}/WEB-INF/lib"/>
  <log>
    <file path="application.log"/>
  </log>
  <jazn provider="XML">
    <property name="role.mapping.dynamic" value="true"/>
    <property name="custom.loginmodule.provider" path="${moduleName?lower_case}/WEB-INF/lib" value="true"/>
    <jazn-web-app auth-method="CUSTOM_AUTH"/>
  </jazn>
  <jazn-loginconfig>
    <application>
      <name>${moduleName?lower_case}</name>
      <login-modules>
        <login-module>
          <class>com.technology.jep.jepcommon.security.JepLoginModule</class>
          <control-flag>required</control-flag>
        </login-module>
      </login-modules>
    </application>
  </jazn-loginconfig>
</orion-application>
