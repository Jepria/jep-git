package com.technology.jep.jepria.client.ui.statusbar;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

public class StatusBarPresenter<V extends StatusBarView, E extends PlainEventBus, S extends JepDataServiceAsync, 
    F extends StandardClientFactory<E, S>>
  extends JepPresenter<E, F> {
  
  protected V view;
  
  public StatusBarPresenter(Place place, F clientFactory) {
    super(place, clientFactory);
    
    view = (V)clientFactory.getStatusBarView();
  }
  
  public void start(AcceptsOneWidget container, EventBus eventBus) {
    // Подписка activity-презентера на события EventBus.
    
    // "Привязка" элементов представления к функционалу презентера.
    bind();
    // Переведем презентер модуля в заданный режим.
    changeWorkstate(place);
  }
  
  /**
   * Метод используется для перекрытия потомками с целью "привязки" элементов представления к функционалу презентера.
   */
  protected void bind() {
    view.setPresenter(this);
  }
  
  protected void onChangeWorkstate(WorkstateEnum workstate) {
    view.showWorkstate(workstate);
  }
  
}
