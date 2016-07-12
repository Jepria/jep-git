package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;
import com.technology.jep.jepria.shared.load.PagingConfig;

public class PagingEvent extends BusEvent<PagingEvent.Handler> {

  /**
   * Implemented by handlers of PagingEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link PagingEvent} is fired.
     * 
     * @param event the {@link PagingEvent}
     */
    void onPaging(PagingEvent event);
  }

  /**
   * A singleton instance of Type&lt;PagingHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final PagingConfig pagingConfig;

  public PagingEvent(PagingConfig pagingConfig) {
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
    handler.onPaging(this);
  }
}
