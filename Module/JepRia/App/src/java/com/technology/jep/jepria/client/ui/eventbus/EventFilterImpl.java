package com.technology.jep.jepria.client.ui.eventbus;

import com.google.gwt.event.shared.EventBus;
import com.technology.jep.jepria.client.ui.ClientFactory;

public class EventFilterImpl implements EventFilter<EventBus> {
	private ClientFactory<?> clientFactory;
	
	public EventFilterImpl(ClientFactory<?> clientFactory) {
		this.clientFactory = clientFactory;
	}

	public boolean checkEvent(BusEvent<?> event)	{
		return clientFactory.getUiSecurity().checkEvent(event);
	}
}
