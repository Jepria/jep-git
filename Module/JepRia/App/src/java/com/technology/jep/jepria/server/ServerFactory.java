package com.technology.jep.jepria.server;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.server.security.oc4j.JepSecurityModule_OC4J;
import com.technology.jep.jepria.server.security.weblogic.JepSecurityModule_WL;
import com.technology.jep.jepria.server.util.JepServerUtil;

public class ServerFactory {
	protected static Logger logger = Logger.getLogger(ServerFactory.class.getName());	

	static public JepSecurityModule getSecurityModule(HttpServletRequest request) {
		JepSecurityModule result = null;
		
		if(JepServerUtil.isOC4JEnvironment(request.getSession().getServletContext())) {
			result = JepSecurityModule_OC4J.getInstance(request);
		} else {
			result = JepSecurityModule_WL.getInstance(request);
		}
		return result;
	}
}
