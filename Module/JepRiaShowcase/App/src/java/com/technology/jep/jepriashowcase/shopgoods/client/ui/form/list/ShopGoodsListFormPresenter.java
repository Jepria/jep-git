package com.technology.jep.jepriashowcase.shopgoods.client.ui.form.list;
 
import com.google.gwt.place.shared.Place;
 
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepriashowcase.shopgoods.shared.service.ShopGoodsServiceAsync;
import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;import com.technology.jep.jepria.client.ui.form.list.ListFormView; 
public class ShopGoodsListFormPresenter<V extends ListFormView, E extends PlainEventBus, S extends ShopGoodsServiceAsync, F extends StandardClientFactory<E, S>> 
  extends ListFormPresenter<V, E, S, F> { 
 
  public ShopGoodsListFormPresenter(Place place, F clientFactory) {
    super(place, clientFactory);
  }
 
}
