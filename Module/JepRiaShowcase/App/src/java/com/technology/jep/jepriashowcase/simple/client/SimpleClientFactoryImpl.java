package com.technology.jep.jepriashowcase.simple.client;

import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.SIMPLE_MODULE_ID;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.jepriashowcase.simple.client.ui.plain.SimpleModulePresenter;
import com.technology.jep.jepriashowcase.simple.client.ui.plain.SimpleModuleViewImpl;
import com.technology.jep.jepriashowcase.simple.shared.record.SimpleRecordDefinition;

/**
 * Реализация клиентской фабрики простого модуля.
 */
public class SimpleClientFactoryImpl<E extends PlainEventBus, S extends JepDataServiceAsync> extends PlainClientFactoryImpl<E, S> {

  /**
   * Поле для реализации singleton'а клиентской фабрики модуля.
   */
  public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
  
  public SimpleClientFactoryImpl() {
    super(SimpleRecordDefinition.instance);
    initActivityMappers(this);
  }

  static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(SimpleClientFactoryImpl.class);
    }
    return instance;
  }

  public IsWidget getModuleView() {
    if(moduleView == null) {
      moduleView = new SimpleModuleViewImpl();
    }
    return moduleView;
  }

  public JepPresenter createPlainModulePresenter(Place place) {
    return new SimpleModulePresenter(SIMPLE_MODULE_ID, place, this);
  }
  
}
