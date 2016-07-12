package com.technology.jep.jepriashowcase.feature.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.FEATURE_MODULE_ID;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;
import com.technology.jep.jepria.client.ui.form.list.StandardListFormViewImpl;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;
import com.technology.jep.jepria.client.ui.plain.StandardModulePresenter;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.jepriashowcase.feature.client.ui.form.detail.FeatureDetailFormPresenter;
import com.technology.jep.jepriashowcase.feature.client.ui.form.detail.FeatureDetailFormViewImpl;
import com.technology.jep.jepriashowcase.feature.client.ui.form.list.FeatureListFormViewImpl;
import com.technology.jep.jepriashowcase.feature.shared.record.FeatureRecordDefinition;
import com.technology.jep.jepriashowcase.feature.shared.service.FeatureService;
import com.technology.jep.jepriashowcase.feature.shared.service.FeatureServiceAsync;
 
public class FeatureClientFactoryImpl<E extends PlainEventBus, S extends FeatureServiceAsync>
  extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {
 
  private static final IsWidget featureDetailFormView = new FeatureDetailFormViewImpl();
  private static final IsWidget featureListFormView = new FeatureListFormViewImpl();
 
  public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
  public FeatureClientFactoryImpl() {
    super(FeatureRecordDefinition.instance);
    initActivityMappers(this);
  }
 
  static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(FeatureClientFactoryImpl.class);
    }
    return instance;
  }
 
 
  public JepPresenter createPlainModulePresenter(Place place) {
    return new StandardModulePresenter(FEATURE_MODULE_ID, place, this);
  }
 
  public JepPresenter createDetailFormPresenter(Place place) {
    return new FeatureDetailFormPresenter(place, this);
  }
 
  public JepPresenter createListFormPresenter(Place place) {
    return new ListFormPresenter/*TODO change to FeatureListFormPresenter*/(place, this);
  }
 
  public IsWidget getDetailFormView() {
    return featureDetailFormView;
  }
 
  public IsWidget getListFormView() {
    return featureListFormView;
  }
 
  public S getService() {
    if(dataService == null) {
      dataService = (S) GWT.create(FeatureService.class);
    }
    return dataService;
  }
}
