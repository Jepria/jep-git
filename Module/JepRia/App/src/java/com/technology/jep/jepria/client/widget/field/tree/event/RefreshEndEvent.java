package com.technology.jep.jepria.client.widget.field.tree.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class RefreshEndEvent extends BusEvent<RefreshEndEvent.Handler> {
  public interface Handler extends EventHandler {
    void onRefreshEnd(RefreshEndEvent event);
  }

  public RefreshEndEvent() {
  }
  
  public static final Type<Handler> TYPE = new Type<Handler>();

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onRefreshEnd(this);
  }
}