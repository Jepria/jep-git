package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.technology.jep.jepriatoolkit.creator.module.adapter.BooleanAdapter;

public class ToolBar {

	@XmlAttribute(name=TOOLBAR_PRESENTER_ATTRIBUTE)
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	private Boolean presenter;
	
	@XmlAttribute(name=TOOLBAR_VIEW_ATTRIBUTE)
	@XmlJavaTypeAdapter(BooleanAdapter.class)
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
