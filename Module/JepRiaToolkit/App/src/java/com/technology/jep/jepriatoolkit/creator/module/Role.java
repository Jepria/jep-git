package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_ROLES_ATTRIBUTE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name=MODULE_ROLES_ATTRIBUTE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Role {
	
	private String name;
	
	@SuppressWarnings("unused")
	private Role(){}
	
	public Role(String roleName){
		setName(roleName);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
