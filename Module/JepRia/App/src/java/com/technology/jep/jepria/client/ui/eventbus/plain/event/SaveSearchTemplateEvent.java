package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.record.JepRecord;

public class SaveSearchTemplateEvent extends
    BusEvent<SaveSearchTemplateEvent.Handler> {

  /**
   * Implemented by handlers of SearchEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link SearchEvent} is fired.
     * 
     * @param event
     *            the {@link SearchEvent}
     */
    void onSaveSearchTemplate(SaveSearchTemplateEvent event);
  }

  /**
   * A singleton instance of Type&lt;SearchHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final JepRecord searchTemplate;

  public SaveSearchTemplateEvent(JepRecord searchTemplate) {
    this.searchTemplate = searchTemplate;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public JepRecord getSearchTemplate() {
    return searchTemplate;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onSaveSearchTemplate(this);
  }
}
