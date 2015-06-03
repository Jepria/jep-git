package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.ActionEvent;

public class SaveEvent extends ActionEvent<SaveEvent.Handler> {

  /**
   * Implemented by handlers of SaveEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link SaveEvent} is fired.
     *
     * @param event the {@link SaveEvent}
     */
    void onSave(SaveEvent event);
  }

  /**
   * A singleton instance of Type&lt;SaveHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onSave(this);
  }
}
