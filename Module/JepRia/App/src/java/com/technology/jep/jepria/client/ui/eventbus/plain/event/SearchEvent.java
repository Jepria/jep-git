package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;
import com.technology.jep.jepria.shared.load.PagingConfig;

public class SearchEvent extends
    BusEvent<SearchEvent.Handler> {

  /**
   * Implemented by handlers of SearchEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link SearchEvent} is fired.
     * 
     * @param event
     *            the {@link SearchEvent}
     */
    void onSearch(SearchEvent event);
  }

  /**
   * A singleton instance of Type&lt;SearchHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final PagingConfig pagingConfig;

  public SearchEvent(PagingConfig pagingConfig) {
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
    handler.onSearch(this);
  }
}
