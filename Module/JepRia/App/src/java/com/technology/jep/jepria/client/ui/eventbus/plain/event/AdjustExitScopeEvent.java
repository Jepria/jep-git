package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class AdjustExitScopeEvent extends BusEvent<AdjustExitScopeEvent.Handler> {

  /**
   * Implemented by handlers of AdjustExitScopeEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link AdjustExitScopeEvent} is fired.
     *
     * @param event the {@link AdjustExitScopeEvent}
     */
    void onAdjustExitScope(AdjustExitScopeEvent event);
  }

  /**
   * A singleton instance of Type&lt;Handler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onAdjustExitScope(this);
  }
}
