package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUTTON_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TOOLBAR_PRESENTER_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TOOLBAR_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TOOLBAR_VIEW_ATTRIBUTE;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.technology.jep.jepriatoolkit.creator.module.adapter.ReverseBooleanAdapter;

//Указание атрибутов тэга происходит в обратном порядке, вложенных элементов/тэгов - в прямом.
@XmlType(propOrder = {"buttons", "view", "presenter"})
@XmlRootElement(name=TOOLBAR_TAG_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class ToolBar {

	@XmlAttribute(name=TOOLBAR_PRESENTER_ATTRIBUTE)
	@XmlJavaTypeAdapter(ReverseBooleanAdapter.class)
	private Boolean presenter;
	
	@XmlAttribute(name=TOOLBAR_VIEW_ATTRIBUTE)
	@XmlJavaTypeAdapter(ReverseBooleanAdapter.class)
	private Boolean view;
	
	@XmlElement(name = BUTTON_TAG_NAME)
	private List<ModuleButton> buttons = null;

	public boolean isPresenter() {
		return presenter;
	}

	public void setPresenter(boolean presenter) {
		this.presenter = presenter;
	}

	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	public List<ModuleButton> getButtons() {
		return buttons;
	}

	public void setButtons(List<ModuleButton> buttons) {
		this.buttons = buttons;
	}
}
