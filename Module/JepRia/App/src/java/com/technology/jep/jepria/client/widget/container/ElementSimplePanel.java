package com.technology.jep.jepria.client.widget.container;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Класс панели, состоящей из единичного DOM-элемента.
 */
public class ElementSimplePanel extends SimplePanel {
	
	/**
	 * Создает панель с единичным элементом.
	 * 
	 * @param el		встраиваемый DOM-элемент
	 */
	public ElementSimplePanel(Element el){
		super(el);
	}
}
