package com.technology.jep.jepria.client.widget.field.multistate.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.technology.jep.jepria.client.widget.field.multistate.event.AfterExpandEvent.AfterExpandHandler;

public class AfterExpandEvent extends GwtEvent<AfterExpandHandler> {

  /**
   * Handler class for {@link AfterExpandEvent} events.
   */
  public interface AfterExpandHandler extends EventHandler {

    void onAfterExpand(AfterExpandEvent event);
  }

  /**
   * A widget that implements this interface is a public source of
   * {@link AfterExpandEvent} events.
   */
  public interface HasAfterExpandHandlers {

    /**
     * Adds a {@link AfterExpandHandler} handler for
     * {@link AfterExpandEvent} events.
     * 
     * @param handler
     *            the handler
     * @return the registration for the event
     */
    HandlerRegistration addAfterExpandHandler(AfterExpandHandler handler);
  }

  /**
   * Handler type.
   */
  private static Type<AfterExpandHandler> TYPE = new Type<AfterExpandHandler>();

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<AfterExpandHandler> getType() {
    return TYPE;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Type<AfterExpandHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  protected void dispatch(AfterExpandHandler handler) {
    handler.onAfterExpand(this);
  }

}
