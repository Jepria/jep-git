package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.supplierText;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SUPPLIER_ID;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SUPPLIER_NAME;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.wizard.BlockViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;

public class SupplierFirstBlockViewImpl extends BlockViewImpl {
	
	public SupplierFirstBlockViewImpl(){		
		VerticalPanel innerPanel = new VerticalPanel();
		
		JepIntegerField supplierIdNumberField = new JepIntegerField(supplierText.supplier_detail_supplier_id());
		JepTextField supplierNameTextField = new JepTextField(supplierText.supplier_detail_supplier_name());
		supplierNameTextField.setMaxLength(255);
		
		innerPanel.add(supplierIdNumberField);
		innerPanel.add(supplierNameTextField);
		// set widget
		setWidget(innerPanel);
		// set text
		asWidget().setCaptionText(supplierText.supplier_idNameFieldSet_title());
		
		fields.put(SUPPLIER_ID, supplierIdNumberField);
		fields.put(SUPPLIER_NAME, supplierNameTextField);
	}
}
