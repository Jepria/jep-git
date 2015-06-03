package com.technology.jep.jepria.client.security;

import java.util.ArrayList;
import java.util.List;

/**
 * Клиентский аналог серверного {@link com.technology.jep.jepria.server.security.JepSecurityModule}.
 */
public class ClientSecurity {
	
	public static final int CHECK_ROLES_BY_OR = 0;
	public static final int CHECK_ROLES_BY_AND = 1;
	
	public static ClientSecurity instance = new ClientSecurity();
	
	private ClientSecurity() {};

	private String username = null;

	private Integer operatorId = null;

	private List<String> roles = null;
	
	public String getUsername() {
		return username;
	}
		
	public Integer getOperatorId() {
		return operatorId;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setOperatorId(Integer operatorId) {
		if(this.operatorId == null) {	// Делается только один раз.
			this.operatorId = operatorId;
		}
	}

	public void setRoles(List<String> roles) {
		if(this.roles == null) {	// Делается только один раз.
			this.roles = roles;
		}
	}
	
	public boolean isUserHaveRole(String role) {
		return roles != null && roles.contains(role);
	}

	/**
	 * Проверка наличия у пользователя хотя бы одной из заданных ролей.
	 * 
	 * @param checkedRoles роли, наличие которых проверяется
	 * @return true, если есть хотя бы одна роль, иначе - false
	 */
	public static boolean isUserHaveRoles(List<String> checkedRoles) {
		List<String> roles = ClientSecurity.instance.getRoles();
		if(roles != null) {
			for(String role: checkedRoles) {
				if(roles.contains(role)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isUserHaveRoles(String strRoles) {
		return isUserHaveRoles(getRoles(strRoles));
	}

	/**
	 * Проверка наличия у пользователя всех заданных ролей.
	 * 
	 * @param checkedRoles роли, наличие которых проверяется
	 * @return true, если есть все заданные роли, иначе - false
	 */
	public static boolean isUserHaveAllRoles(List<String> checkedRoles) {
		return ClientSecurity.instance.getRoles().containsAll(checkedRoles);
	}

	public static List<String> getRoles(String strRoles) {
		String[] rolesArray = strRoles.split(",");
		List<String> roles = new ArrayList<String>();
		for(int i = 0; i < rolesArray.length; i++) {
			roles.add(rolesArray[i].trim());
		}
		return roles;
	}

}
