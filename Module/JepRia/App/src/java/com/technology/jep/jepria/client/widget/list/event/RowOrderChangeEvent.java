package com.technology.jep.jepria.client.widget.list.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class RowOrderChangeEvent extends BusEvent<RowOrderChangeEvent.Handler> {

  /**
   * Implemented by handlers of RowOrderChangeEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link RowOrderChangeEvent} is fired.
     * 
     * @param event the {@link RowOrderChangeEvent}
     */
    void onRowOrderChange(RowOrderChangeEvent event);
  }

  /**
   * A singleton instance of Type&lt;RowOrderChangeHandler&gt;.
   */
  public static Type<Handler> TYPE;

  private final int oldIndex;
  private final int newIndex;
  private final boolean isAbove;

  public RowOrderChangeEvent(int oldIndex, int newIndex, boolean isAbove) {
    this.oldIndex = oldIndex;
    this.newIndex = newIndex;
    this.isAbove = isAbove;
  }

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<Handler> getType() {
    if (TYPE == null) {
      TYPE = new Type<Handler>();
    }
    return TYPE;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public int getOldIndex() {
    return this.oldIndex;
  }

  public int getNewIndex() {
    return this.newIndex;
  }
  
  public boolean isAbove() {
    return this.isAbove;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onRowOrderChange(this);
  }

  public static RowOrderChangeEvent fire(HasHandlers source, int oldIndex,
      int newIndex, boolean isAbove) {
    RowOrderChangeEvent event = new RowOrderChangeEvent(oldIndex, newIndex, isAbove);
    if (TYPE != null) {
      source.fireEvent(event);
    }
    return event;
  }
}