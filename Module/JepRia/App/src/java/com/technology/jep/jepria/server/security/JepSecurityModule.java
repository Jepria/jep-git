package com.technology.jep.jepria.server.security;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionBindingListener;

public interface JepSecurityModule extends HttpSessionBindingListener {
	Integer getOperatorId();

	List<String> getRoles();

	String getUsername();

	void logout(HttpServletRequest request, HttpServletResponse response);

	boolean isRole(String role, boolean makeError) throws Exception;
	
	Integer getJepPrincipalOperatorId(Principal principal);	
}
