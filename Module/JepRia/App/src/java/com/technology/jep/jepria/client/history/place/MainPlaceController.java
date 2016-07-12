package com.technology.jep.jepria.client.history.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.message.JepMessageBoxImpl;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactory;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

/**
 * Класс переопределен для поддержки обработки использования пользователем кнопок Back/Forward браузера и для возможности захода в приложение
 * по сохраненному Url (для записи состояния приложения в адресную строку браузера и для восстановления состояния приложения по адресной строке).
 */
public class MainPlaceController<E extends MainEventBus, S extends JepMainServiceAsync, F extends MainClientFactory<E, S>> 
  extends JepPlaceController<E, F> {
  
  public MainPlaceController(E eventBus, F clientFactory) {
    super(eventBus, clientFactory);
  }

  /**
   * Перепишем метод из предположения, что он вызывается <u><b>ТОЛЬКО</b></u> при отработки History через браузер.<br/>
   * В прикладном и системном коде этот метод более никаким образом вызывается НЕ должен.
   *
   * @param newPlace новый Place, на который осуществляется переход
   */
  public void goTo(Place newPlace) {
    Log.trace(this.getClass() + ".goTo(" + newPlace + ")");
    
    try {
      if(JepScopeStack.instance.isUserEntered()) {
        clientFactory.getEventBus().enterFromHistory(newPlace);
      } else {
        super.goTo(newPlace);
        clientFactory.getEventBus().start();
      }
    } catch(Throwable th) {
      th.printStackTrace();
      JepMessageBoxImpl.instance.showError(th);
    }
  }

  /**
   * Записывает в строку браузера History Token соответствующий переданному Place'у.
   *
   * @param newPlace Place, History Token которого необходимо записать в строку браузера
   */
  public void writeHistory(Place newPlace) {
    super.goTo(newPlace);
  }

}
