package com.technology.jep.jepriashowcase.requestprocess.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.REQUESTPROCESS_MODULE_ID;
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
 
import com.technology.jep.jepriashowcase.requestprocess.client.ui.form.detail.RequestProcessDetailFormPresenter;
import com.technology.jep.jepriashowcase.requestprocess.client.ui.form.detail.RequestProcessDetailFormViewImpl;
import com.technology.jep.jepriashowcase.requestprocess.client.ui.form.list.RequestProcessListFormViewImpl;
import com.technology.jep.jepriashowcase.requestprocess.client.ui.form.list.RequestProcessListFormPresenter;
import com.technology.jep.jepriashowcase.requestprocess.shared.service.RequestProcessService;
import com.technology.jep.jepriashowcase.requestprocess.shared.service.RequestProcessServiceAsync;
import com.technology.jep.jepriashowcase.requestprocess.shared.record.RequestProcessRecordDefinition;
 
public class RequestProcessClientFactoryImpl<E extends PlainEventBus, S extends RequestProcessServiceAsync>
	extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {
 
	private static final IsWidget requestProcessDetailFormView = new RequestProcessDetailFormViewImpl();
	private static final IsWidget requestProcessListFormView = new RequestProcessListFormViewImpl();
 
	public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
	public RequestProcessClientFactoryImpl() {
		super(RequestProcessRecordDefinition.instance);
		initActivityMappers(this);
	}
 
	static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
		if(instance == null) {
			instance = GWT.create(RequestProcessClientFactoryImpl.class);
		}
		return instance;
	}
 
 
public JepPresenter createPlainModulePresenter(Place place) {
	return new StandardModulePresenter(REQUESTPROCESS_MODULE_ID, place, this);
}
 
	public JepPresenter createDetailFormPresenter(Place place) {
		return new RequestProcessDetailFormPresenter(place, this);
	}
 
	public JepPresenter createListFormPresenter(Place place) {
		return new RequestProcessListFormPresenter(place, this);
	}
 
	public IsWidget getDetailFormView() {
		return requestProcessDetailFormView;
	}
 
	public IsWidget getListFormView() {
		return requestProcessListFormView;
	}
 
	public S getService() {
		if(dataService == null) {
			dataService = (S) GWT.create(RequestProcessService.class);
		}
		return dataService;
	}
}
