package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;
import com.technology.jep.jepria.shared.record.JepRecord;

public class SetCurrentRecordEvent extends
    BusEvent<SetCurrentRecordEvent.Handler> {

  /**
   * Implemented by handlers of SetNewRecordEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link SetCurrentRecordEvent} is fired.
     * 
     * @param event
     *            the {@link SetCurrentRecordEvent}
     */
    void onSetCurrentRecord(SetCurrentRecordEvent event);
  }

  /**
   * A singleton instance of Type&lt;SetNewRecordHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final JepRecord currentRecord;

  public SetCurrentRecordEvent(JepRecord newCurrentRecord) {
    this.currentRecord = newCurrentRecord;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }
  
  public JepRecord getCurrentRecord() {
    return currentRecord;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onSetCurrentRecord(this);
  }
}
