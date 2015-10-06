package com.technology.jep.jepriashowcase.requestprocess.client.ui.form.list;
 
import com.google.gwt.place.shared.Place;
 
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepriashowcase.requestprocess.shared.service.RequestProcessServiceAsync;
import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;import com.technology.jep.jepria.client.ui.form.list.ListFormView; 
public class RequestProcessListFormPresenter<V extends ListFormView, E extends PlainEventBus, S extends RequestProcessServiceAsync, F extends StandardClientFactory<E, S>> 
	extends ListFormPresenter<V, E, S, F> { 
 
	public RequestProcessListFormPresenter(Place place, F clientFactory) {
		super(place, clientFactory);
	}
 
}
