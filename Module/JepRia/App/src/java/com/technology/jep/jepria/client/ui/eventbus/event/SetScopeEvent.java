package com.technology.jep.jepria.client.ui.eventbus.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.history.scope.JepScope;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class SetScopeEvent extends
    BusEvent<SetScopeEvent.SetScopeHandler> {

  /**
   * Implemented by handlers of SetScopeEvent.
   */
  public interface SetScopeHandler extends EventHandler {
    /**
     * Called when a {@link SetScopeEvent} is fired.
     * 
     * @param event
     *            the {@link SetScopeEvent}
     */
    void onSetScope(SetScopeEvent event);
  }

  /**
   * A singleton instance of Type&lt;SetScopeHandler&gt;.
   */
  public static final Type<SetScopeHandler> TYPE = new Type<SetScopeHandler>();
  
  private final JepScope scope;

  public SetScopeEvent(JepScope scope) {
    this.scope = scope;
  }

  @Override
  public Type<SetScopeHandler> getAssociatedType() {
    return TYPE;
  }

  public JepScope getScope() {
    return scope;
  }

  @Override
  protected void dispatch(SetScopeHandler handler) {
    handler.onSetScope(this);
  }
}
