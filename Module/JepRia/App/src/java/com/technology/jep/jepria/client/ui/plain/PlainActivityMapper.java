package com.technology.jep.jepria.client.ui.plain;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.JepActivityMapper;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

public class PlainActivityMapper<F extends PlainClientFactory<PlainEventBus, JepDataServiceAsync>>  
  extends JepActivityMapper<PlainEventBus, F> implements ActivityMapper {

  /**
   * Презентер модуля.
   */
  protected JepPresenter plainModulePresenter = null;

  public PlainActivityMapper(F clientFactory) {
    super(clientFactory);
  }

  public Activity getActivity(Place place) {
    if(plainModulePresenter == null) {
      plainModulePresenter = clientFactory.createPlainModulePresenter(place);
    } else {
      plainModulePresenter.setPlace(place);
    }
    return plainModulePresenter;
  }
}
