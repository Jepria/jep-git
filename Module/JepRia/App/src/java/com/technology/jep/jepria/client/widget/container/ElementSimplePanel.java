package com.technology.jep.jepria.client.widget.container;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Класс панели, состоящей из единичного DOM-элемента.<br/>
 * Необходимость в данном классе возникает в случае, когда элемент в DOM-дереве следует ассоциировать
 * с GWT-виджетом. Например, для случаев, когда метод принимает в качестве параметра ссылку на виджет, а в 
 * распоряжении имеется лишь DOM-элемент (см. {@link com.technology.jep.jepria.client.widget.field.tree.TreeField} или {@link com.technology.jep.jepria.client.widget.list.JepGrid})
 */
public class ElementSimplePanel extends SimplePanel {
  
  /**
   * Создает панель с единичным элементом.
   * 
   * @param el    встраиваемый DOM-элемент
   */
  public ElementSimplePanel(Element el){
    super(el);
  }
}
