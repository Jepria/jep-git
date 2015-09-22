package com.technology.jep.jepriashowcase.supplier.client.ui.form.detail;
 
import com.google.gwt.event.dom.client.ClickHandler;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormView;
import com.technology.jep.jepria.client.ui.wizard.BlockContainer;
 
public interface SupplierDetailFormView extends DetailFormView {
	void initButtonProperties();
	void addButtonClickHandler(String buttonId, ClickHandler handler);
	BlockContainer getBlockContainer();
}
