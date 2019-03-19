package com.technology.jep.jepria.client.widget.field.tree.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class RefreshStartEvent extends BusEvent<RefreshStartEvent.Handler> {
  public interface Handler extends EventHandler {
    void onRefreshStart(RefreshStartEvent event);
  }

  public RefreshStartEvent() {
  }
  
  public static final Type<Handler> TYPE = new Type<Handler>();

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onRefreshStart(this);
  }
}