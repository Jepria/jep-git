<?xml version="1.0" encoding="UTF-8"?>
<Context>
  <Realm className="org.apache.catalina.realm.JAASRealm" 
    appName="${defaultDatasource}"
    userClassNames="com.technology.jep.jepcommon.security.JepPrincipal"
    roleClassNames="com.technology.jep.jepcommon.security.JepPrincipal" />
</Context>
