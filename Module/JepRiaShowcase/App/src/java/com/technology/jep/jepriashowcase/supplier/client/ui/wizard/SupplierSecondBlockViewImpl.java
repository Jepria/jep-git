package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.supplierText;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.CONTRACT_FINISH_DATE;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.CONTRACT_FINISH_DATE_FROM;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.CONTRACT_FINISH_DATE_TO;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.wizard.BlockPositionEnum;
import com.technology.jep.jepria.client.ui.wizard.BlockViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;

public class SupplierSecondBlockViewImpl extends BlockViewImpl {

	public SupplierSecondBlockViewImpl(){
		
		VerticalPanel innerPanel = new VerticalPanel();
		
		JepDateField contractFinishDateDateField = new JepDateField(supplierText.supplier_detail_contract_finish_date());
		JepDateField contractFinishDateFromDateField = new JepDateField(supplierText.supplier_detail_contract_finish_date_from());
		JepDateField contractFinishDateToDateField = new JepDateField(supplierText.supplier_detail_contract_finish_date_to());
		
		innerPanel.add(contractFinishDateDateField);
		innerPanel.add(contractFinishDateFromDateField);
		innerPanel.add(contractFinishDateToDateField);

		// set widget
		setWidget(innerPanel, BlockPositionEnum.RIGHT);
		// set caption label
		asWidget().setCaptionText(supplierText.supplier_datesFieldSet_title());
		
		fields.put(CONTRACT_FINISH_DATE, contractFinishDateDateField);
		fields.put(CONTRACT_FINISH_DATE_FROM, contractFinishDateFromDateField);
		fields.put(CONTRACT_FINISH_DATE_TO, contractFinishDateToDateField);
	}
}
