package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class ListEvent extends BusEvent<ListEvent.Handler> {

  /**
   * Implemented by handlers of ListEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link ListEvent} is fired.
     *
     * @param event the {@link ListEvent}
     */
    void onList(ListEvent event);
  }

  /**
   * A singleton instance of Type&lt;ListHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onList(this);
  }
}
