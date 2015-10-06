package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.supplierText;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.BANK_BIC;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.KS;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.RECIPIENT_NAME;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SETTLEMENT_ACCOUNT;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.async.TypingTimeoutAsyncCallback;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.wizard.BlockPresenter;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.supplier.shared.field.BankOptions;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierServiceAsync;

public class SupplierFifthBlockPresenter<S extends SupplierServiceAsync> extends BlockPresenter<SupplierFifthBlockViewImpl, S, SupplierFifthBlockClientFactory<S>> {

	private static final int BANK_BIC_OPTION_COUNT = 25;
	
	public SupplierFifthBlockPresenter(Place place, SupplierFifthBlockClientFactory<S> clientFactory) {
		super(place, clientFactory);
	}
	
	public void bind() {
		super.bind();
		
		fields.addFieldListener(BANK_BIC, JepEventType.TYPING_TIMEOUT_EVENT, new JepListener() {			
			@Override
			public void handleEvent(JepEvent event) {
				((JepComboBoxField)fields.get(BANK_BIC)).setLastOptionText(null);
				String rawValue = fields.get(BANK_BIC).getRawValue();
				service.getBank(rawValue + "%", BANK_BIC_OPTION_COUNT + 1, new TypingTimeoutAsyncCallback<List<JepOption>>(event) {
					@Override
					public void onSuccessLoad(List<JepOption> result) {
						if (result.size() > BANK_BIC_OPTION_COUNT) {
							((JepComboBoxField)fields.get(BANK_BIC)).setLastOptionText(supplierText.supplier_detail_bankBic_notAllOptionsDisplayed());
						}
						fields.setFieldOptions(BANK_BIC, result);
					}
				});
				
			}
		});
		
		fields.addFieldListener(BANK_BIC, JepEventType.CHANGE_SELECTION_EVENT, new JepListener(){
			@Override
			public void handleEvent(JepEvent event) {
				JepOption bankOption = fields.getFieldValue(BANK_BIC);
				if (bankOption != null) {
					fields.setFieldValue(KS, bankOption.get(BankOptions.KS));
				}
				else {
					fields.clearField(KS);
				}
			}});
		
	}
	
	protected void adjustToWorkstate(WorkstateEnum workstate) {
		fields.setFieldVisible(BANK_BIC, !SEARCH.equals(workstate));
		fields.setFieldVisible(KS, !SEARCH.equals(workstate));
		fields.setFieldVisible(RECIPIENT_NAME, !SEARCH.equals(workstate));
		fields.setFieldVisible(SETTLEMENT_ACCOUNT, !SEARCH.equals(workstate));
		
		fields.setFieldEditable(KS, false);
	}
}
