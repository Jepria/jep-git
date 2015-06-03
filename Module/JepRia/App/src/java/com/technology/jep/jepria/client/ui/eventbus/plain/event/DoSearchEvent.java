package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.ActionEvent;

public class DoSearchEvent extends ActionEvent<DoSearchEvent.Handler> {

  /**
   * Implemented by handlers of DoSearchEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link DoSearchEvent} is fired.
     *
     * @param event the {@link DoSearchEvent}
     */
    void onDoSearch(DoSearchEvent event);
  }

  /**
   * A singleton instance of Type&lt;DoSearchHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onDoSearch(this);
  }
}
