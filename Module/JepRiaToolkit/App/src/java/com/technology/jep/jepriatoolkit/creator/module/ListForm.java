package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DBL_CLICK_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LIST_FORM_DND_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LIST_FORM_GROUP_FIELD_NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LIST_FORM_PRESENTER_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LIST_FORM_TAG_NAME;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.technology.jep.jepriatoolkit.creator.module.adapter.BooleanAdapter;

//Указание атрибутов тэга происходит в обратном порядке, вложенных элементов/тэгов - в прямом.
@XmlType(propOrder = {"dnd", "presenter", "dblClick", "groupField"})
@XmlRootElement(name = LIST_FORM_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class ListForm extends ModuleForm {

	@XmlAttribute(name=LIST_FORM_GROUP_FIELD_NAME_ATTRIBUTE)
	private String groupField;
	
	@XmlAttribute(name=LIST_FORM_DND_NAME_ATTRIBUTE)
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	private Boolean dnd;
	
	@XmlAttribute(name=DBL_CLICK_NAME_ATTRIBUTE)
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	private Boolean dblClick;
	
	@XmlAttribute(name=LIST_FORM_PRESENTER_ATTRIBUTE)
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	private Boolean presenter;
	
	@SuppressWarnings("unused")
	private ListForm(){}
	
	public ListForm(List<ModuleField> listFormFields) {
		super(listFormFields);
	}

	public String getGroupField() {
		return groupField;
	}

	public void setGroupField(String groupField) {
		this.groupField = groupField;
	}

	public boolean isDnd() {
		return dnd;
	}

	public void setDnd(boolean dnd) {
		this.dnd = dnd;
	}

	public boolean isDblClick() {
		return dblClick;
	}

	public void setDblClick(boolean dblClick) {
		this.dblClick = dblClick;
	}

	public boolean isPresenter() {
		return presenter;
	}

	public void setPresenter(boolean presenter) {
		this.presenter = presenter;
	}
}
