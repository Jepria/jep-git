package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SUPPLIER_NAME;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.wizard.BlockPresenter;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierServiceAsync;

public class SupplierFirstBlockPresenter<S extends SupplierServiceAsync> extends BlockPresenter<SupplierFirstBlockViewImpl, S, SupplierFirstBlockClientFactory<S>> {

	public SupplierFirstBlockPresenter(Place place, SupplierFirstBlockClientFactory<S> clientFactory) {
		super(place, clientFactory);
	}
	
	protected void adjustToWorkstate(WorkstateEnum workstate) {
		fields.setFieldAllowBlank(SUPPLIER_NAME, !(CREATE.equals(workstate) || EDIT.equals(workstate)));
	}
}
