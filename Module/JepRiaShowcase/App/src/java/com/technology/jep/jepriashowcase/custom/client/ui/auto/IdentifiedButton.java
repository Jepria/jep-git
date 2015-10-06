package com.technology.jep.jepriashowcase.custom.client.ui.auto;

import com.google.gwt.user.client.ui.Button;

/**
 * Кнопка с заданным id (поддержка automation)
 * TODO Переименовать в JepButton, а JepButton - в ToolbarButton 
 * TODO Переместить в JepRia 
 */
public class IdentifiedButton extends Button {

	public IdentifiedButton(String html, String id) {
		super(html);
		this.getElement().setId(id);
	}
}
