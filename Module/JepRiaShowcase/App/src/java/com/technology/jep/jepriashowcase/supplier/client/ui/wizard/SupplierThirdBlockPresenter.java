package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepria.shared.field.JepFieldNames.MAX_ROW_COUNT;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.EXCLUSIVE_SUPPLIER_FLAG;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.EXCLUSIVE_SUPPLIER_OPTION;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SUPPLIER_DESCRIPTION;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Timer;
import com.technology.jep.jepria.client.JepRiaClientConstant;
import com.technology.jep.jepria.client.async.FirstTimeUseAsyncCallback;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.wizard.BlockPresenter;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierServiceAsync;

public class SupplierThirdBlockPresenter<S extends SupplierServiceAsync> extends BlockPresenter<SupplierThirdBlockViewImpl, S, SupplierThirdBlockClientFactory<S>> {

	public SupplierThirdBlockPresenter(Place place, SupplierThirdBlockClientFactory<S> clientFactory) {
		super(place, clientFactory);
	}
	
	public void bind() {
		super.bind();
		
		fields.addFieldListener(EXCLUSIVE_SUPPLIER_OPTION, JepEventType.FIRST_TIME_USE_EVENT, new JepListener() {
			@Override
			public void handleEvent(JepEvent event) {
				new FirstTimeUseAsyncCallback<List<JepOption>>(event) {
					@Override
					public void onSuccessLoad(final List<JepOption> result) {
						fields.setFieldOptions(EXCLUSIVE_SUPPLIER_OPTION, result);	
							
					}
				}.onSuccess(JepOption.buildListFromToken(JepRiaClientConstant.JepTexts.form_detail_option_token_yesNo()));
			}
		});
		
	}
	
	protected void adjustToWorkstate(WorkstateEnum workstate) {
		fields.setFieldVisible(SUPPLIER_DESCRIPTION, VIEW_DETAILS.equals(workstate));
		fields.setFieldVisible(EXCLUSIVE_SUPPLIER_FLAG, !SEARCH.equals(workstate));
		fields.setFieldVisible(EXCLUSIVE_SUPPLIER_OPTION, SEARCH.equals(workstate));
		
		fields.setFieldVisible(MAX_ROW_COUNT, SEARCH.equals(workstate));
		fields.setFieldAllowBlank(MAX_ROW_COUNT, !SEARCH.equals(workstate));
		fields.setFieldValue(MAX_ROW_COUNT, 25);
	}
}
