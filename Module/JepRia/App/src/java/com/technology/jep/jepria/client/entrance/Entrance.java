package com.technology.jep.jepria.client.entrance;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

public class Entrance {

	private static JepMainServiceAsync mainService = null;
	
	public static void setService(JepMainServiceAsync service) {
		mainService = service;
	}
	
	public static void logout() {
		mainService.logout(Window.Location.getHref(), new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				reload();
			}

			public void onSuccess(String logoutUrl) {
				if(logoutUrl != null) {
					goTo(logoutUrl);
				} else {
					reload();
				}
			}
		});
	}

	private native static void reload() /*-{
		document.domain = document.domain;
		if(window.frameElement != null) {
			$wnd.parent.location.reload(true)
		} else {
			$wnd.location.reload(true)
		}
	}-*/;

	private native static void goTo(String url) /*-{
		$wnd.location = url;
	}-*/;
	
	private native static boolean isFrameElement() /*-{
		return window.frameElement != null;
	}-*/;	
}
