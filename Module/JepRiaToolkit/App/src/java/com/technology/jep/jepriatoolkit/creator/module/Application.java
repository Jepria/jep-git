package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULES_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.*;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.technology.jep.jepriatoolkit.log.Logger;

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
	
	@XmlTransient
	private List<String> moduleIds;
	
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
	public void setModuleIds(List<String> modules){
		this.moduleIds = modules;
	}
	public void uptodate(Application newApplication){
		if (modules != null) {
			List<Module> originModules = modules.getModules();
			for (Module module : newApplication.modules.getModules()){
				boolean exists = false;
				String moduleId = module.getModuleId();
				for (Module originmodule : originModules){
					String originalModuleId = originmodule.getModuleId();
					exists = moduleId.equalsIgnoreCase(originalModuleId);
					if (exists){
						Logger.appendMessageToForm(moduleId, multipleConcat("Start analysis of module '", moduleId, "'"));
						originmodule.uptodate(module);
						Logger.appendMessageToTheEndOfForm(moduleId, multipleConcat("Stop analysis of module '", moduleId, "'"));
						break;
					}
				}
				if (!exists){
					Logger.appendMessageToForm(moduleId, multipleConcat("The module '", moduleId, "' was added to application structure!"));
					originModules.add(module);
				}
			}
			// check if all modules in xml has source code or maybe were deleted
			for (Module originmodule : originModules){
				String originModuleId = originmodule.getModuleId();
				if (!newApplication.moduleIds.contains(originModuleId)){
					Logger.appendMessageToForm(originModuleId, multipleConcat("Pay attention that module '", originModuleId, "' has no source code! If you need, you can remove this module description manually!"));
				}
			}
		}
	}
}
