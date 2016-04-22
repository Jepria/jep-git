package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULES_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NAME_ATTRIBUTE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

// Указание атрибутов тэга происходит в обратном порядке, вложенных элементов/тэгов - в прямом.
@XmlType(propOrder = {"modules", "projectPackage", "defaultDatasource", "name"})
@XmlRootElement(name = APPLICATION_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class Application {

	@XmlAttribute(required=true, name = NAME_ATTRIBUTE)
	private String name;

	@XmlAttribute
	private String defaultDatasource;
	
	@XmlAttribute(required=true)
	private String projectPackage;
	
	@XmlElement(name = MODULES_TAG_NAME)
	private Modules modules;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefaultDatasource() {
		return defaultDatasource;
	}
	public void setDefaultDatasource(String defaultDatasource) {
		this.defaultDatasource = defaultDatasource;
	}
	public String getProjectPackage() {
		return projectPackage;
	}
	public void setProjectPackage(String projectPackage) {
		this.projectPackage = projectPackage;
	}
	public Modules getModules() {
		return modules;
	}
	public void setModules(Modules modules) {
		this.modules = modules;
	}
}
