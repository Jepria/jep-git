package com.technology.jep.jepria.client.ui;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.technology.jep.jepria.client.message.JepMessageBox;
import com.technology.jep.jepria.shared.log.JepLogger;

abstract public class JepActivity<E extends EventBus, F extends ClientFactory<E>> extends AbstractActivity {

  protected F clientFactory;
  protected E eventBus;
  
  /**
   * Клиентский логгер.
   */
  protected static JepLogger logger;
  
  /**
   * Интерфейс вывода сообщений.
   */
  protected static JepMessageBox messageBox;
  
  public JepActivity(F clientFactory) {
    this.clientFactory = clientFactory;
    this.eventBus = clientFactory.getEventBus();
    this.logger = this.clientFactory.getLogger();
    this.messageBox = this.clientFactory.getMessageBox();
  }

  public String mayStop() {
    logger.trace(this.getClass() + ".mayStop()");
    return null;
  }

  public void onCancel() {
    logger.trace(this.getClass() + ".onCancel()");
  }

  public void onStop() {
    logger.trace(this.getClass() + ".onStop()");
  }
}
