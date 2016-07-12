package com.technology.jep.jepria.client.history.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.technology.jep.jepria.client.ui.eventbus.JepEventBus;
import com.technology.jep.jepria.client.ui.wizard.BlockClientFactory;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

public class BlockPlaceController<E extends JepEventBus, S extends JepDataServiceAsync, F extends BlockClientFactory<S>> 
  extends PlaceController {
  
  protected F clientFactory;
  
  public BlockPlaceController(
      E eventBus,
      F clientFactory) {
    super(eventBus);
    this.clientFactory = clientFactory;
  }
  
  protected Place _place;
  
  public void goTo(Place newPlace) {
    if(newPlace != null && !newPlace.equals(_place)) {
      super.goTo(newPlace);
      _place = newPlace;
    }
  }
}
