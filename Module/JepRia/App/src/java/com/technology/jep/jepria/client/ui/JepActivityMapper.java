package com.technology.jep.jepria.client.ui;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;

public abstract class JepActivityMapper<E extends EventBus, F extends  ClientFactory<E>> implements ActivityMapper {

  protected F clientFactory;

  public JepActivityMapper(F clientFactory) {
    this.clientFactory = clientFactory;
  }
  
}
