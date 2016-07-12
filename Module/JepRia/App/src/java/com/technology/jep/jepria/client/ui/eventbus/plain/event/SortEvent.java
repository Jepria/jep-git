package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;
import com.technology.jep.jepria.shared.load.SortConfig;

public class SortEvent extends BusEvent<SortEvent.Handler> {

  /**
   * Implemented by handlers of SortEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link SortEvent} is fired.
     * 
     * @param event the {@link SortEvent}
     */
    void onSort(SortEvent event);
  }

  /**
   * A singleton instance of Type&lt;SortHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final SortConfig sortConfig;

  public SortEvent(SortConfig sortConfig) {
    this.sortConfig = sortConfig;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public SortConfig getSortConfig() {
    return sortConfig;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onSort(this);
  }
}
