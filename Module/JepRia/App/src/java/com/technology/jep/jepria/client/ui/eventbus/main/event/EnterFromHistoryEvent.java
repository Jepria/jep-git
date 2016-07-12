package com.technology.jep.jepria.client.ui.eventbus.main.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class EnterFromHistoryEvent extends
    BusEvent<EnterFromHistoryEvent.Handler> {

  /**
   * Implemented by handlers of EnterFromHistoryEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link EnterFromHistoryEvent} is fired.
     * 
     * @param event
     *            the {@link EnterFromHistoryEvent}
     */
    void onEnterFromHistory(EnterFromHistoryEvent event);
  }

  /**
   * A singleton instance of Type&lt;Handler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final Place place;

  public EnterFromHistoryEvent(Place place) {
    this.place = place;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public Place getPlace() {
    return place;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onEnterFromHistory(this);
  }
}
