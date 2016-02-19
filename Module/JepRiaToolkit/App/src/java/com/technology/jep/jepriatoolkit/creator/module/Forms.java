package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = FORMS_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class Forms {

	@XmlAttribute(name = DETAIL_FORM_TAG_NAME)
	private DetailForm detailForm;
	@XmlAttribute(name = LIST_FORM_TAG_NAME)
	private ListForm listForm;
	
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
