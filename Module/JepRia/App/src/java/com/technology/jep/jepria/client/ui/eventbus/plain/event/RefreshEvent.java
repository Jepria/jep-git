package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class RefreshEvent extends BusEvent<RefreshEvent.Handler> {

  /**
   * Implemented by handlers of RefreshEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link RefreshEvent} is fired.
     *
     * @param event the {@link RefreshEvent}
     */
    void onRefresh(RefreshEvent event);
  }

  /**
   * A singleton instance of Type&lt;RefreshHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onRefresh(this);
  }
}
