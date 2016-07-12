package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class SetListUIDEvent extends BusEvent<SetListUIDEvent.Handler> {

  public interface Handler extends EventHandler {
    void onSetListUID(SetListUIDEvent event);
  }
  
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final Integer listUID;

  public SetListUIDEvent(Integer uid) {
    this.listUID = uid;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }
  
  public Integer getListUID() {
    return listUID;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onSetListUID(this);
  }
}
