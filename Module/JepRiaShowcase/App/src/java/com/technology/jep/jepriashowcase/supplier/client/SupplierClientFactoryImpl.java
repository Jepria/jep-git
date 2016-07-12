package com.technology.jep.jepriashowcase.supplier.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.SUPPLIER_MODULE_ID;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
 
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.form.list.ListFormView;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;
import com.technology.jep.jepria.client.ui.plain.StandardModulePresenter;
import com.technology.jep.jepria.client.history.place.JepWorkstatePlace;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarPresenter;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarView;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarViewImpl;
import com.technology.jep.jepria.client.ui.wizard.BlockManager;
import com.technology.jep.jepria.client.ui.wizard.BlockManagerImpl;
 
import com.technology.jep.jepriashowcase.supplier.client.ui.form.detail.SupplierDetailFormPresenter;
import com.technology.jep.jepriashowcase.supplier.client.ui.form.detail.SupplierDetailFormViewImpl;
import com.technology.jep.jepriashowcase.supplier.client.ui.form.list.SupplierListFormViewImpl;
import com.technology.jep.jepriashowcase.supplier.client.ui.form.list.SupplierListFormPresenter;
import com.technology.jep.jepriashowcase.supplier.client.ui.wizard.SupplierFirstBlockClientFactory;
import com.technology.jep.jepriashowcase.supplier.client.ui.wizard.SupplierSecondBlockClientFactory;
import com.technology.jep.jepriashowcase.supplier.client.ui.wizard.SupplierThirdBlockClientFactory;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierService;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierServiceAsync;
import com.technology.jep.jepriashowcase.supplier.shared.record.SupplierRecordDefinition;
 
public class SupplierClientFactoryImpl<E extends PlainEventBus, S extends SupplierServiceAsync>
  extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {
 
  private static final IsWidget supplierDetailFormView = new SupplierDetailFormViewImpl();
  private static final IsWidget supplierListFormView = new SupplierListFormViewImpl();
 
  public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
  
  public SupplierClientFactoryImpl() {
    super(SupplierRecordDefinition.instance);
    initActivityMappers(this);
  }
 
  static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(SupplierClientFactoryImpl.class);
    }
    return instance;
  }
 
 
public JepPresenter createPlainModulePresenter(Place place) {
  return new StandardModulePresenter(SUPPLIER_MODULE_ID, place, this);
}
 
  public JepPresenter createDetailFormPresenter(Place place) {
    return new SupplierDetailFormPresenter(place, this);
  }
 
  public JepPresenter createListFormPresenter(Place place) {
    return new SupplierListFormPresenter(place, this);
  }
 
  public IsWidget getDetailFormView() {
    return supplierDetailFormView;
  }
 
  public IsWidget getListFormView() {
    return supplierListFormView;
  }
 
  public S getService() {
    if(dataService == null) {
      dataService = (S) GWT.create(SupplierService.class);
    }
    return dataService;
  }
}
