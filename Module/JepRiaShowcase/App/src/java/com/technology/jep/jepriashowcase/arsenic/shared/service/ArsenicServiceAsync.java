package com.technology.jep.jepriashowcase.arsenic.shared.service;
 
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
 
public interface ArsenicServiceAsync extends JepDataServiceAsync {
	void durableFetch(long msec, AsyncCallback<Void> callback);
}
