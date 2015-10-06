package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepria.shared.field.JepFieldNames.MAX_ROW_COUNT;
import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.supplierText;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.EXCLUSIVE_SUPPLIER_FLAG;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.EXCLUSIVE_SUPPLIER_OPTION;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SUPPLIER_DESCRIPTION;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.wizard.BlockPositionEnum;
import com.technology.jep.jepria.client.ui.wizard.BlockViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepCheckBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextAreaField;

public class SupplierThirdBlockViewImpl extends BlockViewImpl {

	public SupplierThirdBlockViewImpl(){
		
		VerticalPanel innerPanel = new VerticalPanel();
		
		JepCheckBoxField exclusiveSupplierFlagCheckBoxField = new JepCheckBoxField(supplierText.supplier_detail_exclusive_supplier_flag());
		JepComboBoxField exclusiveSupplierOptionComboBoxField = new JepComboBoxField(supplierText.supplier_detail_exclusive_supplier_flag());
		JepTextAreaField supplierDescriptionTextAreaField = new JepTextAreaField(supplierText.supplier_detail_supplier_description());
		supplierDescriptionTextAreaField.getEditableCard().setHeight(80 + Unit.PX.getType());
		JepIntegerField maxRowCountField = new JepIntegerField(supplierText.supplier_detail_row_count());
		maxRowCountField.setMaxLength(4);
		maxRowCountField.setFieldWidth(55);
		
		innerPanel.add(exclusiveSupplierFlagCheckBoxField);
		innerPanel.add(exclusiveSupplierOptionComboBoxField);
		innerPanel.add(supplierDescriptionTextAreaField);
		innerPanel.add(maxRowCountField);
		
		// init main widget
		setWidget(innerPanel, BlockPositionEnum.DOWN);
		// initialize fieldset caption
		asWidget().setCaptionText(supplierText.supplier_restFieldSet_title());
		
		fields.put(EXCLUSIVE_SUPPLIER_FLAG, exclusiveSupplierFlagCheckBoxField);
		fields.put(EXCLUSIVE_SUPPLIER_OPTION, exclusiveSupplierOptionComboBoxField);
		fields.put(SUPPLIER_DESCRIPTION, supplierDescriptionTextAreaField);
		fields.put(MAX_ROW_COUNT, maxRowCountField);
	}
}
