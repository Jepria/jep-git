package com.technology.jep.jepria.client.widget.list.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent;

public class ColumnWidthChangeEvent extends GwtEvent<ColumnWidthChangeEvent.Handler> {
   public static interface Handler extends EventHandler {

        /**
         * Called when {@link ColumnSortEvent} is fired.
         * 
         * @param event the {@link ColumnSortEvent} that was fired
         */
        void onColumnSort(ColumnSortEvent event);
      }

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void dispatch(Handler handler) {
    // TODO Auto-generated method stub
    
  }
}
