package com.technology.jep.jepria.client.history.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.History;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * Класс создан для поддержки безопасности переходов на Places посредством перехвата вызово goTo(newPlace).
 */
public class PlainPlaceController<E extends PlainEventBus, S extends JepDataServiceAsync, F extends PlainClientFactory<E, S>> 
  extends JepPlaceController<E, F> {
  
  public PlainPlaceController(
      E eventBus,
      F clientFactory) {
    super(eventBus, clientFactory);
  }

  private static String currentHistory = null;
  
  private boolean writeHistory = true;
  
  public void goTo(Place newPlace) {
    Log.trace(this.getClass() + ".goTo: newPlace = " + newPlace + ", getWhere() = " + getWhere() + ", History.getToken() = " + History.getToken());
    
    // TODO: рефакторить: да, надежность и предотвращение двойной записи в историю, но performance полного сравнения стека каждый раз сомнителен.
    String newHistory = JepScopeStack.instance.toHistoryToken((JepWorkstatePlace)newPlace);
    
    if(!newHistory.equals(currentHistory)) {
      super.goTo(newPlace);
      currentHistory = newHistory;
      
      // Если необходимо писать историю и новое состояние НЕ является JepSelectedPlace (состояние JepSelectedPlace, в текущей версии, 
      // не пишем в историю, т.к. пока не реализовано полноценное восстановление приложения из данного состояния), то ...
      if(writeHistory && !(newPlace instanceof JepSelectedPlace)) {
        // Отобразим History в адресной строке браузера.
        MainPlaceController mainPlaceController = ((PlainClientFactory<?, ?>)clientFactory).getMainClientFactory().getPlaceController();
        mainPlaceController.writeHistory(newPlace);
      }
    }
    
  }
  
  public void setWriteHistory(boolean writeHistory) {
    this.writeHistory = writeHistory;
  }
  
}
