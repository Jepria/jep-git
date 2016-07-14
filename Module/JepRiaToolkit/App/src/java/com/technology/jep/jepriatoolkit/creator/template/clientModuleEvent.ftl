package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus.event;
 
import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;
 
public class ${button.customEvent}Event extends BusEvent<${button.customEvent}Event.Handler> {
 
  public interface Handler extends EventHandler {
    void on${button.customEvent}Event(${button.customEvent}Event event);
  }
 
  public static final Type<Handler> TYPE = new Type<Handler>();
 
  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }
 
  @Override
  protected void dispatch(Handler handler) {
    handler.on${button.customEvent}Event(this);
  }
}
