package com.technology.jep.jepria.client.ui.eventbus.main.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class SetMainViewEvent extends
  BusEvent<SetMainViewEvent.Handler> {

  /**
   * Implemented by handlers of SetMainViewEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link SetMainViewEvent} is fired.
     * 
     * @param event the {@link SetMainViewEvent}
     */
    void onSetMainView(SetMainViewEvent event);
  }

  /**
   * A singleton instance of Type&lt;SetMainViewHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final IsWidget mainView;

  public SetMainViewEvent(IsWidget mainView) {
    this.mainView = mainView;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public IsWidget getMainView() {
    return mainView;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onSetMainView(this);
  }
  
}
