package com.technology.jep.jepria.client.widget.field.multistate.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeSelectEvent.BeforeSelectHandler;

/**
 * Event type for widgets that can be selected.
 */
public class BeforeSelectEvent<T> extends CancellableEventImpl<BeforeSelectHandler<T>> {

  /**
   * Handler class for {@link BeforeSelectEvent} events.
   */
  public interface BeforeSelectHandler<T> extends EventHandler {

    /**
     * Called before a content panel is selected. Listeners can cancel the
     * action by calling {@link BeforeSelectEvent#setCancelled(boolean)}.
     */
    void onBeforeSelect(BeforeSelectEvent<T> event);
  }

  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeSelectEvent} events.
   */
  public interface HasBeforeSelectHandlers<T> {

    /**
     * Adds a {@link BeforeSelectHandler} handler for
     * {@link BeforeSelectEvent} events.
     * 
     * @param handler
     *            the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeSelectHandler(BeforeSelectHandler<T> handler);
  }

  /**
   * Handler type.
   */
  private static Type<BeforeSelectHandler<?>> TYPE = new Type<BeforeSelectHandler<?>>();

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeSelectHandler<?>> getType() {
    return TYPE;
  }

  private T selectedItem = null;

  public BeforeSelectEvent(T option){
    setSelectedItem(option);
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Type<BeforeSelectHandler<T>> getAssociatedType() {
    return (Type) TYPE;
  }

  public T getSelectedItem() {
    return selectedItem;
  }

  public void setSelectedItem(T selectedItem) {
    this.selectedItem = selectedItem;
  }

  @Override
  protected void dispatch(BeforeSelectHandler<T> handler) {
    handler.onBeforeSelect(this);
  }

}
