package com.technology.jep.jepriashowcase.arsenic.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.ARSENIC_MODULE_ID;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;
import com.technology.jep.jepria.client.ui.plain.StandardModulePresenter;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.jepriashowcase.arsenic.client.ui.form.detail.ArsenicDetailFormPresenter;
import com.technology.jep.jepriashowcase.arsenic.client.ui.form.detail.ArsenicDetailFormViewImpl;
import com.technology.jep.jepriashowcase.arsenic.client.ui.form.list.ArsenicListFormViewImpl;
import com.technology.jep.jepriashowcase.arsenic.shared.record.ArsenicRecordDefinition;
import com.technology.jep.jepriashowcase.arsenic.shared.service.ArsenicService;
import com.technology.jep.jepriashowcase.arsenic.shared.service.ArsenicServiceAsync;
 
public class ArsenicClientFactoryImpl<E extends PlainEventBus, S extends ArsenicServiceAsync>
	extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {
 
	private static final IsWidget arsenicDetailFormView = new ArsenicDetailFormViewImpl();
	private static final IsWidget arsenicListFormView = new ArsenicListFormViewImpl();
 
	public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
	public ArsenicClientFactoryImpl() {
		super(ArsenicRecordDefinition.instance);
		initActivityMappers(this);
	}
 
	static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
		if(instance == null) {
			instance = GWT.create(ArsenicClientFactoryImpl.class);
		}
		return instance;
	}
 
 
public JepPresenter createPlainModulePresenter(Place place) {
	return new StandardModulePresenter(ARSENIC_MODULE_ID, place, this);
}
 
	public JepPresenter createDetailFormPresenter(Place place) {
		return new ArsenicDetailFormPresenter(place, this);
	}
 
	public JepPresenter createListFormPresenter(Place place) {
		return new ListFormPresenter(place, this)/*TODO change to local LFP*/;
	}
 
	public IsWidget getDetailFormView() {
		return arsenicDetailFormView;
	}
 
	public IsWidget getListFormView() {
		return arsenicListFormView;
	}
 
	public S getService() {
		if(dataService == null) {
			dataService = (S) GWT.create(ArsenicService.class);
		}
		return dataService;
	}
}
