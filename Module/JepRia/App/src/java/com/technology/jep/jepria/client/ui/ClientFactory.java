package com.technology.jep.jepria.client.ui;

import com.google.gwt.event.shared.EventBus;
import com.technology.jep.jepria.client.exception.ExceptionManager;
import com.technology.jep.jepria.client.message.JepMessageBox;
import com.technology.jep.jepria.client.ui.eventbus.EventFilter;
import com.technology.jep.jepria.shared.log.JepLogger;
import com.technology.jep.jepria.shared.text.JepRiaText;

public interface ClientFactory<E extends EventBus> {

  E getEventBus();

  EventFilter getEventFilter();
  UiSecurity getUiSecurity();
  
  /**
   * Получение логгера.
   * @return логгер
   */
  JepLogger getLogger();
  
  /**
   * Получение интерфейса вывода сообщений. 
   * 
   * @return интерфейс вывода сообщений
   */
  JepMessageBox getMessageBox();
  
  /**
   * Получение интерфейса представления тестовых строк.
   * 
   * @return интерфейс представления тестовых строк
   */
  JepRiaText getTexts();
  
  /**
   * Получение обработчика исключений.
   * 
   * @return обработчик исключений
   */
  ExceptionManager getExceptionManager();
}
