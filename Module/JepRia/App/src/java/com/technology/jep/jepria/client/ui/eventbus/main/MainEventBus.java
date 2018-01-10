package com.technology.jep.jepria.client.ui.eventbus.main;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.eventbus.JepEventBus;
import com.technology.jep.jepria.client.ui.eventbus.event.ExitScopeEvent;
import com.technology.jep.jepria.client.ui.eventbus.main.event.EnterFromHistoryEvent;
import com.technology.jep.jepria.client.ui.eventbus.main.event.SetMainViewBodyEvent;
import com.technology.jep.jepria.client.ui.eventbus.main.event.SetMainViewEvent;
import com.technology.jep.jepria.client.ui.eventbus.main.event.StartEvent;

public class MainEventBus extends JepEventBus {

  public MainEventBus() {
    super();
  }

  public void start() {
    fireEvent(new StartEvent());
  }

  public void enterFromHistory(Place place) {
    fireEvent(new EnterFromHistoryEvent(place));
  }

  public void setMainView(IsWidget mainView) {
    fireEvent(new SetMainViewEvent(mainView));
  }

  public void setMainViewBody(Widget bodyWidget) {
    fireEvent(new SetMainViewBodyEvent(bodyWidget));
  }

  public void exitScope(ExitScopeEvent exitScopeEvent) {
    fireEvent(exitScopeEvent);
  }

}
