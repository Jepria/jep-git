package com.technology.jep.jepria.client.entrance;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.client.security.ClientSecurity;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

public class Entrance {

	private static JepMainServiceAsync mainService = null;
	
	public static void setService(JepMainServiceAsync service) {
		mainService = service;
	}
	
	public static void logout() {
		if(isNavigationFrameExist()) {
			// Выход при помощи Navigation (для увязки работы с фремами).
			navigationLogout();
		} else {
			// Самостоятельный выход.
			mainService.logout(new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					// Попадание сюда в данном случае нормально.
					reload();
				}
	
				public void onSuccess(Void result) {
					// Попаданий сюда пока не обнаружено, но на всякий случай...
					Log.info(ClientSecurity.instance.getUsername() + ": logout success");
					reload();
				}
			});
		}
	}
	
	private native static void reload() /*-{ 
	    $wnd.location.reload(); 
	}-*/; 
	
	private native static void navigationLogout() /*-{ 
		$wnd.parent.navigation.location = "/Navigation/logoutInput.do?lastEventInRequest=6";	
	}-*/;
	
	private native static boolean isNavigationFrameExist()/*-{
		return $wnd.parent.navigation != null;
	}-*/;	

}
