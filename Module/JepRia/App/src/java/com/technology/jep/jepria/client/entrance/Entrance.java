package com.technology.jep.jepria.client.entrance;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

/**
 * TODO Напрашивается переименование
 * 
 * Класс обработки Logout
 */
public class Entrance {

  private static JepMainServiceAsync mainService = null;
  
  public static void setService(JepMainServiceAsync service) {
    mainService = service;
  }
  
  /**
   * Выход из приложения
   */
  public static void logout() {
    mainService.logout(Window.Location.getHref(), new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        reload();
      }

      public void onSuccess(String logoutUrl) {
        if(logoutUrl != null) {
          goTo(logoutUrl);
          reload();
        } else {
          reload();
        }
      }
    });
  }

  /**
   * Перезагрузка страницы (с учётом окружения - с Navigation или без)
   */
  private native static void reload() /*-{
    try {
      $wnd.parent.location.reload(true); // Сначала пробуем reload для фреймовой конфигурации
    } catch(error) {
      $wnd.location.reload(true); // На фреймах не сработало, значит у нас standalone-страница
    }
  }-*/;

  /**
   * Переход по заданному Url
   * 
   * @param url
   */
  private native static void goTo(String url) /*-{
    $wnd.location = url;
  }-*/;
}
