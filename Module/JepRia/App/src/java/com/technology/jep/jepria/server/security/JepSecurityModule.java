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

	String logout(HttpServletRequest request, HttpServletResponse response) throws Exception;

	boolean isRole(String role, boolean makeError) throws Exception;
	
	Integer getJepPrincipalOperatorId(Principal principal);	
}
