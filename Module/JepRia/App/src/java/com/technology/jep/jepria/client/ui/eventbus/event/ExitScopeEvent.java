package com.technology.jep.jepria.client.ui.eventbus.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class ExitScopeEvent extends BusEvent<ExitScopeEvent.Handler> {

  /**
   * Implemented by handlers of ExitScopeEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link ExitScopeEvent} is fired.
     *
     * @param event the {@link ExitScopeEvent}
     */
    void onExitScope(ExitScopeEvent event);
  }

  /**
   * A singleton instance of Type&lt;ExitScopeHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onExitScope(this);
  }
}
