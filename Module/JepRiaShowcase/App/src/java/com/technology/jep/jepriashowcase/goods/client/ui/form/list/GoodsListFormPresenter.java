package com.technology.jep.jepriashowcase.goods.client.ui.form.list;
 
import com.google.gwt.place.shared.Place;
 
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.history.place.JepSelectedPlace;
import com.technology.jep.jepria.client.history.place.JepViewDetailPlace;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepriashowcase.goods.shared.service.GoodsServiceAsync;
import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;
import com.technology.jep.jepria.client.ui.form.list.ListFormView;
import com.technology.jep.jepria.shared.record.JepRecord;

public class GoodsListFormPresenter<V extends ListFormView, E extends PlainEventBus, S extends GoodsServiceAsync, F extends StandardClientFactory<E, S>> 
	extends ListFormPresenter<V, E, S, F> { 
 
	public GoodsListFormPresenter(Place place, F clientFactory) {
		super(place, clientFactory);
	}
}
