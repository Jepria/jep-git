package com.technology.jep.jepria.client.ui.eventbus.main.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class SetMainViewBodyEvent extends
  BusEvent<SetMainViewBodyEvent.Handler> {

  /**
   * Implemented by handlers of SetMainViewBodyEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link SetMainViewBodyEvent} is fired.
     * 
     * @param event the {@link SetMainViewBodyEvent}
     */
    void onSetMainViewBody(SetMainViewBodyEvent event);
  }

  /**
   * A singleton instance of Type&lt;SetMainViewBodyHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final Widget bodyWidget;

  public SetMainViewBodyEvent(Widget bodyWidget) {
    this.bodyWidget = bodyWidget;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public Widget getBody() {
    return bodyWidget;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onSetMainViewBody(this);
  }
}
