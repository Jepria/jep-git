package com.technology.jep.jepriashowcase.main.client.ui.main;

import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.ALLSHOPGOODS_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.FEATURE_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.GOODS_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.REQUESTPROCESS_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.REQUEST_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.SHOPGOODS_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.SUPPLIER_MODULE_ID;

import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactory;
import com.technology.jep.jepria.client.ui.main.MainModulePresenter;
import com.technology.jep.jepria.client.ui.main.MainView;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

public class JepRiaShowcaseMainModulePresenter<E extends MainEventBus, S extends JepMainServiceAsync> 
	extends MainModulePresenter<MainView, E, S, MainClientFactory<E, S>>	{

	public JepRiaShowcaseMainModulePresenter(MainClientFactory<E, S> clientFactory) {
		super(clientFactory);
		
		addModuleProtection(SUPPLIER_MODULE_ID, "JrsEditSupplier");
		addModuleProtection(GOODS_MODULE_ID, "JrsEditGoods");
		addModuleProtection(SHOPGOODS_MODULE_ID, "JrsEditShopGoods");
		addModuleProtection(ALLSHOPGOODS_MODULE_ID, "JrsEditShopGoods");
		addModuleProtection(REQUEST_MODULE_ID, "JrsEditRequest");
		addModuleProtection(REQUESTPROCESS_MODULE_ID, "JrsEditRequestProcess");
		addModuleProtection(FEATURE_MODULE_ID, "JrsEditFeature");
		
		setProtectedModuleItemsVisibility(false);
	}
	
}


