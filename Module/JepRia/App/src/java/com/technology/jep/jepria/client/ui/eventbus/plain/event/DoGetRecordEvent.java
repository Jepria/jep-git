package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;
import com.technology.jep.jepria.shared.load.PagingConfig;

public class DoGetRecordEvent extends
    BusEvent<DoGetRecordEvent.Handler> {

  /**
   * Implemented by handlers of DoGetRecordEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link DoGetRecordEvent} is fired.
     * 
     * @param event the {@link DoGetRecordEvent}
     */
    void onDoGetRecord(DoGetRecordEvent event);
  }

  /**
   * A singleton instance of Type&lt;DoGetRecordHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final PagingConfig pagingConfig;

  public DoGetRecordEvent(PagingConfig pagingConfig) {
    this.pagingConfig = pagingConfig;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public PagingConfig getPagingConfig() {
    return pagingConfig;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onDoGetRecord(this);
  }
}
