package com.technology.jep.jepria.client.async;

import com.allen_sauer.gwt.log.client.Log;
import com.technology.jep.jepria.client.exception.ExceptionManagerImpl;

abstract public class LoadAsyncCallback<T> {

  public void onFailureLoad(Throwable caught) {
    Log.error(caught.getLocalizedMessage(), caught);
    ExceptionManagerImpl.instance.handleException(caught);
  }
  
  abstract public void onSuccessLoad(T loadedObject);
}
