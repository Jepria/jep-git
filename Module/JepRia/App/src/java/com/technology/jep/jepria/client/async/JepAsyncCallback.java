package com.technology.jep.jepria.client.async;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.client.exception.ExceptionManagerImpl;

abstract public class JepAsyncCallback<T> implements AsyncCallback<T> {

  public void onFailure(Throwable caught) {
    Log.error(caught.getLocalizedMessage(), caught);
    ExceptionManagerImpl.instance.handleException(caught);
  }

}
