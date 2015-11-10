package com.technology.jep.jepria.client.entrance;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

public class Entrance {

	private static JepMainServiceAsync mainService = null;
	
	public static void setService(JepMainServiceAsync service) {
		mainService = service;
	}
	
	public static void logout() {

		mainService.logout(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				reload();
			}

			public void onSuccess(String logoutUrl) {
				if(isFrameElement() && isJSSO_CAS_Integrated()) {
						reload();
				} else {
					if(logoutUrl != null) {
						casLogout(logoutUrl);
					} else {
						reload();
					}
				}
			}

			private boolean isJSSO_CAS_Integrated() {
				return false; // TODO Убрать времянку после внедрения интегрированного решения JSSO_CAS
//				return true;
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

	private native static void casLogout(String logoutUrl) /*-{
		$wnd.location = logoutUrl;
	}-*/;
	
	private native static boolean isFrameElement() /*-{
		return window.frameElement != null;
	}-*/;	
}
