package com.technology.jep.jepria.client.ui;

import com.google.gwt.event.shared.EventBus;
import com.technology.jep.jepria.client.JepRiaClientConstant;
import com.technology.jep.jepria.client.exception.ExceptionManager;
import com.technology.jep.jepria.client.exception.ExceptionManagerImpl;
import com.technology.jep.jepria.client.history.place.JepPlaceController;
import com.technology.jep.jepria.client.message.JepMessageBox;
import com.technology.jep.jepria.client.message.JepMessageBoxImpl;
import com.technology.jep.jepria.client.ui.eventbus.EventFilter;
import com.technology.jep.jepria.client.ui.eventbus.EventFilterImpl;
import com.technology.jep.jepria.shared.log.JepLogger;
import com.technology.jep.jepria.shared.log.JepLoggerImpl;
import com.technology.jep.jepria.shared.text.JepRiaText;

abstract public class ClientFactoryImpl<E extends EventBus>  implements ClientFactory<E> {

  protected static JepLogger logger;
  
  protected JepPlaceController placeController = null;
  protected EventBus eventBus = null;
  
  protected JepMessageBox messageBox;
  protected ExceptionManager exceptionManager;
  
  protected EventFilter eventFilter;    
  
  protected UiSecurity uiSecurity;

  public ClientFactoryImpl() {
    uiSecurity = new UiSecurity();
    eventFilter = new EventFilterImpl(this);    
    
    logger = JepLoggerImpl.instance;
    messageBox = JepMessageBoxImpl.instance;
    exceptionManager = ExceptionManagerImpl.instance;
  }

  public EventFilter getEventFilter() {
    return eventFilter;
  }

  public UiSecurity getUiSecurity() {
    return uiSecurity;
  }
  
  public JepLogger getLogger() {
    return logger;
  }
  
  public JepMessageBox getMessageBox() {
    return messageBox;
  }
 
  public ExceptionManager getExceptionManager() {
    return exceptionManager;
  }

  /**
   * Тексты JepRia.
   */
  public JepRiaText getTexts() {
    return JepRiaClientConstant.JepTexts;
  }
  
}
