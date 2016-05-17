package com.technology.jep.jepriashowcase.requestfeature.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.REQUESTFEATURE_MODULE_ID;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl;
import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;
import com.technology.jep.jepria.client.ui.form.list.ListFormViewImpl;
import com.technology.jep.jepria.client.ui.form.list.StandardListFormViewImpl;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;
import com.technology.jep.jepria.client.ui.plain.StandardModulePresenter;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.client.widget.list.GridManager;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.shared.service.data.JepDataService;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.jepriashowcase.requestfeature.client.ui.form.detail.RequestFeatureDetailFormPresenter;
import com.technology.jep.jepriashowcase.requestfeature.client.ui.form.detail.RequestFeatureDetailFormViewImpl;
import com.technology.jep.jepriashowcase.requestfeature.shared.record.RequestFeatureRecordDefinition;
 
public class RequestFeatureClientFactoryImpl<E extends PlainEventBus, S extends JepDataServiceAsync/*TODO change to RequestFeatureServiceAsync*/>
	extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {
 
	private static final IsWidget requestFeatureDetailFormView = new RequestFeatureDetailFormViewImpl();
	private static final IsWidget requestFeatureListFormView = new StandardListFormViewImpl/*TODO change to RequestFeatureListFormViewImpl*/(){
		@Override
		protected List<JepColumn> getColumnConfigurations() {
			return null;
		}};
 
	public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
	public RequestFeatureClientFactoryImpl() {
		super(RequestFeatureRecordDefinition.instance);
		initActivityMappers(this);
	}
 
	static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
		if(instance == null) {
			instance = GWT.create(RequestFeatureClientFactoryImpl.class);
		}
		return instance;
	}
 
 
	public JepPresenter createPlainModulePresenter(Place place) {
		return new StandardModulePresenter(REQUESTFEATURE_MODULE_ID, place, this);
	}
 
	public JepPresenter createDetailFormPresenter(Place place) {
		return new RequestFeatureDetailFormPresenter(place, this);
	}
 
	public JepPresenter createListFormPresenter(Place place) {
		return new ListFormPresenter/*TODO change to RequestFeatureListFormPresenter*/(place, this);
	}
 
	public IsWidget getDetailFormView() {
		return requestFeatureDetailFormView;
	}
 
	public IsWidget getListFormView() {
		return requestFeatureListFormView;
	}
 
	public S getService() {
		if(dataService == null) {
			dataService = (S) GWT.create(JepDataService/*TODO change to RequestFeatureService*/.class);
		}
		return dataService;
	}
}
