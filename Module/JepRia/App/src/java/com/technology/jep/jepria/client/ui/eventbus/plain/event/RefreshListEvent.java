package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class RefreshListEvent extends BusEvent<RefreshListEvent.Handler> {

  /**
   * Implemented by handlers of RefreshEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link RefreshListEvent} is fired.
     *
     * @param event the {@link RefreshListEvent}
     */
    void onRefreshList(RefreshListEvent event);
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
    handler.onRefreshList(this);
  }
}
