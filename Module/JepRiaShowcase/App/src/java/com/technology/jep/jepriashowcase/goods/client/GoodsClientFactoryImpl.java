package com.technology.jep.jepriashowcase.goods.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.GOODS_MODULE_ID;
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
 
import com.technology.jep.jepriashowcase.goods.client.ui.form.detail.GoodsDetailFormPresenter;
import com.technology.jep.jepriashowcase.goods.client.ui.form.detail.GoodsDetailFormViewImpl;
import com.technology.jep.jepriashowcase.goods.client.ui.form.list.GoodsListFormViewImpl;
import com.technology.jep.jepriashowcase.goods.client.ui.form.list.GoodsListFormPresenter;
import com.technology.jep.jepriashowcase.goods.shared.service.GoodsService;
import com.technology.jep.jepriashowcase.goods.shared.service.GoodsServiceAsync;
import com.technology.jep.jepriashowcase.goods.shared.record.GoodsRecordDefinition;
 
public class GoodsClientFactoryImpl<E extends PlainEventBus, S extends GoodsServiceAsync>
	extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {
 
	private static final IsWidget goodsDetailFormView = new GoodsDetailFormViewImpl();
	private static final IsWidget goodsListFormView = new GoodsListFormViewImpl();
 
	public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
	public GoodsClientFactoryImpl() {
		super(GoodsRecordDefinition.instance);
		initActivityMappers(this);
	}
 
	static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
		if(instance == null) {
			instance = GWT.create(GoodsClientFactoryImpl.class);
		}
		return instance;
	}
 
 
public JepPresenter createPlainModulePresenter(Place place) {
	return new StandardModulePresenter(GOODS_MODULE_ID, place, this);
}
 
	public JepPresenter createDetailFormPresenter(Place place) {
		return new GoodsDetailFormPresenter(place, this);
	}
 
	public JepPresenter createListFormPresenter(Place place) {
		return new GoodsListFormPresenter(place, this);
	}
 
	public IsWidget getDetailFormView() {
		return goodsDetailFormView;
	}
 
	public IsWidget getListFormView() {
		return goodsListFormView;
	}
 
	public S getService() {
		if(dataService == null) {
			dataService = (S) GWT.create(GoodsService.class);
		}
		return dataService;
	}
}
