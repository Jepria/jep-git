package com.technology.jep.jepria.client.ui.statusbar;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.JepActivityMapper;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

public class StatusBarActivityMapper<F extends StandardClientFactory<PlainEventBus, JepDataServiceAsync>>  
  extends JepActivityMapper<PlainEventBus, F> implements ActivityMapper {

  /**
   * Презентер панели состояния.
   */
  protected JepPresenter statusBarPresenter = null;

  public StatusBarActivityMapper(F clientFactory) {
    super(clientFactory);
  }

  public Activity getActivity(Place place) {
    if(statusBarPresenter == null) {
      statusBarPresenter = clientFactory.createStatusBarPresenter(place);
    } else {
      statusBarPresenter.setPlace(place);
    }
    return statusBarPresenter;
  }
}
