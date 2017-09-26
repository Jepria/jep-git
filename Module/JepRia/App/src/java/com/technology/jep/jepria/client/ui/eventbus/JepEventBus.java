package com.technology.jep.jepria.client.ui.eventbus;

import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.history.scope.JepScope;
import com.technology.jep.jepria.client.ui.ClientFactory;
import com.technology.jep.jepria.client.ui.eventbus.event.EnterModuleEvent;
import com.technology.jep.jepria.client.ui.eventbus.event.SetScopeEvent;
import com.technology.jep.jepria.client.ui.eventbus.event.UpdateScopeEvent;

public class JepEventBus extends ResettableEventBus {
  /**
   * Клиентская фабрика.
   */
  private ClientFactory<?> clientFactory;
  
  public JepEventBus(ClientFactory<?> clientFactory) {
    super(new SimpleEventBus());
    this.clientFactory = clientFactory;
  }

  protected void checkAndFireEvent(BusEvent<?> event) {
    if(clientFactory.getEventFilter().checkEvent(event)) {
      fireEvent(event);
    }
  }

  public void updateScope(UpdateScopeEvent updateScopeEvent) {
    fireEvent(updateScopeEvent);
  }

  public void setScope(JepScope scope) {
    fireEvent(new SetScopeEvent(scope));
  }
  
  public void enterModule(String moduleId) {
    checkAndFireEvent(new EnterModuleEvent(moduleId));
  }

  public void enterModule(String moduleId, Place place) {
    checkAndFireEvent(new EnterModuleEvent(moduleId, place));
  }
  
}
