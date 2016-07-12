package com.technology.jep.jepria.client.ui.plain;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.event.EnterModuleEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

public class StandardModulePresenter<V extends StandardModuleView, E extends PlainEventBus, S extends JepDataServiceAsync, 
    F extends StandardClientFactory<E, S>>
  extends PlainModulePresenter<V, E, S, F> {
  
  public StandardModulePresenter(String moduleId, Place place, F clientFactory) {
    super(moduleId, place, clientFactory);
  }

  public void start(AcceptsOneWidget container, EventBus eventBus) {
    // Подписка activity-презентера на события EventBus.


    // Подписка activity-презентера на события EventBus ("подписка" родителя).
    // Перевод презентер модуля в заданный режим.
    super.start(container, eventBus);
  }

  /**
   * Метод используется для перекрытия потомками с целью "привязки" элементов представления к функционалу презентера.
   */
  protected void bind() {
    super.bind();
    
    setHeader();
    setFooter();
  }

  /**
   * Установка виджета в области инструментальной панели.
   */
  protected void setHeader() {
    view.setHeader(clientFactory.getToolBarView().asWidget());
  }

  /**
   * Установка виджета в области панели состояния.
   */
  protected void setFooter() {
    view.setFooter(clientFactory.getStatusBarView().asWidget());
  }

  /**
   * Обработчик события перехода на модуль.
   *
   * @param event событие перехода на модуль
   */
  public void onEnterModule(EnterModuleEvent event) {
    super.onEnterModule(event);
    
    Log.trace(this.getClass() + ".onEnterModule: moduleId = " + event.getModuleId());
    
    // Установим содержимое главного виджета(-контейнера) приложения.
    setMainViewBody();
  }
  
  /**
   * Установка виджета(-контейнера) стандартного модуля.<br/>
   * В методе используется вызов вида : <code>mainEventBus.setMainViewBody(view.asWidget());</code> <br/>
   * При этом, при передаче <code>null</code> в качестве виджета стандартного модуля, текущий виджет модуля удаляется из MainView.<br/>
   * Т.о., перегрузкой данного метода можно установить, при заходе на модуль приложения, любой виджет модуля или скрыть текущий.
   */
  protected void setMainViewBody() {
    mainEventBus.setMainViewBody(view.asWidget());
  }
  
  protected void onChangeWorkstate(WorkstateEnum workstate) {
    super.onChangeWorkstate(workstate);
    
    if(SEARCH.equals(workstate) || CREATE.equals(workstate) || VIEW_DETAILS.equals(workstate) || EDIT.equals(workstate)) {
      Log.trace(this.getClass() + ".onChangeWorkstate(): set DetailForm");
      view.setBody(clientFactory.getDetailFormView().asWidget());
    } else {
      Log.trace(this.getClass() + ".onChangeWorkstate(): set ListForm");
      view.setBody(clientFactory.getListFormView().asWidget());
    }
  }

}
