package com.technology.jep.jepriashowcase.allshopgoods.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.*;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.*;
import static com.technology.jep.jepriashowcase.allshopgoods.client.AllShopGoodsClientConstant.*;
 
import com.google.gwt.place.shared.Place;
 
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
import com.technology.jep.jepriashowcase.allshopgoods.shared.service.AllShopGoodsServiceAsync;
 
public class AllShopGoodsDetailFormPresenter<E extends PlainEventBus, S extends AllShopGoodsServiceAsync> 
		extends DetailFormPresenter<AllShopGoodsDetailFormView, E, S, StandardClientFactory<E, S>> { 
 
	public AllShopGoodsDetailFormPresenter(Place place, StandardClientFactory<E, S> clientFactory) {
		super(scopeModuleIds, place, clientFactory);
	}
 
	/* public void bind() {
		super.bind();
		// Здесь размещается код связывания presenter-а и view 
	}
	*/ 
 
	protected void adjustToWorkstate(WorkstateEnum workstate) {
	}
 
}
