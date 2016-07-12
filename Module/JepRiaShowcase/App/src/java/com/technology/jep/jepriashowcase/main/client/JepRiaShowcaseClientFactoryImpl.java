package com.technology.jep.jepriashowcase.main.client;

import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.ALLSHOPGOODS_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.ARSENIC_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.CUSTOM_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.FEATURE_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.GOODS_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.REQUESTPROCESS_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.REQUEST_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.SEARCH_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.SHOPGOODS_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.SIMPLE_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.SUPPLIER_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.jepRiaShowcaseText;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.technology.jep.jepria.client.async.LoadAsyncCallback;
import com.technology.jep.jepria.client.async.LoadPlainClientFactory;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactory;
import com.technology.jep.jepria.client.ui.main.MainClientFactoryImpl;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.jepriashowcase.allshopgoods.client.AllShopGoodsClientFactoryImpl;
import com.technology.jep.jepriashowcase.arsenic.client.ArsenicClientFactoryImpl;
import com.technology.jep.jepriashowcase.custom.client.CustomClientFactoryImpl;
import com.technology.jep.jepriashowcase.feature.client.FeatureClientFactoryImpl;
import com.technology.jep.jepriashowcase.goods.client.GoodsClientFactoryImpl;
import com.technology.jep.jepriashowcase.main.client.ui.main.JepRiaShowcaseMainModulePresenter;
import com.technology.jep.jepriashowcase.request.client.RequestClientFactoryImpl;
import com.technology.jep.jepriashowcase.requestprocess.client.RequestProcessClientFactoryImpl;
import com.technology.jep.jepriashowcase.search.client.SearchClientFactoryImpl;
import com.technology.jep.jepriashowcase.shopgoods.client.ShopGoodsClientFactoryImpl;
import com.technology.jep.jepriashowcase.simple.client.SimpleClientFactoryImpl;
import com.technology.jep.jepriashowcase.supplier.client.SupplierClientFactoryImpl;

public class JepRiaShowcaseClientFactoryImpl<E extends MainEventBus, S extends JepMainServiceAsync>
  extends MainClientFactoryImpl<E, S>
    implements MainClientFactory<E, S> {
  
  static public MainClientFactory<MainEventBus, JepMainServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(JepRiaShowcaseClientFactoryImpl.class);
    }
    return instance;
  }

  private JepRiaShowcaseClientFactoryImpl() {
    super(new String[] {
        CUSTOM_MODULE_ID
        , SEARCH_MODULE_ID
        , SIMPLE_MODULE_ID
        , SUPPLIER_MODULE_ID
        , GOODS_MODULE_ID
        , ARSENIC_MODULE_ID
        , SHOPGOODS_MODULE_ID
        , ALLSHOPGOODS_MODULE_ID
        , REQUEST_MODULE_ID
        , REQUESTPROCESS_MODULE_ID
        , FEATURE_MODULE_ID

      },
      new String[] {
        jepRiaShowcaseText.submodule_custom_title()
        , jepRiaShowcaseText.submodule_simple_title()
        , jepRiaShowcaseText.submodule_search_title()
        , jepRiaShowcaseText.submodule_supplier_title()
        , jepRiaShowcaseText.submodule_goods_title()
        , jepRiaShowcaseText.submodule_arsenic_title()
        , jepRiaShowcaseText.submodule_shopgoods_title()
        , jepRiaShowcaseText.submodule_allshopgoods_title()
        , jepRiaShowcaseText.submodule_request_title()
        , jepRiaShowcaseText.submodule_requestprocess_title()
        , jepRiaShowcaseText.submodule_feature_title()
      }
    );
    
    initActivityMappers(this);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Activity createMainModulePresenter() {
    return new JepRiaShowcaseMainModulePresenter(this);
  }

  public void getPlainClientFactory(String moduleId, final LoadAsyncCallback<PlainClientFactory<PlainEventBus, JepDataServiceAsync>> callback) {
    if(CUSTOM_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + CUSTOM_MODULE_ID);
          return CustomClientFactoryImpl.getInstance();
        }
      });
    }
    else if(SEARCH_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + SEARCH_MODULE_ID);
          return SearchClientFactoryImpl.getInstance();
        }
      });
    } 
    else if(SIMPLE_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + SIMPLE_MODULE_ID);
          return SimpleClientFactoryImpl.getInstance();
        }
      });
    } 
    else if(SUPPLIER_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + SUPPLIER_MODULE_ID);
          return SupplierClientFactoryImpl.getInstance();
        }
      });
    }
    else if(GOODS_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + GOODS_MODULE_ID);
          return GoodsClientFactoryImpl.getInstance();
        }
      });
    }
    else if(ARSENIC_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + ARSENIC_MODULE_ID);
          return ArsenicClientFactoryImpl.getInstance();
        }
      });
    }
    else if(SHOPGOODS_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + SHOPGOODS_MODULE_ID);
          return ShopGoodsClientFactoryImpl.getInstance();
        }
      });
    }
    else if(ALLSHOPGOODS_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + ALLSHOPGOODS_MODULE_ID);
          return AllShopGoodsClientFactoryImpl.getInstance();
        }
      });
    }
    else if(REQUEST_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + REQUEST_MODULE_ID);
          return RequestClientFactoryImpl.getInstance();
        }
      });
    }
    else if(REQUESTPROCESS_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + REQUESTPROCESS_MODULE_ID);
          return RequestProcessClientFactoryImpl.getInstance();
        }
      });
    }
    else if(FEATURE_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(JepRiaShowcaseClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + FEATURE_MODULE_ID);
          return FeatureClientFactoryImpl.getInstance();
        }
      });
    }
  }
  
}
