<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="${form.formName}AutoTestSuite" parallel="false">

  <#noparse>
  <parameter name="baseUrl" value="${baseUrl}"/>
  <parameter name="browserName" value="${browserName}"/>
  <parameter name="browserPath" value="${browserPath}"/>
  <parameter name="driverPath" value="${driverPath}"/>
  <parameter name="username" value="${testUsername}"/>
  <parameter name="password" value="${testPassword}"/>
  <parameter name="jepriaVersion" value="${jepriaVersion}"/>
  <parameter name="forceNewBrowser" value="No"/>
  <parameter name="forceLogin" value="No"/>
  <parameter name="dbUrl" value="${testDbUrl}"/>
  <parameter name="dbUser" value="${testDbUser}"/>
  <parameter name="dbPassword" value="${testDbPassword}"/>
  </#noparse>
 
  <test name="${form.formName}AutoTest">
    <classes>
      <class name="${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.auto.${form.formName}AutoTest" >  
        <methods>
          <include name="test" />
        </methods>
      </class>
    </classes>
  </test> <!-- Test -->
</suite> <!-- Suite -->
