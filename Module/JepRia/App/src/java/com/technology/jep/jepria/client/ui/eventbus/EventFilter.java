package com.technology.jep.jepria.client.ui.eventbus;

import com.google.gwt.event.shared.EventBus;

public interface EventFilter<E extends EventBus> {

  public boolean checkEvent(BusEvent<?> event);
}
