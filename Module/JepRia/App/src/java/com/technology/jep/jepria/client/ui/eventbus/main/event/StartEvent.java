package com.technology.jep.jepria.client.ui.eventbus.main.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class StartEvent extends BusEvent<StartEvent.Handler> {

  /**
   * Implemented by handlers of StartEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link StartEvent} is fired.
     *
     * @param event the {@link StartEvent}
     */
    void onStart(StartEvent event);
  }

  /**
   * A singleton instance of Type&lt;StartHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onStart(this);
  }
}
