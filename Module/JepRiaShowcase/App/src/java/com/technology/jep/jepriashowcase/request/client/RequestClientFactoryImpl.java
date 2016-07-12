package com.technology.jep.jepriashowcase.request.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.REQUEST_MODULE_ID;
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
 
import com.technology.jep.jepriashowcase.request.client.ui.form.detail.RequestDetailFormPresenter;
import com.technology.jep.jepriashowcase.request.client.ui.form.detail.RequestDetailFormViewImpl;
import com.technology.jep.jepriashowcase.request.client.ui.form.list.RequestListFormViewImpl;
import com.technology.jep.jepriashowcase.request.client.ui.form.list.RequestListFormPresenter;
import com.technology.jep.jepriashowcase.request.shared.service.RequestService;
import com.technology.jep.jepriashowcase.request.shared.service.RequestServiceAsync;
import com.technology.jep.jepriashowcase.request.shared.record.RequestRecordDefinition;
 
public class RequestClientFactoryImpl<E extends PlainEventBus, S extends RequestServiceAsync>
  extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {
 
  private static final IsWidget requestDetailFormView = new RequestDetailFormViewImpl();
  private static final IsWidget requestListFormView = new RequestListFormViewImpl();
 
  public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
  public RequestClientFactoryImpl() {
    super(RequestRecordDefinition.instance);
    initActivityMappers(this);
  }
 
  static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(RequestClientFactoryImpl.class);
    }
    return instance;
  }
 
 
public JepPresenter createPlainModulePresenter(Place place) {
  return new StandardModulePresenter(REQUEST_MODULE_ID, place, this);
}
 
  public JepPresenter createDetailFormPresenter(Place place) {
    return new RequestDetailFormPresenter(place, this);
  }
 
  public JepPresenter createListFormPresenter(Place place) {
    return new RequestListFormPresenter(place, this);
  }
 
  public IsWidget getDetailFormView() {
    return requestDetailFormView;
  }
 
  public IsWidget getListFormView() {
    return requestListFormView;
  }
 
  public S getService() {
    if(dataService == null) {
      dataService = (S) GWT.create(RequestService.class);
    }
    return dataService;
  }
}
