package com.technology.jep.jepria.client.ui.eventbus;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Событие шины (EventBus).
 */
abstract public class BusEvent<H extends EventHandler> extends GwtEvent<H> {

  /**
   * Как правило, метод должен перекрыватся потомками.
   * 
   * @return имя события, отображаемое в сообщениях, логах, ...
   */
  public String getDisplayName() {
    return this.getClass().toString();
  }

}
