package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.CONTRACT_FINISH_DATE_FROM;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SUPPLIER_ID;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SUPPLIER_NAME;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.wizard.BlockPresenter;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.validation.Validator;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierServiceAsync;

public class SupplierFirstBlockPresenter<S extends SupplierServiceAsync> extends BlockPresenter<SupplierFirstBlockViewImpl, S, SupplierFirstBlockClientFactory<S>> {

	public SupplierFirstBlockPresenter(Place place, SupplierFirstBlockClientFactory<S> clientFactory) {
		super(place, clientFactory);
	}
	
	private Integer supplierId = null;
	
	public void bind(){
		super.bind();
		
		fields.addFieldListener(SUPPLIER_ID, JepEventType.LOST_FOCUS_EVENT, new JepListener() {
			@Override
			public void handleEvent(JepEvent event) {
				if (SEARCH.equals(_workstate)){
					final Integer id = fields.getFieldValue(SUPPLIER_ID);
					if (!JepRiaUtil.isEmpty(id)) {
						fields.get(SUPPLIER_NAME).setLoadingImage(true);
						service.getSupplierNameById(id, new JepAsyncCallback<String>() {
							@Override
							public void onSuccess(String result){
								fields.setFieldValue(SUPPLIER_NAME, result);
								supplierId = id;
								fields.get(SUPPLIER_NAME).setLoadingImage(false);
							}
							@Override
							public void onFailure(Throwable th){
								fields.get(SUPPLIER_NAME).setLoadingImage(false);
								super.onFailure(th);
							}
						});
					}
					else {
						fields.clearField(SUPPLIER_NAME);
						supplierId = id;
					}
				}
			}
		});
		
		view.setValidator(new Validator(){
			@Override
			public boolean isValid() {
				return SEARCH.equals(_workstate) ? JepRiaUtil.equalWithNull(supplierId, fields.getFieldValue(SUPPLIER_ID)) : true;
			}
		});
	}
	
	protected void adjustToWorkstate(WorkstateEnum workstate) {
		supplierId = null;
		fields.setFieldAllowBlank(SUPPLIER_NAME, !(CREATE.equals(workstate) || EDIT.equals(workstate)));
	}
}
