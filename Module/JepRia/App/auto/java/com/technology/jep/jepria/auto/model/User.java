package com.technology.jep.jepria.auto.model;

import java.util.List;

public class User {
	private String userName;
	private List<String> roles;

	public String getUsername() {
		return userName;
	}

	public User setUsername(String username) {
		this.userName = username;
		return this;
	}

	public List<String> getRoles() {
		return roles;
	}

	public User setRoles(List<String> roles) {
		this.roles = roles;
		return this;
	}
}
