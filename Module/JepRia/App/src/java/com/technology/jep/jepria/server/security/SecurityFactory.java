package com.technology.jep.jepria.server.security;

import javax.servlet.http.HttpServletRequest;

import com.technology.jep.jepria.server.security.cas.JepSecurityModule_Standard;
import com.technology.jep.jepria.server.security.oc4j.JepSecurityModule_OC4J;
import com.technology.jep.jepria.server.util.JepServerUtil;

public class SecurityFactory {
	
	static public JepSecurityModule getSecurityModule(HttpServletRequest request) {
		JepSecurityModule result = null;
		
		if(JepServerUtil.isJavaSSO(request)) {
			result = JepSecurityModule_OC4J.getInstance(request);
		} else {
			result = JepSecurityModule_Standard.getInstance(request);
		}
		
		return result;
	}
}
