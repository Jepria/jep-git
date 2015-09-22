package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.supplierText;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.BANK_BIC;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.KS;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.RECIPIENT_NAME;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SETTLEMENT_ACCOUNT;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.wizard.BlockPositionEnum;
import com.technology.jep.jepria.client.ui.wizard.BlockViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepMaskedTextField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;

public class SupplierFifthBlockViewImpl extends BlockViewImpl {

	public SupplierFifthBlockViewImpl(){
		
		VerticalPanel innerPanel = new VerticalPanel();
		
		JepComboBoxField bankBicComboBoxField = new JepComboBoxField(supplierText.supplier_detail_bankBic());
		bankBicComboBoxField.setFieldWidth(450);
		bankBicComboBoxField.setEmptyText(supplierText.supplier_detail_bankBic_emptyText());
		JepTextField ksTextField = new JepTextField(supplierText.supplier_detail_ks());
		JepTextField recipientNameTextField = new JepTextField(supplierText.supplier_detail_recipientName());
		recipientNameTextField.setFieldWidth(450);
		recipientNameTextField.setMaxLength(150);
		JepMaskedTextField settlementAccountMaskedTextField = new JepMaskedTextField(supplierText.supplier_detail_settlementAccount(), "4\\000081\\0000000000000");
		
		innerPanel.add(bankBicComboBoxField);
		innerPanel.add(ksTextField);
		innerPanel.add(recipientNameTextField);
		innerPanel.add(settlementAccountMaskedTextField);
		
		// init main widget
		setWidget(innerPanel, BlockPositionEnum.DOWN);
		// initialize fieldset caption
		asWidget().setCaptionText(supplierText.supplier_detail_bankDetailsFieldSet_title());
		
		fields.put(BANK_BIC, bankBicComboBoxField);
		fields.put(KS, ksTextField);
		fields.put(RECIPIENT_NAME, recipientNameTextField);
		fields.put(SETTLEMENT_ACCOUNT, settlementAccountMaskedTextField);
	}
}
