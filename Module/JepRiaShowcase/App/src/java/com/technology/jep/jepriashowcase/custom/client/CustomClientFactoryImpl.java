package com.technology.jep.jepriashowcase.custom.client;

import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.CUSTOM_MODULE_ID;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.jepriashowcase.custom.client.ui.plain.CustomModulePresenter;
import com.technology.jep.jepriashowcase.custom.client.ui.plain.CustomModuleViewImpl;
import com.technology.jep.jepriashowcase.custom.shared.record.CustomRecordDefinition;
import com.technology.jep.jepriashowcase.custom.shared.service.CustomService;
import com.technology.jep.jepriashowcase.custom.shared.service.CustomServiceAsync;

/**
 * Реализация клиентской фабрики простого модуля.
 */
public class CustomClientFactoryImpl<E extends PlainEventBus, S extends CustomServiceAsync> extends PlainClientFactoryImpl<E, S> {

  /**
   * Поле для реализации singleton'а клиентской фабрики модуля.
   */
  public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
  
  public CustomClientFactoryImpl() {
    super(CustomRecordDefinition.instance);
    initActivityMappers(this);
  }

  static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(CustomClientFactoryImpl.class);
    }
    return instance;
  }

  public IsWidget getModuleView() {
    if(moduleView == null) {
      moduleView = new CustomModuleViewImpl();
    }
    return moduleView;
  }

  public S getService() {
    if(dataService == null) {
      dataService = (S) GWT.create(CustomService.class);
    }
    return dataService;
  }

  public JepPresenter createPlainModulePresenter(Place place) {
    return new CustomModulePresenter(CUSTOM_MODULE_ID, place, this);
  }
  
}
