package com.technology.jep.jepria.client.ui.form.detail;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.client.widget.field.multistate.JepMultiStateField;

/**
 * Стандартная детальная форма.<br/>
 * В отличие от {@link com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl}, при создании представления формы 
 * не требуется ручное добавление графических элементов (VerticalPanel, ScrollPanel) на форму и полей в FieldManager -- 
 * требуется только определение конфигураций полей.
 */
public abstract class StandardDetailFormViewImpl extends DetailFormViewImpl {

	/**
	 * VerticalPanel для возможности управления виджетами
	 * (например, выравнивание).
	 */
	protected VerticalPanel panel;
	
	public StandardDetailFormViewImpl() {
		this(new FieldManager());
	}
	
	public StandardDetailFormViewImpl(FieldManager fields) {
		super(fields);
		
		ScrollPanel scrollPanel = new ScrollPanel();
		setWidget(scrollPanel);
		
		scrollPanel.setSize("100%", "100%");
		
		panel = new VerticalPanel();
		panel.getElement().getStyle().setMarginTop(5, Unit.PX);
		scrollPanel.add(panel);
		
		LinkedHashMap<String, Widget> fieldConfigurations = getFieldConfigurations();
		if (fieldConfigurations == null) {
			fieldConfigurations = new LinkedHashMap<String, Widget>();
		}
 
		for (Map.Entry<String, Widget> entry: fieldConfigurations.entrySet()) {
			Widget field = entry.getValue();
			
			// Добавляем на форму виджеты _всех_ типов
			panel.add(field);
			
			// Добавляем в FieldManager _только_ виджеты типа JepMultiStateField
			if (field instanceof JepMultiStateField<?, ?>) {
				fields.put(entry.getKey(), (JepMultiStateField<?, ?>)field);
			}
		}
	}
	
	/**
	* Метод должен быть переопределен в наследниках и возвращать набор пар
	* {FIELD_ID, field} = {идентификатор поля, само поле}.
	* 
	* Если field типа JepMultiStateField, то это поле будет добавлено и на форму,
	* и в FieldManager с заданным FIELD_ID.
	* Если field другого типа (например, стандартный Widget из GWT), то это поле будет добавлено только на форму,
	* при этом FIELD_ID не используется и может быть любым.
	* Это нужно для поддержки возможности добавления на форму не только Jep-полей,
	* но и групп полей или стандартных gwt-виджетов.  
	*/
	protected abstract LinkedHashMap<String, Widget> getFieldConfigurations();
}
