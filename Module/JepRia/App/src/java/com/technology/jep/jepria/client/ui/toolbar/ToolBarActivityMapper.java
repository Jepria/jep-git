package com.technology.jep.jepria.client.ui.toolbar;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.JepActivityMapper;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

public class ToolBarActivityMapper<F extends StandardClientFactory<PlainEventBus, JepDataServiceAsync>>  
  extends JepActivityMapper<PlainEventBus, F> implements ActivityMapper {

  /**
   * Презентер инструментальной панели.
   */
  protected JepPresenter toolBarPresenter = null;

  public ToolBarActivityMapper(F clientFactory) {
    super(clientFactory);
  }

  public Activity getActivity(Place place) {
    if(toolBarPresenter == null) {
      toolBarPresenter = clientFactory.createToolBarPresenter(place);
    } else {
      toolBarPresenter.setPlace(place);
    }
    return toolBarPresenter;
  }
}
