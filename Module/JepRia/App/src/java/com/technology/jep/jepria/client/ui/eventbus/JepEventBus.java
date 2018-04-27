package com.technology.jep.jepria.client.ui.eventbus;

import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.history.place.JepViewListPlace;
import com.technology.jep.jepria.client.history.scope.JepScope;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.eventbus.event.EnterModuleEvent;
import com.technology.jep.jepria.client.ui.eventbus.event.SetScopeEvent;
import com.technology.jep.jepria.client.ui.eventbus.event.UpdateScopeEvent;

public class JepEventBus extends ResettableEventBus {

  public JepEventBus() {
    super(new SimpleEventBus());
  }

  public void updateScope(UpdateScopeEvent updateScopeEvent) {
    fireEvent(updateScopeEvent);
  }

  public void setScope(JepScope scope) {
    fireEvent(new SetScopeEvent(scope));
  }
  
  /**
   * Событие входа в модуль. Метод с одним параметром для обратной совместимости.
   * @param moduleId Идентификатор модуля.
   */
  public void enterModule(String moduleId) {
    enterModule(moduleId, false);
  }
  
  /**
   * Событие входа в модуль.
   * @param moduleId Идентификатор модуля.
   * @param detectPlace если true, то будет автоматически определенно место входа в модуль,
   * иначе будет то состояние, в котором модуль остался на момент выхода из него.
   */
  public void enterModule(String moduleId, Boolean detectPlace) {
    
    Place place = null;
    
    if (Boolean.TRUE.equals(detectPlace)) {
      // Автоопределение места входа в модуль.
      // Актуально при нажатии кнопки UP или при нажатии на табы:
      // если переход не в главный модуль скопа, то переход в состояние списка,
      // если в главный - то переход в то состояние, в котором остался главный модуль.
      // (В стандартном случае при использовании кнопки UP, перехода в главный модуль не будет,
      // ранее код был в методе onEnterModule при любых межмодульных переходах).
      JepScope scope = JepScopeStack.instance.peek();
      place = scope.isMain(moduleId) ? null : new JepViewListPlace();
    }
    
    enterModule(moduleId, place);
  }

  /**
   * Событие входа в модуль в определенное место входа.
   * @param moduleId Идентификатор модуля.
   * @param place Место входа.
   */
  public void enterModule(String moduleId, Place place) {
    fireEvent(new EnterModuleEvent(moduleId, place));
  }
}
