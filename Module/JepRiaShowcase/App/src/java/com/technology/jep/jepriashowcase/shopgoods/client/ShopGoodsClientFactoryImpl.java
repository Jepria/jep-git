package com.technology.jep.jepriashowcase.shopgoods.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.SHOPGOODS_MODULE_ID;
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
 
import com.technology.jep.jepriashowcase.shopgoods.client.ui.form.detail.ShopGoodsDetailFormPresenter;
import com.technology.jep.jepriashowcase.shopgoods.client.ui.form.detail.ShopGoodsDetailFormViewImpl;
import com.technology.jep.jepriashowcase.shopgoods.client.ui.form.list.ShopGoodsListFormViewImpl;
import com.technology.jep.jepriashowcase.shopgoods.client.ui.form.list.ShopGoodsListFormPresenter;
import com.technology.jep.jepriashowcase.shopgoods.shared.service.ShopGoodsService;
import com.technology.jep.jepriashowcase.shopgoods.shared.service.ShopGoodsServiceAsync;
import com.technology.jep.jepriashowcase.shopgoods.shared.record.ShopGoodsRecordDefinition;
 
public class ShopGoodsClientFactoryImpl<E extends PlainEventBus, S extends ShopGoodsServiceAsync>
	extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {
 
	private static final IsWidget shopGoodsDetailFormView = new ShopGoodsDetailFormViewImpl();
	private static final IsWidget shopGoodsListFormView = new ShopGoodsListFormViewImpl();
 
	public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
	public ShopGoodsClientFactoryImpl() {
		super(ShopGoodsRecordDefinition.instance);
		initActivityMappers(this);
	}
 
	static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
		if(instance == null) {
			instance = GWT.create(ShopGoodsClientFactoryImpl.class);
		}
		return instance;
	}
 
 
public JepPresenter createPlainModulePresenter(Place place) {
	return new StandardModulePresenter(SHOPGOODS_MODULE_ID, place, this);
}
 
	public JepPresenter createDetailFormPresenter(Place place) {
		return new ShopGoodsDetailFormPresenter(place, this);
	}
 
	public JepPresenter createListFormPresenter(Place place) {
		return new ShopGoodsListFormPresenter(place, this);
	}
 
	public IsWidget getDetailFormView() {
		return shopGoodsDetailFormView;
	}
 
	public IsWidget getListFormView() {
		return shopGoodsListFormView;
	}
 
	public S getService() {
		if(dataService == null) {
			dataService = (S) GWT.create(ShopGoodsService.class);
		}
		return dataService;
	}
}
