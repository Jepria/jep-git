package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.FAX_NUMBER;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.PHONE_NUMBER;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.wizard.BlockPresenter;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierServiceAsync;

public class SupplierFourthBlockPresenter<S extends SupplierServiceAsync> extends BlockPresenter<SupplierFourthBlockViewImpl, S, SupplierFourthBlockClientFactory<S>> {

  public SupplierFourthBlockPresenter(Place place, SupplierFourthBlockClientFactory<S> clientFactory) {
    super(place, clientFactory);
  }
  
  protected void adjustToWorkstate(WorkstateEnum workstate) {
    fields.setFieldVisible(PHONE_NUMBER, !SEARCH.equals(workstate));
    fields.setFieldVisible(FAX_NUMBER, !SEARCH.equals(workstate));
  }
}
