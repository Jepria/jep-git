package com.technology.jep.jepria.client.async;

import com.google.gwt.core.client.RunAsyncCallback;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

abstract public class LoadPlainClientFactory implements RunAsyncCallback {

  private LoadAsyncCallback<PlainClientFactory<PlainEventBus, JepDataServiceAsync>> callback;
  
  public LoadPlainClientFactory(LoadAsyncCallback<PlainClientFactory<PlainEventBus, JepDataServiceAsync>> callback) {
    this.callback = callback;
  }
  
  public abstract PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory();

  public void onFailure(Throwable caught) {
    callback.onFailureLoad(caught);
  }
  
  public void onSuccess() {
    callback.onSuccessLoad(getPlainClientFactory());
  }
  
}
