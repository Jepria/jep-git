package com.technology.jep.jepria.client.widget.field.multistate.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent.CheckChangeHandler;
import com.technology.jep.jepria.shared.field.option.JepOption;

/**
 * Handler class for {@link CheckChangeEvent} events.
 */
public class CheckChangeEvent<V extends JepOption> extends CancellableEventImpl<CheckChangeHandler<V>> {

  private V selectedOption; 
  private boolean selected; 
  
  /**
   * Handler class for {@link CheckChangeEvent} events.
   */
  public interface CheckChangeHandler<V extends JepOption> extends EventHandler {
    void onCheckChange(CheckChangeEvent<V> event);
  }

  /**
   * A widget that implements this interface is a public source of
   * {@link CheckChangeEvent} events.
   */
  public interface HasCheckChangeHandlers<V extends JepOption> {

    /**
     * Adds a {@link CheckChangeHandler} handler for
     * {@link CheckChangeEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addCheckChangeHandler(CheckChangeHandler<V> handler);
  }
  
  public CheckChangeEvent(V option, boolean selected){
    this.selectedOption = option;
    this.selected = selected;
  }
  
  /**
   * Handler type.
   */
  private static Type<CheckChangeHandler<?>> TYPE = new Type<CheckChangeHandler<?>>();

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<CheckChangeHandler<?>> getType() {
    return TYPE;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Type<CheckChangeHandler<V>> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  protected void dispatch(CheckChangeHandler<V> handler) {
    handler.onCheckChange(this);
  }

  public V getSelectedOption() {
    return selectedOption;
  }

  public boolean isSelected() {
    return selected;
  }
}
