package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DETAIL_FORM_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FORMS_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LIST_FORM_TAG_NAME;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//Указание атрибутов тэга происходит в обратном порядке, вложенных элементов/тэгов - в прямом.
@XmlType(propOrder = {"detailForm", "listForm"})
@XmlRootElement(name = FORMS_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class Forms {

	@XmlElement(name = DETAIL_FORM_TAG_NAME)
	private DetailForm detailForm;
	@XmlElement(name = LIST_FORM_TAG_NAME)
	private ListForm listForm;
	
	public Forms(){}
	
	public Forms(DetailForm detailForm, ListForm listForm) {
		setDetailForm(detailForm);
		setListForm(listForm);
	}
	public DetailForm getDetailForm() {
		return detailForm;
	}
	public void setDetailForm(DetailForm detailForm) {
		this.detailForm = detailForm;
	}
	public ListForm getListForm() {
		return listForm;
	}
	public void setListForm(ListForm listForm) {
		this.listForm = listForm;
	}
}
