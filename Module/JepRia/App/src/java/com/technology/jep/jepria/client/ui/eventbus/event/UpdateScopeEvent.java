package com.technology.jep.jepria.client.ui.eventbus.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.history.scope.JepScope;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class UpdateScopeEvent extends BusEvent<UpdateScopeEvent.Handler> {

  /**
   * Implemented by handlers of UpdateScopeEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link UpdateScopeEvent} is fired.
     *
     * @param event the {@link UpdateScopeEvent}
     */
    void onUpdateScope(UpdateScopeEvent event);
  }

  /**
   * A singleton instance of Type&lt;UpdateScopeHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private JepScope scope = null;

  public JepScope getScope() {
  return scope;
  }

  public UpdateScopeEvent(JepScope scope) {
    this.scope = scope;
  }

@Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onUpdateScope(this);
  }
}
