package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name=MODULES_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class Modules {

	@XmlElement(name=MODULE_TAG_NAME)
	private List<Module> modules = null;
	
	@SuppressWarnings("unused")
	private Modules(){}
	
	public Modules(List<Module> modules) {
		this.modules = modules;
	}
	
	public Modules(Module... modules) {
		this.modules = Arrays.asList(modules);
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
}
