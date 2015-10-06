package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.CONTRACT_FINISH_DATE;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.CONTRACT_FINISH_DATE_FROM;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.CONTRACT_FINISH_DATE_TO;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.wizard.BlockPresenter;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierServiceAsync;

public class SupplierSecondBlockPresenter<S extends SupplierServiceAsync> extends BlockPresenter<SupplierSecondBlockViewImpl, S, SupplierSecondBlockClientFactory<S>> {

	public SupplierSecondBlockPresenter(Place place, SupplierSecondBlockClientFactory<S> clientFactory) {
		super(place, clientFactory);
	}

	protected void adjustToWorkstate(WorkstateEnum workstate) {
		fields.setFieldVisible(CONTRACT_FINISH_DATE, CREATE.equals(workstate) || EDIT.equals(workstate) || VIEW_DETAILS.equals(workstate));
		fields.setFieldVisible(CONTRACT_FINISH_DATE_FROM, SEARCH.equals(workstate));
		fields.setFieldVisible(CONTRACT_FINISH_DATE_TO, SEARCH.equals(workstate));
		
		fields.setFieldAllowBlank(CONTRACT_FINISH_DATE, !(CREATE.equals(workstate) || EDIT.equals(workstate)));
	}
}
