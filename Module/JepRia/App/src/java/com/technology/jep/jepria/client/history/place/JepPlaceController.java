package com.technology.jep.jepria.client.history.place;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.technology.jep.jepria.client.ui.ClientFactory;

/**
 * Класс создан для поддержки безопасности переходов на Places посредством перехвата вызово goTo(newPlace).
 */
public class JepPlaceController<E extends EventBus, F extends ClientFactory<E>> extends PlaceController {
  
  protected F clientFactory;

  public JepPlaceController(EventBus eventBus, F clientFactory) {
    super(eventBus);
    this.clientFactory = clientFactory;
  }

}
