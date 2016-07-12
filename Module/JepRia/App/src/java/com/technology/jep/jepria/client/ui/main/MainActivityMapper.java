package com.technology.jep.jepria.client.ui.main;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.JepActivityMapper;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

public class MainActivityMapper<F extends MainClientFactory<MainEventBus, JepMainServiceAsync>>  
  extends JepActivityMapper<MainEventBus, F> implements ActivityMapper {

  /**
   * Главный презентер приложения.
   */
  protected Activity mainModulePresenter = null;

  public MainActivityMapper(F clientFactory) {
    super(clientFactory);
  }

  public Activity getActivity(Place place) {
    if(mainModulePresenter == null) {
      mainModulePresenter = clientFactory.createMainModulePresenter();
    }
    return mainModulePresenter;
  }
}
