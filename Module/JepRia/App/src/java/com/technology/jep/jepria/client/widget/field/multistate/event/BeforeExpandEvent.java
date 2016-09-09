package com.technology.jep.jepria.client.widget.field.multistate.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeExpandEvent.BeforeExpandHandler;

/**
 * Event type for widgets that can be expanded.
 */
public class BeforeExpandEvent extends CancellableEventImpl<BeforeExpandHandler> {

  /**
   * Handler class for {@link BeforeExpandEvent} events.
   */
  public interface BeforeExpandHandler extends EventHandler {

    /**
     * Called before a content panel is expanded. Listeners can cancel the action
     * by calling {@link BeforeExpandEvent#setCancelled(boolean)}.
     */
    void onBeforeExpand(BeforeExpandEvent event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeExpandEvent} events.
   */
  public interface HasBeforeExpandHandlers {

    /**
     * Adds a {@link BeforeExpandHandler} handler for {@link BeforeExpandEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeExpandHandler(BeforeExpandHandler handler);
  }
  
  /**
   * Handler type.
   */
  private static Type<BeforeExpandHandler> TYPE = new Type<BeforeExpandHandler>();

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeExpandHandler> getType() {
    return TYPE;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<BeforeExpandHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  protected void dispatch(BeforeExpandHandler handler) {
    handler.onBeforeExpand(this);
  }

}
