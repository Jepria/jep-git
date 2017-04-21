package com.technology.jep.jepria.client.widget.list.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

public class RowPositionChangeEvent extends BusEvent<RowPositionChangeEvent.Handler> {

	/**
	 * Implemented by handlers of RowPositionChangeEvent.
	 */
	public interface Handler extends EventHandler {
		/**
		 * Called when a {@link RowPositionChangeEvent} is fired.
		 * 
		 * @param event the {@link RowPositionChangeEvent}
		 */
		void onRowPositionChange(RowPositionChangeEvent event);
	}

	/**
	 * A singleton instance of Type&lt;RowPositionChangeHandler&gt;.
	 */
	public static Type<Handler> TYPE;

	private final List<Object> oldRowList;
	private final int newIndex;
	private final boolean isOver;
	private final boolean insertBefore;
	private final boolean insertAfter;
	
	public RowPositionChangeEvent(List<Object> oldRowList, int newIndex, boolean isOver, boolean insertBefore, boolean insertAfter) {
		this.oldRowList = oldRowList;
		this.newIndex = newIndex;
		this.isOver = isOver;
		this.insertBefore = insertBefore;
		this.insertAfter = insertAfter;
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

	public List<Object> getOldRowList() {
		return this.oldRowList;
	}

	public int getNewIndex() {
		return this.newIndex;
	}

	public boolean isOver() {
		return this.isOver;
	}

	public boolean isInsertBefore() {
		return insertBefore;
	}

	public boolean isInsertAfter() {
		return insertAfter;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onRowPositionChange(this);
	}

	public static RowPositionChangeEvent fire(HasHandlers source, List<Object> oldRowList,
			int newIndex, boolean isOver, boolean insertBefore, boolean insertAfter) {
		RowPositionChangeEvent event = new RowPositionChangeEvent(oldRowList, newIndex, isOver, insertBefore, insertAfter);
		if (TYPE != null) {
			source.fireEvent(event);
		}
		return event;
	}
}