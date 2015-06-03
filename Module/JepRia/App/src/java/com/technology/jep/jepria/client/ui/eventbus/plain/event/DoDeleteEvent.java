package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.ActionEvent;

public class DoDeleteEvent extends ActionEvent<DoDeleteEvent.Handler> {

  /**
   * Implemented by handlers of DoDeleteEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link DoDeleteEvent} is fired.
     *
     * @param event the {@link DoDeleteEvent}
     */
    void onDoDelete(DoDeleteEvent event);
  }

  /**
   * A singleton instance of Type&lt;DoDeleteHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onDoDelete(this);
  }
}
