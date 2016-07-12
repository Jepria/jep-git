package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;
import com.technology.jep.jepria.shared.record.JepRecord;

public class DeleteEvent extends
    BusEvent<DeleteEvent.Handler> {

  /**
   * Implemented by handlers of DeleteEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link DeleteEvent} is fired.
     * 
     * @param event
     *            the {@link DeleteEvent}
     */
    void onDelete(DeleteEvent event);
  }

  /**
   * A singleton instance of Type&lt;DeleteHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final JepRecord record;

  public DeleteEvent(JepRecord record) {
    this.record = record;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public JepRecord getRecord() {
    return record;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onDelete(this);
  }
}
