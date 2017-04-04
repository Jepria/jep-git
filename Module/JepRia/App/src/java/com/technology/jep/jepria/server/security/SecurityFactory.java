package com.technology.jep.jepria.server.security;

import javax.servlet.http.HttpServletRequest;

import com.technology.jep.jepria.server.security.tomcat.JepSecurityModule_Tomcat;
import com.technology.jep.jepria.server.util.JepServerUtil;

public class SecurityFactory {
  
  static public JepSecurityModule getSecurityModule(HttpServletRequest request) {
    JepSecurityModule result = null;
    
    if(JepServerUtil.isTomcat(request)) {
      result = JepSecurityModule_Tomcat.getInstance(request);
    }
    
    return result;
  }
}
