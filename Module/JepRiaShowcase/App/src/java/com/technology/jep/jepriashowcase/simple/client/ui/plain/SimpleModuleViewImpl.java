package com.technology.jep.jepriashowcase.simple.client.ui.plain;

import static com.technology.jep.jepriashowcase.simple.client.SimpleClientConstant.simpleText;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.technology.jep.jepria.client.ui.plain.PlainModuleViewImpl;

public class SimpleModuleViewImpl extends PlainModuleViewImpl implements SimpleModuleView {
	
	private Button supplierButton;
	private Button errorButton;
	private Button customModuleButton;
	
	public SimpleModuleViewImpl() {
		DecoratorPanel simpleMainView = new DecoratorPanel();
		
		FlexTable body = new FlexTable();
		
		HTML description = new HTML(simpleText.simple_description());
		body.setWidget(0, 0, description);
		
		supplierButton = new Button(simpleText.simple_operatorButton());
		body.setWidget(1, 0, supplierButton);

		errorButton = new Button(simpleText.simple_errorButton());
		body.setWidget(2, 0, errorButton);
		
		customModuleButton = new Button(simpleText.simple_customModuleButton());
		body.setWidget(3, 0, customModuleButton);

		simpleMainView.setWidget(body);

		setWidget(simpleMainView);
	}
	
	public void addSupplierButtonClickHandler(ClickHandler clickHandler) {
		supplierButton.addClickHandler(clickHandler);
	}

	public void addErrorButtonClickHandler(ClickHandler clickHandler) {
		errorButton.addClickHandler(clickHandler);
	}

	public void addCustomModuleClickHandler(ClickHandler clickHandler) {
		customModuleButton.addClickHandler(clickHandler);
	}
	
}
