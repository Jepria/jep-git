package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class ShowHelpEvent extends BusEvent<ShowHelpEvent.ShowHelpHandler> {

  /**
   * Implemented by handlers of ShowHelpEvent.
   */
  public interface ShowHelpHandler extends EventHandler {
    /**
     * Called when a {@link ShowHelpEvent} is fired.
     *
     * @param event the {@link ShowHelpEvent}
     */
    void onShowHelp(ShowHelpEvent event);
  }

  /**
   * A singleton instance of Type&lt;ShowHelpHandler&gt;.
   */
  public static final Type<ShowHelpHandler> TYPE = new Type<ShowHelpHandler>();

  @Override
  public Type<ShowHelpHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(ShowHelpHandler handler) {
    handler.onShowHelp(this);
  }
}
