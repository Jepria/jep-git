package com.technology.jep.jepria.client.widget.field.multistate.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Cancellable Event Type
 * If you need to stop the event propagation you should this event type.
 * @param <H> event handler
 */
public abstract class CancellableEventImpl<H extends EventHandler> extends GwtEvent<H> implements CancellableEvent {

  /**
   * The flag specify the need to stop propagation of event 
   */
  private boolean cancelled = false;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

}
