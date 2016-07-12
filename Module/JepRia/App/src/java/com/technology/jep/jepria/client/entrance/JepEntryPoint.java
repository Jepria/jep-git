package com.technology.jep.jepria.client.entrance;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.DialogBox;
import com.technology.jep.jepria.client.history.DefaultPlaceHistoryMapper;
import com.technology.jep.jepria.client.history.place.JepPlaceController;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactory;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JepEntryPoint implements EntryPoint {

  protected MainClientFactory<MainEventBus, JepMainServiceAsync> clientFactory;

  public JepEntryPoint(MainClientFactory<MainEventBus, JepMainServiceAsync> clientFactory) {
    this.clientFactory = clientFactory;
  }

  public void onModuleLoad() {
    Log.trace(this.getClass() + ".onModuleLoad()");

    // Set uncaught exception handler.
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

      public void onUncaughtException(Throwable th) {
        String text = "Uncaught exception: ";
        while (th != null) {
          StackTraceElement[] stackTraceElements = th.getStackTrace();
          text += th.toString() + "\n";
          if(stackTraceElements != null) {
            for (StackTraceElement element : stackTraceElements) {
              text += "    at " + element + "\n";
            }
          }

          th = th.getCause();
          if (th != null) {
            text += "Caused by: ";
          }
        }
        DialogBox dialogBox = new DialogBox(true);
        dialogBox.getElement().getStyle().setProperty("backgroundColor", "#ABCDEF");
        System.err.print(text);
        text = text.replaceAll(" ", "&nbsp;");
        dialogBox.setHTML("<pre>" + text + "</pre>");
        dialogBox.center();
      }
    });

    // Use a deferred so that the handler catches onModuleLoad() exceptions.
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      public void execute() {
        initHistory();
        Log.info("Application started");
      }
    });
  }
  
  /**
   * Настраивает обработку History приложения.<br/>
   * Все последующие действия приложения (запуск приложения, настройка и т.п.) происходят на основе обработки History.
   */
  protected void initHistory() {
    Log.trace(this.getClass() + ".initHistory();");
    
    PlaceHistoryMapper historyMapper = createPlaceHistoryMapper();
    PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);

    JepPlaceController mainModulePlaceController = clientFactory.getPlaceController();
    MainEventBus eventBus = clientFactory.getEventBus();
    Place defaultPlace = clientFactory.getDefaultPlace();

    historyHandler.register(mainModulePlaceController, eventBus, defaultPlace);

    historyHandler.handleCurrentHistory();
  }

  /**
   * Создание экземпляра объекта отображения Place'ов в/из History Token'а.<br/>
   * Метод переопределяется в потомках для возможности создания новых Place'ов на прикладном уровне.
   *
   * @return экземпляр объекта отображения Place'ов в/из History Token'а
   */
  protected PlaceHistoryMapper createPlaceHistoryMapper() {
    return GWT.create(DefaultPlaceHistoryMapper.class);
  }

}
