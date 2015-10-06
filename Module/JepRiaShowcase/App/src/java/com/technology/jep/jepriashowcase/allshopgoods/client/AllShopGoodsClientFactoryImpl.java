package com.technology.jep.jepriashowcase.allshopgoods.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.ALLSHOPGOODS_MODULE_ID;
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
 
import com.technology.jep.jepriashowcase.allshopgoods.client.ui.form.detail.AllShopGoodsDetailFormPresenter;
import com.technology.jep.jepriashowcase.allshopgoods.client.ui.form.detail.AllShopGoodsDetailFormViewImpl;
import com.technology.jep.jepriashowcase.allshopgoods.client.ui.form.list.AllShopGoodsListFormViewImpl;
import com.technology.jep.jepriashowcase.allshopgoods.client.ui.form.list.AllShopGoodsListFormPresenter;
import com.technology.jep.jepriashowcase.allshopgoods.shared.service.AllShopGoodsService;
import com.technology.jep.jepriashowcase.allshopgoods.shared.service.AllShopGoodsServiceAsync;
import com.technology.jep.jepriashowcase.allshopgoods.shared.record.AllShopGoodsRecordDefinition;
 
public class AllShopGoodsClientFactoryImpl<E extends PlainEventBus, S extends AllShopGoodsServiceAsync>
	extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {
 
	private static final IsWidget allShopGoodsDetailFormView = new AllShopGoodsDetailFormViewImpl();
	private static final IsWidget allShopGoodsListFormView = new AllShopGoodsListFormViewImpl();
 
	public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
	public AllShopGoodsClientFactoryImpl() {
		super(AllShopGoodsRecordDefinition.instance);
		initActivityMappers(this);
	}
 
	static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
		if(instance == null) {
			instance = GWT.create(AllShopGoodsClientFactoryImpl.class);
		}
		return instance;
	}
 
 
public JepPresenter createPlainModulePresenter(Place place) {
	return new StandardModulePresenter(ALLSHOPGOODS_MODULE_ID, place, this);
}
 
	public JepPresenter createDetailFormPresenter(Place place) {
		return new AllShopGoodsDetailFormPresenter(place, this);
	}
 
	public JepPresenter createListFormPresenter(Place place) {
		return new AllShopGoodsListFormPresenter(place, this);
	}
 
	public IsWidget getDetailFormView() {
		return allShopGoodsDetailFormView;
	}
 
	public IsWidget getListFormView() {
		return allShopGoodsListFormView;
	}
 
	public S getService() {
		if(dataService == null) {
			dataService = (S) GWT.create(AllShopGoodsService.class);
		}
		return dataService;
	}
}
