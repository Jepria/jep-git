package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_TAG_NAME;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ModuleForm {

	protected ModuleForm(){}
	
	public ModuleForm(List<ModuleField> fields){
		setFields(fields);
	}
	
	@XmlElement(name = FIELD_TAG_NAME)
	private List<ModuleField> fields = null;
	
	public List<ModuleField> getFields() {
		return fields;
	}

	public void setFields(List<ModuleField> fields) {
		this.fields = fields;
	}
}
