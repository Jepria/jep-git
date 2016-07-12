package com.technology.jep.jepria.client.ui.main;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;

/**
 * Класс создан для фильтрации PlaceChange-событий для JepWorkstatePlace, "приходящих" со стороны подчинённых модулей.
 */
public class MainActivityManager extends ActivityManager {

  public MainActivityManager(ActivityMapper mapper, EventBus eventBus) {
    super(mapper, eventBus);
  }

  private boolean isFirstTime = true;
  public void onPlaceChange(PlaceChangeEvent event) {
    Log.trace(this.getClass() + ".onPlaceChange(PlaceChangeEvent event): newPlace = " + event.getNewPlace());
    if(isFirstTime) { 
      super.onPlaceChange(event);
      isFirstTime = false;
    }
  }
  
}
