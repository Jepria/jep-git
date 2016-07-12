package com.technology.jep.jepria.client.ui.form.list;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.JepActivityMapper;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

public class ListFormActivityMapper<F extends StandardClientFactory<PlainEventBus, JepDataServiceAsync>>  
  extends JepActivityMapper<PlainEventBus, F> implements ActivityMapper {

  /**
   * Презентер детальной формы.
   */
  protected JepPresenter listFormPresenter = null;

  public ListFormActivityMapper(F clientFactory) {
    super(clientFactory);
  }

  public Activity getActivity(Place place) {
    if(listFormPresenter == null) {
      listFormPresenter = clientFactory.createListFormPresenter(place);
    } else {
      listFormPresenter.setPlace(place);
    }
    return listFormPresenter;
  }
}
