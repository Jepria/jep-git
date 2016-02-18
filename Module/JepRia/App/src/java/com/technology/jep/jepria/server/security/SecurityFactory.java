package com.technology.jep.jepria.server.security;

import javax.servlet.http.HttpServletRequest;

import com.google.gwt.user.server.rpc.UnexpectedException;
import com.technology.jep.jepria.server.security.cas.JepSecurityModule_CAS;
import com.technology.jep.jepria.server.security.oc4j.JepSecurityModule_OC4J;
import com.technology.jep.jepria.server.util.JepServerUtil;

public class SecurityFactory {
	
	static public JepSecurityModule getSecurityModule(HttpServletRequest request) {
		JepSecurityModule result = null;
		
		if(JepServerUtil.isJavaSSO(request)) {
			result = JepSecurityModule_OC4J.getInstance(request);
		} else if(JepServerUtil.isCASEnvironment(request)) {
			result = JepSecurityModule_CAS.getInstance(request);
		} else {
			throw new UnexpectedException("Unknown SSO Type", null);
		}
		
		return result;
	}
}
