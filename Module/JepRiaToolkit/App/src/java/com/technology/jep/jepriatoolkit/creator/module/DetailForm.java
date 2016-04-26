package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DETAIL_FORM_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FIELD_LABEL_WIDTH_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PRESENTER_BOBY_TAG_NAME;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//Указание атрибутов тэга происходит в обратном порядке, вложенных элементов/тэгов - в прямом.
@XmlType(propOrder = {"presenterBody", "labelWidth"})
@XmlRootElement(name = DETAIL_FORM_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailForm extends ModuleForm {

	@XmlAttribute(name=FIELD_LABEL_WIDTH_ATTRIBUTE)
	private String labelWidth;
	
	@XmlElement(name=PRESENTER_BOBY_TAG_NAME)
	private String presenterBody;
	
	@SuppressWarnings("unused")
	private DetailForm(){}
	
	public DetailForm(List<ModuleField> listFormFields) {
		super(listFormFields);
	}

	public String getLabelWidth() {
		return labelWidth;
	}

	public void setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
	}

	public String getPresenterBody() {
		return presenterBody;
	}
	
	public void setPresenterBody(String presenterBody) {
		this.presenterBody = presenterBody;
	}
}
