package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = DETAIL_FORM_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailForm {

	@XmlAttribute(name=FIELD_LABEL_WIDTH_ATTRIBUTE)
	private String labelWidth;
	
	@XmlElement(name=PRESENTER_BOBY_TAG_NAME)
	private String presenterBody;
	
	@XmlElement(name = FIELD_TAG_NAME)
	private List<ModuleField> fields = null;

	public String getLabelWidth() {
		return labelWidth;
	}

	public void setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
	}

	public List<ModuleField> getFields() {
		return fields;
	}

	public void setFields(List<ModuleField> fields) {
		this.fields = fields;
	}

	public String getPresenterBody() {
		return presenterBody;
	}
	
	public void setPresenterBody(String presenterBody) {
		this.presenterBody = presenterBody;
	}
}
