package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class SetSaveButtonEnabledEvent extends BusEvent<SetSaveButtonEnabledEvent.Handler> {

  /**
   * Implemented by handlers of SetSaveButtonEnabledEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link SetSaveButtonEnabledEvent} is fired.
     *
     * @param event the {@link SetSaveButtonEnabledEvent}
     */
    void onSetSaveButtonEnabled(SetSaveButtonEnabledEvent event);
  }

  /**
   * A singleton instance of Type&lt;Handler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final boolean enabled;
  
  public SetSaveButtonEnabledEvent(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }
  
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onSetSaveButtonEnabled(this);
  }
}
