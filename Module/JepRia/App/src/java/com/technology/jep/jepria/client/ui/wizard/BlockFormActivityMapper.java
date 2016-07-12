package com.technology.jep.jepria.client.ui.wizard;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.JepActivityMapper;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.JepEventBus;

@SuppressWarnings("rawtypes")
public class BlockFormActivityMapper<F extends BlockClientFactory<?>>  
  extends JepActivityMapper<JepEventBus, F> implements ActivityMapper {

  /**
   * Презентер блока визарда.
   */
  protected JepPresenter presenter = null;

  public BlockFormActivityMapper(F clientFactory) {
    super(clientFactory);
  }

  public Activity getActivity(Place place) {
    if(presenter == null) {
      presenter = clientFactory.createPresenter(place);
    } else {
      presenter.setPlace(place);
    }
    return presenter;
  }
}
